package com.community.manager.module.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.community.manager.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@TableName(value ="t_role")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity implements Serializable {

    /**
     * 角色名称
     */
    @TableField(value = "role_name")
    private String roleName;

    /**
     * 角色编码
     */
    @TableField(value = "role_code")
    private String roleCode;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 启用状态 0.禁用 1.启用
     */
    @TableField(value = "status")
    private Boolean status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}