package com.community.manager.module.system.controller;

import com.community.manager.common.model.vo.Result;
import com.community.manager.module.admin.domain.entity.User;
import com.community.manager.module.admin.domain.vo.UserVO;
import com.community.manager.module.admin.service.IUserService;
import com.community.manager.module.system.domain.dto.UserDetailDTO;
import com.community.manager.module.system.domain.vo.RePasswordRequest;
import com.community.manager.module.system.domain.vo.UserSettingRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserConfigController {

    private final IUserService userService;

    public UserConfigController(IUserService userService) {
        this.userService = userService;
    }


    /**
     * 重置用户密码
     * @param userDetailDTO 授权主体
     * @param request 重置密码参数
     */
    @ResponseBody
    @PostMapping("/admin/user/config/rePassword")
    public Result<Void> reSetUserPassword(@AuthenticationPrincipal UserDetailDTO userDetailDTO,
                                          @RequestBody @Validated RePasswordRequest request){
        request.setUserId(userDetailDTO.getId());
        userService.resetUserPassword(request);
        return Result.ok();
    }

    /**
     * 更新用户信息
     * @param userDetailDTO 授权主体
     * @param request 用户信息参数
     */
    @ResponseBody
    @PostMapping("/admin/user/update/info")
    public Result<Void> updateUserInfo(@AuthenticationPrincipal UserDetailDTO userDetailDTO,
                                          @RequestBody @Validated UserSettingRequest request){
        request.setUserId(userDetailDTO.getId());
        userService.updateUserInfo(request);
        return Result.ok();
    }

    /**
     * 获取用户信息
     * @param userDetailDTO 授权主体
     */
    @ResponseBody
    @PostMapping("/admin/user/info")
    public Result<UserVO> getUserInfo(@AuthenticationPrincipal UserDetailDTO userDetailDTO){
        User user = userService.getById(userDetailDTO.getId());
        Optional.ofNullable(user).orElseThrow(() -> new RuntimeException("请求异常"));
        return Result.ok(UserVO.fromUser(user));
    }
}
