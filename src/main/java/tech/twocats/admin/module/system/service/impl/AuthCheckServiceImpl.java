package tech.twocats.admin.module.system.service.impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Service;
import tech.twocats.admin.module.system.service.IAuthCheckService;

import java.util.Collection;
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

        if (authorities.contains("ADMIN")){
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
