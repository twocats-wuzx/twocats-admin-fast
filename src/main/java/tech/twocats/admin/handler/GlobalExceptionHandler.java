package tech.twocats.admin.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import tech.twocats.admin.exception.BaseException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception e){
        Map<String, Object> model = new HashMap<>();

        // http 请求方式错误
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return new ModelAndView("view/exception/404", model);
        }

        return new ModelAndView("view/exception/404", model);
    }

    @ExceptionHandler(BaseException.class)
    public ModelAndView baseExceptionHandler(BaseException e){
        log.error("error:", e);
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView("view/exception/404", model);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ModelAndView authenticationE(AuthenticationException authenticationException){
        log.error(authenticationException.getMessage(), authenticationException);
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView("/login", model);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ModelAndView systemError(AccessDeniedException accessDeniedException){
        log.error(accessDeniedException.getMessage(), accessDeniedException);
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView("view/exception/500", model);
    }

    @ExceptionHandler(value = {Throwable.class})
    public ModelAndView systemError(Throwable throwable){
        log.error(throwable.getMessage(), throwable);
        Map<String, Object> model = new HashMap<>();
        return new ModelAndView("view/exception/500", model);
    }

}
