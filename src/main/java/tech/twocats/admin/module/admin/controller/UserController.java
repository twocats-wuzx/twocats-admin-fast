package tech.twocats.admin.module.admin.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tech.twocats.admin.module.admin.service.IUserService;

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



}
