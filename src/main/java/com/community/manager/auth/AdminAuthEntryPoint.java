package com.community.manager.auth;

import com.alibaba.fastjson2.JSON;
import com.community.manager.common.error.ErrorType;
import com.community.manager.common.error.SecurityError;
import com.community.manager.common.model.vo.Result;
import com.community.manager.util.AuthExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class AdminAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * 处理认证异常
     * @param request 请求
     * @param response 响应
     * @param exception 异常
     * @throws IOException IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        log.info("==> authentication failure, message: {}", exception.getMessage(), exception);
        ErrorType errorType = AuthExceptionUtil.getErrorTypeByException(exception);
        response.reset();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(JSON.toJSONString(Result.fail(errorType)));
        response.getWriter().flush();
    }

}
