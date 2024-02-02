package com.community.manager.module.system.controller;


import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ResponseBody
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request){
        Map<String, Object> body = Collections.unmodifiableMap(
                getErrorAttributes(request, ErrorAttributeOptions.defaults()));
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(body, status);
    }

}
