package tech.twocats.admin.module.user.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.twocats.admin.common.model.Result;
import tech.twocats.admin.module.user.domain.dto.UserDetailDTO;
import tech.twocats.admin.module.user.domain.vo.MenuVO;
import tech.twocats.admin.module.user.service.IMenuService;

import java.util.List;

@Controller
public class MenuController {

    private final IMenuService menuService;

    public MenuController(IMenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping("/menu/manager")
    public String menuManagerView(){
        return "/view/system/menu/menu";
    }

    @ResponseBody
    @PostMapping("/api/menu/list")
    @PreAuthorize("@authCheck.check('menu:list')")
    public Result<List<MenuVO>> getMenuList(){
        return Result.ok(menuService.getMenus());
    }
}
