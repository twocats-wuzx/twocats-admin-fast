package tech.twocats.admin.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created on 2022/8/9
 *
 * @author Wuzhengxian
 */
@Slf4j
@Component
public class AdminAuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        log.info("==> authentication failure, message: {}", exception.getMessage(), exception);
        response.reset();
        response.sendRedirect("/admin/login");
    }

}
