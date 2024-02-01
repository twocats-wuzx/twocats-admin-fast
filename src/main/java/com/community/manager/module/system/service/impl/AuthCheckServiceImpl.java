package com.community.manager.module.system.service.impl;

import com.community.manager.common.AppConstant;
import com.community.manager.module.system.service.IAuthCheckService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("authCheck")
public class AuthCheckServiceImpl implements IAuthCheckService {

    private final SecurityContextHolderStrategy securityContextHolderStrategy
            = SecurityContextHolder.getContextHolderStrategy();
    @Override
    public boolean check(String... permissions) {
        SecurityContext context = securityContextHolderStrategy.getContext();
        if (context == null
                || context.getAuthentication() == null
                || !context.getAuthentication().isAuthenticated()){
            return false;
        }
        List<String> authorities = context.getAuthentication()
                .getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        if (authorities.contains(AppConstant.SUPER_ADMIN_AUTH_CODE)){
            return true;
        }
        for (String permission : permissions) {
            if (authorities.contains(permission)){
                return true;
            }
        }
        return false;
    }

}
