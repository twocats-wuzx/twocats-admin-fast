package com.community.manager.module.admin.domain.vo;

import com.community.manager.module.admin.domain.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class RoleVO {

    private Long id;

    private String roleName;

    private String roleCode;

    private String remark;

    private Boolean status;

    private List<Long> menuIds;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public static RoleVO fromRole(Role role){
        if (role == null){
            return null;
        }
        return getRoleVOByRole(role);
    }

    public static RoleVO fromRole(Role role, List<Long> menuIds){
        if (role == null){
            return null;
        }
        RoleVO roleVO = getRoleVOByRole(role);
        // 角色所有的菜单
        roleVO.setMenuIds(menuIds);
        return roleVO;
    }

    /**
     * 从角色实体中拷贝角色信息
     * @param role 角色实体
     * @return 角色VO
     */
    private static RoleVO getRoleVOByRole(Role role) {
        RoleVO roleVO = new RoleVO();
        roleVO.setId(role.getId());
        roleVO.setRoleName(role.getRoleName());
        roleVO.setRoleCode(role.getRoleCode());
        roleVO.setRemark(role.getRemark());
        roleVO.setStatus(role.getStatus());
        roleVO.setCreateTime(role.getCreateTime());
        return roleVO;
    }
}
