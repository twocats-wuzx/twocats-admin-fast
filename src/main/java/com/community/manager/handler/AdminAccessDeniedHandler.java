package com.community.manager.handler;

import com.alibaba.fastjson2.JSON;
import com.community.manager.common.error.Error;
import com.community.manager.common.error.SecurityError;
import com.community.manager.common.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
public class AdminAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 处理权限鉴别异常
     * @param request 请求
     * @param response 响应
     * @param exception 异常
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
            throws IOException, ServletException {
        log.info("==> access denied, message: {}", exception.getMessage());
        response.reset();
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JSON.toJSONString(Result.fail(SecurityError.UN_ACCESS)));
    }

}
