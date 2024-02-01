package com.community.manager.module.admin.controller;

import com.community.manager.common.ValidateGroup;
import com.community.manager.common.model.vo.CommonPage;
import com.community.manager.common.model.vo.LongListWrapper;
import com.community.manager.common.model.vo.LongWrapper;
import com.community.manager.common.model.vo.Result;
import com.community.manager.module.admin.domain.vo.UserQuery;
import com.community.manager.module.admin.domain.vo.UserRequest;
import com.community.manager.module.admin.domain.vo.UserVO;
import com.community.manager.module.admin.service.IUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.constraints.NotNull;

@Controller
public class UserManagerController {

    private final IUserService userService;

    public UserManagerController(IUserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/admin/user/manager")
    @PreAuthorize("@authCheck.check('user:list')")
    public String userManagerView(){
        return "view/admin/user/user";
    }

    @ResponseBody
    @PostMapping("/api/admin/user/save")
    @PreAuthorize("@authCheck.check('user:add')")
    public Result<Void> saveUser(@RequestBody
                                 @Validated(value = {ValidateGroup.Add.class})
                                 UserRequest request){
        userService.saveUser(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/user/delete")
    @PreAuthorize("@authCheck.check('user:del')")
    public Result<Void> deleteUser(@NotNull @RequestBody LongListWrapper ids){
        userService.deleteUser(ids);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/user/edit")
    @PreAuthorize("@authCheck.check('user:edit')")
    public Result<Void> editUser(@RequestBody
                                 @Validated(value = {ValidateGroup.Edit.class})
                                     UserRequest request){
        userService.editUser(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/user/change/status")
    @PreAuthorize("@authCheck.check('user:edit')")
    public Result<Void> changeUserStatus(@RequestBody
                                         @Validated(value = {ValidateGroup.StatusChange.class})
                                             UserRequest request){
        userService.changeUserStatus(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/user/list")
    @PreAuthorize("@authCheck.check('user:list')")
    public Result<CommonPage<UserVO>> getUserList(@RequestBody UserQuery query){
        return Result.ok(CommonPage.from(userService.getUsers(query), UserVO::fromUser));
    }

    @ResponseBody
    @PostMapping("/api/admin/user/detail")
    @PreAuthorize("@authCheck.check('user:detail')")
    public Result<UserVO> getUserDetail(@RequestBody LongWrapper idWrapper){
        return Result.ok(userService.getUserDetail(idWrapper.getKey()));
    }


}
