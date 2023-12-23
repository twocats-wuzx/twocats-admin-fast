package tech.twocats.admin.module.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tech.twocats.admin.common.ValidateGroup;
import tech.twocats.admin.common.model.vo.Result;
import tech.twocats.admin.common.model.vo.LongListWrapper;
import tech.twocats.admin.common.model.vo.LongWrapper;
import tech.twocats.admin.module.admin.domain.dto.UserDetailDTO;
import tech.twocats.admin.module.admin.domain.vo.MenuQuery;
import tech.twocats.admin.module.admin.domain.vo.MenuRequest;
import tech.twocats.admin.module.admin.domain.vo.MenuVO;
import tech.twocats.admin.module.admin.domain.vo.RoleVO;
import tech.twocats.admin.module.admin.service.IMenuService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Controller
public class MenuController {

    private final IMenuService menuService;

    public MenuController(IMenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping("/menu/manager")
    @PreAuthorize("@authCheck.check('menu:list')")
    public String menuManagerView(){
        return "view/admin/menu/menu";
    }


    @ResponseBody
    @PostMapping("/api/menu/save")
    @PreAuthorize("@authCheck.check('menu:add')")
    public Result<Void> saveMenu(@RequestBody
                                     @Validated(value = {ValidateGroup.Add.class})
                                     MenuRequest request){
        menuService.saveMenu(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/menu/delete")
    @PreAuthorize("@authCheck.check('menu:del')")
    public Result<Void> deleteMenu(@NotNull @RequestBody LongListWrapper ids){
        menuService.deleteMenu(ids);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/menu/edit")
    @PreAuthorize("@authCheck.check('menu:edit')")
    public Result<Void> editMenu(@RequestBody
                                     @Validated(value = {ValidateGroup.Edit.class})
                                     MenuRequest request){
        menuService.editMenu(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/menu/change/status")
    @PreAuthorize("@authCheck.check('menu:edit')")
    public Result<Void> changeMenuStatus(@RequestBody
                                             @Validated(value = {ValidateGroup.StatusChange.class})
                                             MenuRequest request){
        menuService.changeMenuStatus(request);
        return Result.ok();
    }

	@ResponseBody
    @PostMapping("/api/menu/list")
    @PreAuthorize("@authCheck.check('menu:list')")
    public Result<List<MenuVO>> getMenuList(@RequestBody MenuQuery query){
        return Result.ok(menuService.getMenus(query));
    }

    @ResponseBody
    @PostMapping("/api/permission/list")
    @PreAuthorize("@authCheck.check('role:add, role:edit')")
    public Result<List<MenuVO>> getAllMenus(@AuthenticationPrincipal UserDetailDTO userDetailDTO){
        return Result.ok(menuService.getPermissionsByUserId(userDetailDTO.getId()));
    }

    @ResponseBody
    @PostMapping("/api/parent/menu")
    @PreAuthorize("@authCheck.check('menu:list')")
    public Result<List<MenuVO>> getMenuParentList(@AuthenticationPrincipal UserDetailDTO userDetailDTO){
        return Result.ok(menuService.getMenusByUserId(userDetailDTO.getId()));
    }

    @ResponseBody
    @PostMapping("/api/menu/detail")
    @PreAuthorize("@authCheck.check('menu:detail')")
    public Result<MenuVO> getMenuDetail(@RequestBody LongWrapper idWrapper){
        return Result.ok(MenuVO.fromMenu(menuService.getById(idWrapper.getKey())));
    }

}
