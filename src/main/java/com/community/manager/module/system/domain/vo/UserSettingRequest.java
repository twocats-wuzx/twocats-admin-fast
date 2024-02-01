package com.community.manager.module.system.domain.vo;

import com.community.manager.common.enums.GenderEnum;
import com.community.manager.module.admin.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Optional;

@Getter
@Setter
public class UserSettingRequest {

    @JsonIgnore
    private Long userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别 MALE.男 FEMALE.女
     */
    @NotBlank(message = "用户性别不能为空")
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

    /**
     * 更新用户信息的值
     * @param user 需要更新的用户
     * @return 更新后的用户实体
     */
    public static User updateUser(User user, UserSettingRequest request){
        if (user == null){
            return null;
        }
        Optional.ofNullable(request.getRealName()).ifPresent(user::setRealName);
        Optional.ofNullable(request.getNickname()).ifPresent(user::setNickname);
        Optional.ofNullable(request.getGender()).ifPresent(user::setGender);
        Optional.ofNullable(request.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(request.getMobile()).ifPresent(user::setMobile);
        return user;
    }

}
