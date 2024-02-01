package com.community.manager.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.community.manager.common.model.vo.LongListWrapper;
import com.community.manager.module.admin.domain.entity.Role;
import com.community.manager.module.admin.domain.vo.RoleQuery;
import com.community.manager.module.admin.domain.vo.RoleRequest;
import com.community.manager.module.admin.domain.vo.RoleVO;
import org.springframework.lang.NonNull;

import java.util.List;


public interface IRoleService extends IService<Role> {

    Long getSuperAdminId();

    /**
     * 获取角色列表
     * @param query 查询字段，包含分页，筛选字段
     * @return 角色列表
     */
    IPage<Role> getRoles(RoleQuery query);

    /**
     * 获取角色明细信息，包含角色的菜单和权限
     * @param key 角色ID
     * @return 角色明细信息
     */
    RoleVO getRoleDetail(Long key);

    /**
     * 新增角色
     * @param request 角色信息
     */
    void saveRole(RoleRequest request);

    /**
     * 删除角色
     * @param ids 角色信息
     */
    void deleteRole(LongListWrapper ids);

    /**
     * 编辑角色
     * @param request 角色信息
     */
    void editRole(RoleRequest request);

    /**
     * 修改角色状态
     * @param request 角色信息
     */
    void changeRoleStatus(RoleRequest request);

    /**
     * 校验角色ID是否存在
     * @param roleIds 角色ID列表
     * @return 角色ID列表
     */
    @NonNull
    List<Long> validateRoleIds(List<Long> roleIds);

    /**
     * 获取角色穿梭框数据
     * @return 角色ID名称列表
     */
    List<RoleVO> getRoleTransfer();
}
