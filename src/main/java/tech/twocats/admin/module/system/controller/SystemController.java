package tech.twocats.admin.module.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemController {

    @RequestMapping("/system/manager/role")
    public String roleManagerView(){
        return "view/system/role/role";
    }

    @RequestMapping("/system/manager/user")
    public String userManagerView(){
        return "view/system/user/user";
    }

}
