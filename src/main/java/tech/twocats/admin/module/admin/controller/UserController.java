package tech.twocats.admin.module.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.twocats.admin.common.ValidateGroup;
import tech.twocats.admin.common.model.vo.CommonPage;
import tech.twocats.admin.common.model.vo.LongListWrapper;
import tech.twocats.admin.common.model.vo.LongWrapper;
import tech.twocats.admin.common.model.vo.Result;
import tech.twocats.admin.module.admin.domain.vo.UserQuery;
import tech.twocats.admin.module.admin.domain.vo.UserRequest;
import tech.twocats.admin.module.admin.domain.vo.UserVO;
import tech.twocats.admin.module.admin.service.IUserService;

import javax.validation.constraints.NotNull;

@Controller
public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/user/manager")
    @PreAuthorize("@authCheck.check('user:list')")
    public String userManagerView(){
        return "view/admin/user/user";
    }

    @ResponseBody
    @PostMapping("/api/user/save")
    @PreAuthorize("@authCheck.check('user:add')")
    public Result<Void> saveUser(@RequestBody
                                 @Validated(value = {ValidateGroup.Add.class})
                                 UserRequest request){
        userService.saveUser(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/user/delete")
    @PreAuthorize("@authCheck.check('user:del')")
    public Result<Void> deleteUser(@NotNull @RequestBody LongListWrapper ids){
        userService.deleteUser(ids);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/user/edit")
    @PreAuthorize("@authCheck.check('user:edit')")
    public Result<Void> editUser(@RequestBody
                                 @Validated(value = {ValidateGroup.Edit.class})
                                     UserRequest request){
        userService.editUser(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/user/change/status")
    @PreAuthorize("@authCheck.check('user:edit')")
    public Result<Void> changeUserStatus(@RequestBody
                                         @Validated(value = {ValidateGroup.StatusChange.class})
                                             UserRequest request){
        userService.changeUserStatus(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/user/list")
    @PreAuthorize("@authCheck.check('user:list')")
    public Result<CommonPage<UserVO>> getUserList(@RequestBody UserQuery query){
        return Result.ok(CommonPage.from(userService.getUsers(query), UserVO::fromUser));
    }

    @ResponseBody
    @PostMapping("/api/user/detail")
    @PreAuthorize("@authCheck.check('user:detail')")
    public Result<UserVO> getUserDetail(@RequestBody LongWrapper idWrapper){
        return Result.ok(userService.getUserDetail(idWrapper.getKey()));
    }


}
