package com.community.manager.module.admin.mapper;

import com.community.manager.module.admin.domain.entity.Menu;
import com.community.manager.module.admin.domain.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 通过角色ID查询所有的菜单和权限
     *
     * @param roleIds      角色ID列表
     * @param isSuperAdmin 是否有超管角色
     * @param menuType 菜单类型
     * @return 菜单列表
     */
    List<Menu> getMenusByRoleIds(@Param("roleIds") List<Long> roleIds,
                                 @Param("isSuperAdmin") boolean isSuperAdmin,
                                 @Param("menuType") String menuType);
}




