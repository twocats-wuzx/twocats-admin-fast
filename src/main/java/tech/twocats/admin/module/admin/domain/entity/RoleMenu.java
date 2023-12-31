package tech.twocats.admin.module.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.twocats.admin.common.model.entity.BaseEntity;


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