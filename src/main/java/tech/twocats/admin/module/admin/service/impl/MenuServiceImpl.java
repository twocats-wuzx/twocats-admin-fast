package tech.twocats.admin.module.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.twocats.admin.common.enums.MenuTypeEnum;
import tech.twocats.admin.common.error.SystemError;
import tech.twocats.admin.common.model.vo.LongListWrapper;
import tech.twocats.admin.exception.BaseException;
import tech.twocats.admin.module.admin.domain.entity.Menu;
import tech.twocats.admin.module.admin.domain.entity.RoleMenu;
import tech.twocats.admin.module.admin.domain.entity.UserRole;
import tech.twocats.admin.module.admin.domain.vo.MenuQuery;
import tech.twocats.admin.module.admin.domain.vo.MenuRequest;
import tech.twocats.admin.module.admin.domain.vo.MenuVO;
import tech.twocats.admin.module.admin.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import tech.twocats.admin.module.admin.service.IMenuService;
import tech.twocats.admin.module.admin.service.IRoleMenuService;
import tech.twocats.admin.module.admin.service.IRoleService;
import tech.twocats.admin.module.admin.service.IUserRoleService;

import javax.validation.constraints.NotNull;
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
        List<UserRole> userRoles = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, userId).list();
        if (CollectionUtils.isEmpty(userRoles)){
            return new ArrayList<>(1);
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        Long superAdminId = roleService.getSuperAdminId();
        boolean isSuperAdmin = roleIds.contains(superAdminId);

        List<RoleMenu> roleMenus = roleMenuService.lambdaQuery()
                .in(RoleMenu::getRoleId, roleIds).list();
        if (CollectionUtils.isEmpty(roleMenus)){
            return new ArrayList<>(1);
        }

        List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).distinct().collect(Collectors.toList());
        List<Menu> menus = this.lambdaQuery()
                .in(!isSuperAdmin, Menu::getId, menuIds)
                .eq(Menu::getType, MenuTypeEnum.MENU)
                .orderByAsc(Menu::getPid)
                .orderByAsc(Menu::getSort)
                .list();
        return toMenuTree(menus);
    }

    @Override
    public List<MenuVO> getMenus(MenuQuery query) {
        List<Menu> menus = this.lambdaQuery()
                .ne(Menu::getAuthority, "ADMIN")
                .like(StringUtils.hasLength(query.getTitle()), Menu::getTitle, query.getTitle())
                .orderByAsc(Menu::getPid)
                .orderByAsc(Menu::getSort)
                .list();
        if (CollectionUtils.isEmpty(menus)){
            return new ArrayList<>();
        }
        return toMenuTree(menus);
    }

    @Override
    public void saveMenu(MenuRequest request) {
        MenuRequest.checkMenu(request);
        // 判断父级菜单是否存在
        if (request.getPid() != null){
            this.lambdaQuery()
                    .eq(Menu::getId, request.getPid())
                    .oneOpt()
                    .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "父级菜单不存在"));
        }

        Menu menu = MenuRequest.toMenu(request);
        this.save(menu);
    }

    @Override
    public void editMenu(MenuRequest request) {
        MenuRequest.checkMenu(request);
        // 判断菜单是否存在
        this.lambdaQuery()
                .eq(Menu::getId, request.getId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "菜单不存在"));

        Menu menu = MenuRequest.toMenu(request);
        this.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(@NotNull LongListWrapper ids) {
        // 通过ids删除菜单
        this.removeByIds(ids.getKeys());
    }

    @Override
    public void changeMenuStatus(MenuRequest request) {
        // 判断菜单是否存在
        this.lambdaQuery()
                .eq(Menu::getId, request.getId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "菜单不存在"));

        // 更新数据库中菜单的状态
        Menu menu = new Menu();
        menu.setId(request.getId());
        menu.setStatus(request.getStatus());
        this.updateById(menu);
    }

    public List<MenuVO> toMenuTree(List<Menu> menus){
        List<Long> ids = menus.stream().map(Menu::getId).collect(Collectors.toList());
        Map<Long, MenuVO> menuVOMap = menus.stream()
                .map(MenuVO::fromMenu).collect(Collectors.toMap(
                        MenuVO::getId,
                        o -> o,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));

        List<MenuVO> menuVOS = new ArrayList<>();
        menuVOMap.forEach((key, value) -> {
            if (ids.contains(value.getPid())){
                MenuVO parentMenuVO = menuVOMap.get(value.getPid());
                Optional.ofNullable(parentMenuVO)
                        .ifPresent(parentMenu -> parentMenuVO.getChild().add(value));
            }else {
                menuVOS.add(value);
            }
        });
        menuVOMap.forEach((key, value) -> {
            if (!value.getStatus()){
                value.setChild(null);
            }
        });

        menuVOS.sort(Comparator.comparingInt(MenuVO::getSort));
        return menuVOS;
    }

}




