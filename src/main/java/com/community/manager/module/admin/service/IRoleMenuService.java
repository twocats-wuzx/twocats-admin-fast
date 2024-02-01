package com.community.manager.module.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.manager.common.enums.MenuTypeEnum;
import com.community.manager.module.admin.domain.entity.Menu;
import com.community.manager.module.admin.domain.entity.RoleMenu;
import org.springframework.lang.NonNull;
import java.util.List;


public interface IRoleMenuService extends IService<RoleMenu> {

    /**
     * 通过角色ID查询所有的菜单
     * @param roleIds 角色ID列表
     * @param menuTypeEnum 菜单类型
     * @return 菜单列表
     */
    @NonNull
    List<Menu> getMenusByRoleIds(@NonNull List<Long> roleIds, MenuTypeEnum menuTypeEnum);

    /**
     * 保存角色的菜单信息
     * @param id 角色ID
     * @param menuIds 菜单ID列表
     */
    void saveRoleMenus(Long id, List<Long> menuIds);
}
