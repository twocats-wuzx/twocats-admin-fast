package com.community.manager.config;

import com.community.manager.auth.*;
import com.community.manager.module.system.service.impl.AdminUserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository(){
        return new HttpSessionSecurityContextRepository();
    }

    /**
     * 并发会话控制监听器配置
     *  - 提供并发会话支持
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    @Order(200)
    public SecurityFilterChain normalFilterChain(HttpSecurity http,
                                                 RedisSecurityContextRepository redisSecurityContextRepository)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/api/web/captcha")
                        ).permitAll()
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/api/web/**")
                        ).authenticated()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/web/logout")
                        .deleteCookies("JSESSIONID")
                )
                .sessionManagement(AbstractHttpConfigurer::disable)
                // 基于Redis的SpringSecurity管理
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(true)
                        .securityContextRepository(new DelegatingSecurityContextRepository(
                                redisSecurityContextRepository
                        ))
                );
        return http.build();
    }

    @Bean
    @Order(100)
    public SecurityFilterChain adminFilterChain(HttpSecurity http,
                                                AdminAuthEntryPoint adminAuthEntryPoint,
                                                AdminAccessDeniedHandler adminAccessDeniedHandler,
                                                AdminAuthSuccessHandler adminAuthSuccessHandler,
                                                AdminAuthFailureHandler adminAuthFailureHandler,
                                                RedisSecurityContextRepository redisSecurityContextRepository) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions().sameOrigin()
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/api/admin/login"),
                                AntPathRequestMatcher.antMatcher("/api/admin/logout"),
                                AntPathRequestMatcher.antMatcher("/api/admin/captcha")
                        ).permitAll()
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/api/admin/**")
                        ).authenticated()
                )
                .exceptionHandling(exceptionHandler -> exceptionHandler
                        .accessDeniedHandler(adminAccessDeniedHandler)
                        .authenticationEntryPoint(adminAuthEntryPoint)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/admin/logout")
                        .deleteCookies("JSESSIONID")
                )
                // 基于HttpSession的会话管理
                .sessionManagement(AbstractHttpConfigurer::disable)
                // 基于Redis的SpringSecurity管理
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(true)
                        .securityContextRepository(new DelegatingSecurityContextRepository(
                                redisSecurityContextRepository
                        ))
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(formLogin -> formLogin
                        .successHandler(adminAuthSuccessHandler)
                        .failureHandler(adminAuthFailureHandler)
                );
        return http.build();
    }


    @Primary
    @Bean(name = "normalAuthenticationManager")
    public AuthenticationManager normalAuthenticationManager(
            @Qualifier("normalUserDetailServiceImpl") UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

    @Bean(name = "adminAuthenticationManager")
    public AuthenticationManager adminAuthenticationManager(
            AdminUserDetailServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

}
