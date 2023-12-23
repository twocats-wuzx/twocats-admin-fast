package tech.twocats.admin.module.admin.domain.vo;


import lombok.Getter;
import lombok.Setter;
import tech.twocats.admin.common.model.vo.Page;

@Getter
@Setter
public class RoleQuery extends Page {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

}
