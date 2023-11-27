package tech.twocats.admin.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.twocats.admin.common.enums.MenuTypeEnum;
import tech.twocats.admin.module.user.domain.entity.Menu;
import tech.twocats.admin.module.user.domain.entity.RoleMenu;
import tech.twocats.admin.module.user.domain.entity.UserRole;
import tech.twocats.admin.module.user.domain.vo.MenuQuery;
import tech.twocats.admin.module.user.domain.vo.MenuVO;
import tech.twocats.admin.module.user.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import tech.twocats.admin.module.user.service.IMenuService;
import tech.twocats.admin.module.user.service.IRoleMenuService;
import tech.twocats.admin.module.user.service.IRoleService;
import tech.twocats.admin.module.user.service.IUserRoleService;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements IMenuService {

    private final IRoleService roleService;
    private final IUserRoleService userRoleService;
    private final IRoleMenuService roleMenuService;

    public MenuServiceImpl(IRoleService roleService, IUserRoleService userRoleService, IRoleMenuService roleMenuService) {
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.roleMenuService = roleMenuService;
    }

    @Override
    public List<MenuVO> getMenusByUserId(Long userId) {
        List<UserRole> userRoles = ChainWrappers.lambdaQueryChain(userRoleService.getBaseMapper())
                .eq(UserRole::getUserId, userId).list();
        if (CollectionUtils.isEmpty(userRoles)){
            return new ArrayList<>(1);
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        Long superAdminId = roleService.getSuperAdminId();
        boolean isSuperAdmin = roleIds.contains(superAdminId);

        List<RoleMenu> roleMenus = ChainWrappers.lambdaQueryChain(roleMenuService.getBaseMapper())
                .in(RoleMenu::getRoleId, roleIds).list();
        if (CollectionUtils.isEmpty(roleMenus)){
            return new ArrayList<>(1);
        }

        List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).distinct().collect(Collectors.toList());
        List<Menu> menus = ChainWrappers.lambdaQueryChain(this.getBaseMapper())
                .in(!isSuperAdmin, Menu::getId, menuIds)
                .eq(Menu::getType, MenuTypeEnum.MENU)
                .orderByAsc(Menu::getPid)
                .orderByAsc(Menu::getSort)
                .list();
        return toMenuTree(menus);
    }

    @Override
    public List<MenuVO> getMenus(MenuQuery query) {
        List<Menu> menus = ChainWrappers.lambdaQueryChain(this.getBaseMapper())
                .ne(Menu::getAuthority, "ADMIN")
                .like(StringUtils.hasLength(query.getTitle()), Menu::getTitle, query.getTitle())
                .orderByAsc(Menu::getPid)
                .orderByAsc(Menu::getSort)
                .list();
        return menus.stream().map(MenuVO::new).collect(Collectors.toList());
    }

    public List<MenuVO> toMenuTree(List<Menu> menus){
        Map<Long, MenuVO> menuMap= new HashMap<>();
        List<MenuVO> menuVOS = new ArrayList<>();
        menus.forEach(menu -> {
            MenuVO menuVO = new MenuVO(menu);
            menuMap.put(menu.getId(), menuVO);
            if (menu.getPid() != -1){
                MenuVO parentMenuVO = menuMap.get(menu.getPid());
                Optional.ofNullable(parentMenuVO)
                        .ifPresent(parentMenu -> parentMenuVO.getChild().add(menuVO));
            }else {
                menuVOS.add(menuVO);
            }
        });
        return menuVOS;
    }

}




