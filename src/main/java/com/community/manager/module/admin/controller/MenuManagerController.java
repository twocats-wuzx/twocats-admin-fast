package com.community.manager.module.admin.controller;

import com.community.manager.common.ValidateGroup;
import com.community.manager.common.model.vo.LongListWrapper;
import com.community.manager.common.model.vo.LongWrapper;
import com.community.manager.common.model.vo.Result;
import com.community.manager.module.admin.domain.vo.MenuQuery;
import com.community.manager.module.admin.domain.vo.MenuRequest;
import com.community.manager.module.admin.domain.vo.MenuVO;
import com.community.manager.module.admin.service.IMenuService;
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
public class MenuManagerController {

    private final IMenuService menuService;

    public MenuManagerController(IMenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping("/admin/menu/manager")
    @PreAuthorize("@authCheck.check('menu:list')")
    public String menuManagerView(){
        return "view/admin/menu/menu";
    }


    @ResponseBody
    @PostMapping("/api/admin/menu/save")
    @PreAuthorize("@authCheck.check('menu:add')")
    public Result<Void> saveMenu(@RequestBody
                                     @Validated(value = {ValidateGroup.Add.class})
                                 MenuRequest request){
        menuService.saveMenu(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/menu/delete")
    @PreAuthorize("@authCheck.check('menu:del')")
    public Result<Void> deleteMenu(@NotNull @RequestBody LongListWrapper ids){
        menuService.deleteMenu(ids);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/menu/edit")
    @PreAuthorize("@authCheck.check('menu:edit')")
    public Result<Void> editMenu(@RequestBody
                                     @Validated(value = {ValidateGroup.Edit.class})
                                     MenuRequest request){
        menuService.editMenu(request);
        return Result.ok();
    }

    @ResponseBody
    @PostMapping("/api/admin/menu/change/status")
    @PreAuthorize("@authCheck.check('menu:edit')")
    public Result<Void> changeMenuStatus(@RequestBody
                                             @Validated(value = {ValidateGroup.StatusChange.class})
                                             MenuRequest request){
        menuService.changeMenuStatus(request);
        return Result.ok();
    }

	@ResponseBody
    @PostMapping("/api/admin/menu/list")
    @PreAuthorize("@authCheck.check('menu:list')")
    public Result<List<MenuVO>> getMenuList(@RequestBody MenuQuery query){
        return Result.ok(menuService.getMenus(query));
    }

    @ResponseBody
    @PostMapping("/api/admin/permission/list")
    @PreAuthorize("@authCheck.check('role:add', 'role:edit')")
    public Result<List<MenuVO>> getAllMenus(){
        return Result.ok(menuService.getPermissions());
    }

    @ResponseBody
    @PostMapping("/api/admin/parent/menu")
    @PreAuthorize("@authCheck.check('menu:list')")
    public Result<List<MenuVO>> getMenuParentList(){
        return Result.ok(menuService.queryMenuOptions());
    }

    @ResponseBody
    @PostMapping("/api/admin/menu/detail")
    @PreAuthorize("@authCheck.check('menu:detail')")
    public Result<MenuVO> getMenuDetail(@RequestBody LongWrapper idWrapper){
        return Result.ok(MenuVO.fromMenu(menuService.getById(idWrapper.getKey())));
    }

}
