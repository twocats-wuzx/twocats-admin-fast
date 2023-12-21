package tech.twocats.admin.module.admin.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.twocats.admin.common.enums.GenderEnum;


@Data
@TableName(value ="t_user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity implements Serializable {

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 真实姓名
     */
    @TableField(value = "realName")
    private String realName;

    /**
     * 昵称
     */
    @TableField(value = "nickname")
    private String nickname;

    /**
     * 头像
     */
    @TableField(value = "avatar")
    private String avatar;

    /**
     * 性别 MALE.男 FEMALE.女
     */
    @TableField(value = "gender")
    private GenderEnum gender;

    /**
     * 邮箱地址
     */
    @TableField(value = "email")
    private String email;

    /**
     * 手机号码
     */
    @TableField(value = "mobile")
    private String mobile;

    /**
     * 状态 0.禁用 1.启用
     */
    @TableField(value = "status")
    private Boolean status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}