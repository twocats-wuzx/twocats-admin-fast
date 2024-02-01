package com.community.manager.module.system.service.impl;

import com.community.manager.module.system.domain.dto.UserDetailDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.community.manager.module.admin.domain.entity.Menu;
import com.community.manager.module.admin.domain.entity.User;
import com.community.manager.module.admin.service.IUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service(value = "adminUserDetailServiceImpl")
public class AdminUserDetailServiceImpl implements UserDetailsService {

    private final IUserService userService;

    public AdminUserDetailServiceImpl(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.queryByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("未找到用户信息");
        }
        List<Menu> permissions = userService.getPermissionsByUserId(user.getId());
        return buildUserDetails(user, permissions);
    }

    private UserDetails buildUserDetails(User user, List<Menu> permissions){
        return UserDetailDTO
                .build(user, permissions.stream().map(Menu::getAuthority).collect(Collectors.toList()));
    }

}
