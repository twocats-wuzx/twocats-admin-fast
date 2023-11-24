package tech.twocats.admin.module.component.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/component")
public class ComponentController {

    @RequestMapping("/area")
    @PreAuthorize("@authCheck.check('area:component')")
    public String areaView(){
        return "/view/component/area";
    }

    @RequestMapping("/button")
    @PreAuthorize("@authCheck.check('button:component')")
    public String buttonView(){
        return "/view/component/button";
    }

    @RequestMapping("/color-select")
    @PreAuthorize("@authCheck.check('color-select:component')")
    public String colorSelectView(){
        return "/view/component/color-select";
    }

    @RequestMapping("/editor")
    @PreAuthorize("@authCheck.check('editor:component')")
    public String editorView(){
        return "/view/component/editor";
    }

    @RequestMapping("/form")
    @PreAuthorize("@authCheck.check('form:component')")
    public String formView(){
        return "/view/component/form";
    }

    @RequestMapping("/form-step")
    @PreAuthorize("@authCheck.check('form-step:component')")
    public String formStepView(){
        return "/view/component/form-step";
    }

    @RequestMapping("/icon")
    @PreAuthorize("@authCheck.check('icon:component')")
    public String iconView(){
        return "/view/component/icon";
    }

    @RequestMapping("/icon-picker")
    @PreAuthorize("@authCheck.check('icon-picker:component')")
    public String iconPickerView(){
        return "/view/component/icon-picker";
    }

    @RequestMapping("/layer")
    @PreAuthorize("@authCheck.check('layer:component')")
    public String layerView(){
        return "/view/component/layer";
    }

    @RequestMapping("/table")
    @PreAuthorize("@authCheck.check('table:component')")
    public String tableView(){
        return "/view/component/table";
    }

    @RequestMapping("/table-select")
    @PreAuthorize("@authCheck.check('table-select:component')")
    public String tableSelectView(){
        return "/view/component/table-select";
    }

    @RequestMapping("/upload")
    @PreAuthorize("@authCheck.check('upload:component')")
    public String uploadView(){
        return "/view/component/upload";
    }

}
