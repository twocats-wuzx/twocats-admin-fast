package tech.twocats.admin.module.system.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @RequestMapping("/user/user-setting")
    @PreAuthorize("@authCheck.check('user:edit')")
    public String userSettingView(){
        return "view/user/user-setting";
    }

    @RequestMapping("/user/user-password")
    @PreAuthorize("@authCheck.check('user:repassword')")
    public String userPasswordView(){
        return "view/user/user-password";
    }

}
