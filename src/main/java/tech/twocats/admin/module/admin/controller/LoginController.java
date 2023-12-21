package tech.twocats.admin.module.admin.controller;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.twocats.admin.common.model.Result;
import tech.twocats.admin.module.admin.domain.vo.LoginRequest;
import tech.twocats.admin.module.admin.service.IUserService;
import tech.twocats.admin.util.AuthExceptionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private final IUserService userService;
    private final AuthenticationManager normalAuthenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final SecurityContextHolderStrategy securityContextHolderStrategy
            = SecurityContextHolder.getContextHolderStrategy();

    public LoginController(IUserService userService,
                           @Qualifier("normalAuthenticationManager") AuthenticationManager normalAuthenticationManager,
                           SecurityContextRepository securityContextRepository) {
        this.userService = userService;
        this.normalAuthenticationManager = normalAuthenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @RequestMapping("/login")
    public String loginView(){
        return "login";
    }

    /**
     * 普通用户登录
     * @param loginRequest 登录请求参数
     */
    @ResponseBody
    @PostMapping("/api/login")
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
    @ResponseBody
    @PostMapping("/admin/api/login")
    public Result<Void> adminLogin(@RequestBody LoginRequest loginRequest,
                              HttpServletRequest request, HttpServletResponse response){
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


}
