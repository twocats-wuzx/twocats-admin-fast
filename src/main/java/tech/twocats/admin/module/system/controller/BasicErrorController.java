package tech.twocats.admin.module.system.controller;


import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping(value = "${sever.error.path:${error.path:/error}}")
public class BasicErrorController extends AbstractErrorController {
    public BasicErrorController(ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    @RequestMapping(produces =  MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response){
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap(
                getErrorAttributes(request, ErrorAttributeOptions.defaults()));
        response.setStatus(status.value());
        if (status.value() == 403){
            return new ModelAndView("view/exception/403", model);
        }
        if (status.value() == 500){
            return new ModelAndView("view/exception/500", model);
        }
        //ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        //return modelAndView == null ? new ModelAndView("/page/component/404", model) : modelAndView;
        return new ModelAndView("view/exception/404", model);
    }

    @ResponseBody
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> body = Collections.unmodifiableMap(
                getErrorAttributes(request, ErrorAttributeOptions.defaults()));
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(body, status);
    }

}
