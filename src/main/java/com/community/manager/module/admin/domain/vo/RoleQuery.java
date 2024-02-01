package com.community.manager.module.admin.domain.vo;


import com.community.manager.common.model.vo.PageQuery;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleQuery extends PageQuery {

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色编码
     */
    private String roleCode;

}
