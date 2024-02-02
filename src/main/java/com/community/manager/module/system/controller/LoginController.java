package com.community.manager.module.system.controller;

import com.community.manager.auth.RedisSecurityContextRepository;
import com.community.manager.common.model.vo.Result;
import com.community.manager.module.system.domain.vo.LoginRequest;
import com.community.manager.util.AuthExceptionUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final AuthenticationManager normalAuthenticationManager;
    private final AuthenticationManager adminAuthenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy
            = SecurityContextHolder.getContextHolderStrategy();

    public LoginController(@Qualifier("normalAuthenticationManager") AuthenticationManager normalAuthenticationManager,
                           @Qualifier("adminAuthenticationManager") AuthenticationManager adminAuthenticationManager,
                           RedisSecurityContextRepository securityContextRepository) {
        this.normalAuthenticationManager = normalAuthenticationManager;
        this.adminAuthenticationManager = adminAuthenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    /**
     * 普通用户登录
     * @param loginRequest 登录请求参数
     */
    @PostMapping("/login")
    public Result<Void> login(@RequestBody LoginRequest loginRequest,
                              HttpServletRequest request,
                              HttpServletResponse response){
        UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken
                        .unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        try {
            Authentication authentication  = this.normalAuthenticationManager.authenticate(token);
            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);
            securityContextRepository.saveContext(context, request, response);
        }catch (AuthenticationException exception) {
            return Result.fail(AuthExceptionUtil.getErrorTypeByException(exception));
        }
        return Result.ok();
    }

    /**
     * 管理员用户登录
     * @param loginRequest 登录请求参数
     */
    @PostMapping("/admin/login")
    public Result<Void> adminLogin(@RequestBody LoginRequest loginRequest,
                              HttpServletRequest request, HttpServletResponse response){
        UsernamePasswordAuthenticationToken token =
                UsernamePasswordAuthenticationToken
                        .unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        try {
            Authentication authentication  = this.adminAuthenticationManager.authenticate(token);
            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextRepository.saveContext(context, request, response);
        }catch (AuthenticationException exception) {
            return Result.fail(AuthExceptionUtil.getErrorTypeByException(exception));
        }
        return Result.ok();
    }

    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public Result<String> getCaptcha(){
        return null;
    }

}
