package com.community.manager.auth;


import com.alibaba.fastjson2.JSON;
import com.community.manager.common.model.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AdminAuthSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * 处理认证成功
     * @param request 请求
     * @param response 响应
     * @param authentication 认证
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        log.info("==> principal={} authentication success", authentication.getName());
        response.reset();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(JSON.toJSONString(Result.ok()));
        response.getWriter().flush();
    }

}
