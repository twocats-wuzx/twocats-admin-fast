package tech.twocats.admin.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import org.springframework.util.CollectionUtils;
import tech.twocats.admin.common.enums.MenuTypeEnum;
import tech.twocats.admin.module.user.domain.entity.Menu;
import tech.twocats.admin.module.user.domain.entity.RoleMenu;
import tech.twocats.admin.module.user.domain.entity.User;
import tech.twocats.admin.module.user.domain.entity.UserRole;
import tech.twocats.admin.module.user.domain.vo.MenuVO;
import tech.twocats.admin.module.user.service.*;
import tech.twocats.admin.module.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IUserService {

    private final IMenuService menuService;
    private final IUserRoleService userRoleService;
    private final IRoleMenuService roleMenuService;

    public UserServiceImpl(IMenuService menuService,
                           IUserRoleService userRoleService,
                           IRoleMenuService roleMenuService) {
        this.menuService = menuService;
        this.userRoleService = userRoleService;
        this.roleMenuService = roleMenuService;
    }

    /**
     * 通过用户名查询账号信息
     * @param username 用户名
     * @return @link { User }
     */
    @Override
    public User queryByUsername(String username){
        return ChainWrappers.lambdaQueryChain(this.baseMapper)
                .eq(User::getUsername, username)
                .one();
    }

    @Override
    public List<Menu> getPermissionsByUserId(Long userId) {
        List<UserRole> userRoles = ChainWrappers.lambdaQueryChain(userRoleService.getBaseMapper())
                .eq(UserRole::getUserId, userId).list();
        if (CollectionUtils.isEmpty(userRoles)){
            return new ArrayList<>(1);
        }

        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RoleMenu> roleMenus = ChainWrappers.lambdaQueryChain(roleMenuService.getBaseMapper())
                .in(RoleMenu::getRoleId, roleIds).list();
        if (CollectionUtils.isEmpty(roleMenus)){
            return new ArrayList<>(1);
        }

        List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).distinct().collect(Collectors.toList());
        return ChainWrappers.lambdaQueryChain(menuService.getBaseMapper())
                .in(Menu::getId, menuIds)
                .eq(Menu::getType, MenuTypeEnum.PERMISSION)
                .list();
    }

}




