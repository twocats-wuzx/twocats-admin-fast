package tech.twocats.admin.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import tech.twocats.admin.common.model.vo.LongListWrapper;
import tech.twocats.admin.module.admin.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.twocats.admin.module.admin.domain.vo.RoleQuery;
import tech.twocats.admin.module.admin.domain.vo.RoleRequest;
import tech.twocats.admin.module.admin.domain.vo.RoleVO;

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
    void changeMenuStatus(RoleRequest request);
}
