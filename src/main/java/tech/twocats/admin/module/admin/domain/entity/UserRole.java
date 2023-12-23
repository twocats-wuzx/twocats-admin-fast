package tech.twocats.admin.module.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.twocats.admin.common.model.entity.BaseEntity;


@Data
@TableName(value ="t_user_role")
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseEntity implements Serializable {

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 角色ID
     */
    @TableField(value = "role_id")
    private Long roleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}