package tech.twocats.admin.module.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ExceptionController {

    @RequestMapping(value = {"/403"})
    public String error403View() {
        return "view/exception/403";
    }

    @RequestMapping(value = {"/404"})
    public String error404View() {
        return "view/exception/404";
    }

    @RequestMapping(value = {"/500"})
    public String error405View() {
        return "view/exception/500";
    }

}
