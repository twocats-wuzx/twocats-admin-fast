package tech.twocats.admin.config;

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
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import tech.twocats.admin.handler.AdminAuthEntryPoint;
import tech.twocats.admin.handler.AdminAccessDeniedHandler;
import tech.twocats.admin.handler.AdminAuthFailureHandler;
import tech.twocats.admin.handler.AdminAuthSuccessHandler;

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
                                                 HandlerMappingIntrospector introspector,
                                                 HttpSessionSecurityContextRepository httpSessionSecurityContextRepository) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(
                                        AntPathRequestMatcher.antMatcher("/api/web/**")
                                ).authenticated()
                                .requestMatchers(
                                        mvcMatcherBuilder.pattern("/setting/**")
                                ).permitAll()
                        /*.anyRequest().authenticated()*/
                )
                .sessionManagement(session -> session
                        .maximumSessions(2)
                        // 是否阻止达到最大会话数之后的再次登录
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/login")
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                // 基于HttpSession的会话管理
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(true)
                        .securityContextRepository(new DelegatingSecurityContextRepository(
                                httpSessionSecurityContextRepository
                        ))
                );
        return http.build();
    }

    @Bean
    @Order(100)
    public SecurityFilterChain adminFilterChain(HttpSecurity http,
                                                HandlerMappingIntrospector introspector,
                                                AdminAuthEntryPoint adminAuthEntryPoint,
                                                AdminAccessDeniedHandler adminAccessDeniedHandler,
                                                AdminAuthSuccessHandler adminAuthSuccessHandler,
                                                AdminAuthFailureHandler adminAuthFailureHandler,
                                                HttpSessionSecurityContextRepository httpSessionSecurityContextRepository) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(headers -> headers
                        .frameOptions().sameOrigin()
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/api/admin/login"),
                                AntPathRequestMatcher.antMatcher("/api/admin/logout")
                        ).permitAll()
                        .requestMatchers(
                                mvcMatcherBuilder.pattern("/admin/login")
                        ).permitAll()
                        .requestMatchers(
                                AntPathRequestMatcher.antMatcher("/api/admin/**"),
                                mvcMatcherBuilder.pattern("/admin/**")
                        ).authenticated()
                )
                .exceptionHandling(exceptionHandler -> exceptionHandler
                        .accessDeniedHandler(adminAccessDeniedHandler)
                        .authenticationEntryPoint(adminAuthEntryPoint)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/admin/logout")
                        .logoutSuccessUrl("/admin/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                )
                // 基于HttpSession的会话管理
                .sessionManagement(session -> session
                        .maximumSessions(2)
                        // 是否阻止达到最大会话数之后的再次登录
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/admin/login")
                )
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(true)
                        .securityContextRepository(new DelegatingSecurityContextRepository(
                                httpSessionSecurityContextRepository
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
            @Qualifier("adminUserDetailServiceImpl") UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

}
