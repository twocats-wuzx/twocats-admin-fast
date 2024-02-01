package com.community.manager.module.admin.domain.vo;

import com.community.manager.common.enums.GenderEnum;
import com.community.manager.common.model.vo.PageQuery;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserQuery extends PageQuery {

    /**
     * 用户名
     */
    private String username;

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
    private GenderEnum gender;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 状态 0.禁用 1.启用
     */
    private Boolean status;

}
