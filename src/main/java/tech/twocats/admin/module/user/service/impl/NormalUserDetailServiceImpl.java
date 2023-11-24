package tech.twocats.admin.module.user.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.twocats.admin.module.user.domain.dto.UserDetailDTO;
import tech.twocats.admin.module.user.domain.entity.Menu;
import tech.twocats.admin.module.user.domain.entity.User;
import tech.twocats.admin.module.user.service.IUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service(value = "normalUserDetailServiceImpl")
public class NormalUserDetailServiceImpl implements UserDetailsService {

    private final IUserService userService;

    public NormalUserDetailServiceImpl(IUserService userService) {
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
