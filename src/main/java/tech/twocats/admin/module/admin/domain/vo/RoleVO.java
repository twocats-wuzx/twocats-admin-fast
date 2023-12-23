package tech.twocats.admin.module.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import tech.twocats.admin.module.admin.domain.entity.Menu;
import tech.twocats.admin.module.admin.domain.entity.Role;
import tech.twocats.admin.module.admin.domain.entity.RoleMenu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class RoleVO {

    private Long id;

    private String roleName;

    private String roleCode;

    private String remark;

    private Boolean status;

    private List<Long> menus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public static RoleVO fromRole(Role role){
        if (role == null){
            return null;
        }
        return getRoleVO(role);
    }

    public static RoleVO fromRole(Role role, List<Long> menuIds){
        if (role == null){
            return null;
        }
        RoleVO roleVO = getRoleVO(role);
        // 角色所有的菜单
        roleVO.setMenus(menuIds);
        return roleVO;
    }

    /**
     * 从角色实体中拷贝角色信息
     * @param role 角色实体
     * @return 角色VO
     */
    private static RoleVO getRoleVO(Role role) {
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
