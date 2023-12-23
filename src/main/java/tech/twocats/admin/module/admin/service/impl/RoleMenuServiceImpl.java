package tech.twocats.admin.module.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tech.twocats.admin.common.AppConstant;
import tech.twocats.admin.common.enums.MenuTypeEnum;
import tech.twocats.admin.module.admin.domain.entity.Menu;
import tech.twocats.admin.module.admin.domain.entity.RoleMenu;
import tech.twocats.admin.module.admin.service.IMenuService;
import tech.twocats.admin.module.admin.service.IRoleMenuService;
import tech.twocats.admin.module.admin.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements IRoleMenuService {

    private final IMenuService menuService;

    public RoleMenuServiceImpl(@Lazy IMenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 通过角色ID查询所有的菜单和权限
     * @param roleIds 角色ID列表
     * @return 菜单权限列表, 或者空列表
     */
    @NonNull
    public List<Menu> getMenusByRoleIds(@NonNull List<Long> roleIds, MenuTypeEnum menuTypeEnum) {
        if (roleIds.isEmpty()){
            return new ArrayList<>(1);
        }
        boolean isSuperAdmin = roleIds.contains(AppConstant.SUPER_ADMIN_ID);
        return Optional
                .ofNullable(this.baseMapper.getMenusByRoleIds(
                        roleIds, isSuperAdmin,
                        menuTypeEnum == null ? null : menuTypeEnum.getValue()))
                .orElse(new ArrayList<>(1));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenus(Long id, List<Long> menuIds) {
        // 删除角色的所有菜单
        this.removeByRoleId(id);
        // 校验菜单ID是否存在并删除不存在的ID
        menuIds = menuService.validateMenuIds(menuIds);

        // 保存角色的菜单
        if (!CollectionUtils.isEmpty(menuIds)){
            List<RoleMenu> roleMenus = menuIds.stream()
                    .map(menuId -> {
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setRoleId(id);
                        roleMenu.setMenuId(menuId);
                        return roleMenu;
                    }).collect(Collectors.toList());
            this.saveBatch(roleMenus);
        }
    }

    /**
     * 删除角色所有关联的菜单
     * @param roleId 角色ID
     */
    private void removeByRoleId(Long roleId) {
        this.lambdaUpdate().eq(RoleMenu::getRoleId, roleId).remove();
    }

}




