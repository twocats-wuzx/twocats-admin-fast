package tech.twocats.admin.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created on 2022/8/9
 *
 * @author Wuzhengxian
 * @Desc
 */
@Slf4j
@Component
public class AdminAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
            throws IOException, ServletException {
        log.info("==> access denied, message: {}", exception.getMessage());
        response.reset();
        response.sendRedirect("/505");
    }

}
