package tech.twocats.admin.module.admin.domain.vo;

import lombok.Getter;
import lombok.Setter;
import tech.twocats.admin.common.ValidateGroup;
import tech.twocats.admin.common.enums.GenderEnum;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
public class UserRequest {

    @NotNull(message = "用户ID不能为空",
            groups = {ValidateGroup.Edit.class, ValidateGroup.StatusChange.class, ValidateGroup.UploadAvatar.class})
    private Long id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空",
            groups = {ValidateGroup.Add.class, ValidateGroup.Edit.class})
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "用户密码不能为空",
            groups = {ValidateGroup.Add.class, ValidateGroup.Edit.class, ValidateGroup.RePassword.class})
    private String password;

    /**
     * 新用户密码
     */
    @NotBlank(message = "新密码不能为空",
            groups = {ValidateGroup.RePassword.class})
    private String newPassword;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickname;

    @NotBlank(message = "用户状态不能为空",
            groups = {ValidateGroup.StatusChange.class})
    private Boolean status;


    @NotBlank(message = "用户头像不能为空",
            groups = {ValidateGroup.UploadAvatar.class})
    private String avatar;


    /**
     * 性别 MALE.男 FEMALE.女
     */
    @NotBlank(message = "用户性别不能为空",
            groups = {ValidateGroup.Add.class, ValidateGroup.Edit.class})
    private GenderEnum gender;

    /**
     * 邮箱地址
     */
    @Email(message = "邮箱格式错误")
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误" )
    private String mobile;

    private List<Long> roleIds;

}
