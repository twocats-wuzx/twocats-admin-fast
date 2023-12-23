package tech.twocats.admin.module.admin.domain.vo;

import lombok.Getter;
import lombok.Setter;
import tech.twocats.admin.common.enums.GenderEnum;
import tech.twocats.admin.common.model.vo.PageQuery;

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
