package com.community.manager.module.admin.controller;

import com.community.manager.common.ValidateGroup;
import com.community.manager.common.model.vo.CommonPage;
import com.community.manager.common.model.vo.LongListWrapper;
import com.community.manager.common.model.vo.LongWrapper;
import com.community.manager.common.model.vo.Result;
import com.community.manager.module.admin.domain.vo.RoleQuery;
import com.community.manager.module.admin.domain.vo.RoleRequest;
import com.community.manager.module.admin.domain.vo.RoleVO;
import com.community.manager.module.admin.service.IRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.validation.constraints.NotNull;
import java.util.List;


@Controller
public class RoleManagerController {

    private final IRoleService roleService;

    public RoleManagerController(IRoleService roleService) {
        this.roleService = roleService;
    }


    @RequestMapping("/admin/role/manager")
    @PreAuthorize("@authCheck.check('role:list')")
    public String roleManagerView(){
        return "view/admin/role/role";
    }

    @ResponseBody
    @PostMapping("/api/admin/role/save")
    @PreAuthorize("@authCheck.check('role:add')")
    public Result<Void> saveRole(@RequestBody
                                 @Validated(value = {ValidateGroup.Add.class})
                                 RoleRequest request){
        roleService.saveRole(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/role/delete")
    @PreAuthorize("@authCheck.check('role:del')")
    public Result<Void> deleteRole(@NotNull @RequestBody LongListWrapper ids){
        roleService.deleteRole(ids);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/role/edit")
    @PreAuthorize("@authCheck.check('role:edit')")
    public Result<Void> editRole(@RequestBody
                                 @Validated(value = {ValidateGroup.Edit.class})
                                     RoleRequest request){
        roleService.editRole(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/role/change/status")
    @PreAuthorize("@authCheck.check('role:edit')")
    public Result<Void> changeRoleStatus(@RequestBody
                                         @Validated(value = {ValidateGroup.StatusChange.class})
                                             RoleRequest request){
        roleService.changeRoleStatus(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/role/list")
    @PreAuthorize("@authCheck.check('role:list')")
    public Result<CommonPage<RoleVO>> getRoleList(@RequestBody RoleQuery query){
        return Result.ok(CommonPage.from(roleService.getRoles(query), RoleVO::fromRole));
    }

    @ResponseBody
    @PostMapping("/api/admin/role/detail")
    @PreAuthorize("@authCheck.check('role:detail')")
    public Result<RoleVO> getRoleDetail(@RequestBody LongWrapper idWrapper){
        return Result.ok(roleService.getRoleDetail(idWrapper.getKey()));
    }

    @ResponseBody
    @PostMapping("/api/admin/role/transfer")
    @PreAuthorize("@authCheck.check('user:add', 'user:edit')")
    public Result<List<RoleVO>> getRoleDetail(){
        return Result.ok(roleService.getRoleTransfer());
    }

}
