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
import tech.twocats.admin.module.admin.domain.vo.RoleQuery;
import tech.twocats.admin.module.admin.domain.vo.RoleRequest;
import tech.twocats.admin.module.admin.domain.vo.RoleVO;
import tech.twocats.admin.module.admin.service.IRoleService;

import javax.validation.constraints.NotNull;
import java.util.List;


@Controller
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }


    @RequestMapping("/role/manager")
    @PreAuthorize("@authCheck.check('role:list')")
    public String roleManagerView(){
        return "view/admin/role/role";
    }

    @ResponseBody
    @PostMapping("/api/role/save")
    @PreAuthorize("@authCheck.check('role:add')")
    public Result<Void> saveRole(@RequestBody
                                 @Validated(value = {ValidateGroup.Add.class})
                                 RoleRequest request){
        roleService.saveRole(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/role/delete")
    @PreAuthorize("@authCheck.check('role:del')")
    public Result<Void> deleteRole(@NotNull @RequestBody LongListWrapper ids){
        roleService.deleteRole(ids);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/role/edit")
    @PreAuthorize("@authCheck.check('role:edit')")
    public Result<Void> editRole(@RequestBody
                                 @Validated(value = {ValidateGroup.Edit.class})
                                     RoleRequest request){
        roleService.editRole(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/role/change/status")
    @PreAuthorize("@authCheck.check('role:edit')")
    public Result<Void> changeRoleStatus(@RequestBody
                                         @Validated(value = {ValidateGroup.StatusChange.class})
                                             RoleRequest request){
        roleService.changeRoleStatus(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/role/list")
    @PreAuthorize("@authCheck.check('role:list')")
    public Result<CommonPage<RoleVO>> getRoleList(@RequestBody RoleQuery query){
        return Result.ok(CommonPage.from(roleService.getRoles(query), RoleVO::fromRole));
    }

    @ResponseBody
    @PostMapping("/api/role/detail")
    @PreAuthorize("@authCheck.check('role:detail')")
    public Result<RoleVO> getRoleDetail(@RequestBody LongWrapper idWrapper){
        return Result.ok(roleService.getRoleDetail(idWrapper.getKey()));
    }

    @ResponseBody
    @PostMapping("/api/role/transfer")
    @PreAuthorize("@authCheck.check('user:add, user:edit')")
    public Result<List<RoleVO>> getRoleDetail(){
        return Result.ok(roleService.getRoleTransfer());
    }

}
