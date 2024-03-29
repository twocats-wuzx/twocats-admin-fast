package com.community.manager.module.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.community.manager.common.model.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@TableName(value ="t_role_menu")
@EqualsAndHashCode(callSuper = true)
public class RoleMenu extends BaseEntity implements Serializable {

    /**
     * 角色ID
     */
    @TableField(value = "role_id")
    private Long roleId;

    /**
     * 角色ID
     */
    @TableField(value = "menu_id")
    private Long menuId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}