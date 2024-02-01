package com.community.manager.module.admin.domain.vo;

import com.community.manager.common.enums.GenderEnum;
import com.community.manager.module.admin.domain.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class UserVO {

    private Long id;

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
     * 头像
     */
    private String avatar;

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

    private List<Long> roleIds;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public static UserVO fromUser(User user){
        if (user == null){
            return null;
        }
        return getUserVOByUser(user);
    }

    public static UserVO fromUser(User user, List<Long> roleIds){
        if (user == null){
            return null;
        }
        UserVO userVO = getUserVOByUser(user);
        userVO.setRoleIds(roleIds);
        return userVO;
    }

    /**
     * 从用户实体中拷贝用户信息
     * @param user 用户实体
     * @return 用户VO
     */
    private static UserVO getUserVOByUser(User user) {
        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUsername(user.getUsername());
        userVO.setRealName(user.getRealName());
        userVO.setNickname(user.getNickname());
        userVO.setAvatar(user.getAvatar());
        userVO.setGender(user.getGender());
        userVO.setEmail(user.getEmail());
        userVO.setMobile(user.getMobile());
        userVO.setStatus(user.getStatus());
        userVO.setCreateTime(user.getCreateTime());
        return userVO;
    }

}
