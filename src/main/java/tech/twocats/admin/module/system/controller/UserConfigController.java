package tech.twocats.admin.module.system.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.twocats.admin.common.model.vo.Result;
import tech.twocats.admin.module.admin.domain.entity.User;
import tech.twocats.admin.module.admin.domain.vo.UserVO;
import tech.twocats.admin.module.admin.service.IUserService;
import tech.twocats.admin.module.system.domain.dto.UserDetailDTO;
import tech.twocats.admin.module.system.domain.vo.RePasswordRequest;
import tech.twocats.admin.module.system.domain.vo.UserSettingRequest;

import java.util.Optional;

@Controller
public class UserConfigController {

    private final IUserService userService;

    public UserConfigController(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/admin/user/setting")
    public String userSettingView(){
        return "view/system/setting";
    }

    @RequestMapping("/admin/user/rePassword")
    public String userRePasswordView(){
        return "view/system/re-password";
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
