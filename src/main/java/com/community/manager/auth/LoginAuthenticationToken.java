package com.community.manager.auth;

import com.community.manager.module.system.domain.dto.UserDetailDTO;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 登录认证令牌
 */
public class LoginAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetailDTO principal;

    /**
     * 构造登录认证令牌
     * @param principal 授权主体
     */
    public LoginAuthenticationToken(UserDetailDTO principal) {
        super(principal.getAuthorities());
        this.setAuthenticated(true);
        this.principal = principal;
        this.principal.setPassword(null);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * 获取授权主体
     * @return 授权主体
     */
    @Override
    public Object getPrincipal() {
        return principal;
    }

    /**
     * 获取用户信息
     * @return 用户信息
     */
    public UserDetailDTO getUserDetail() {
        return principal;
    }
}
