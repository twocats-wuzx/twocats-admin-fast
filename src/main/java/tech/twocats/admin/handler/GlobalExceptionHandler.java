package tech.twocats.admin.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
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
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import tech.twocats.admin.common.error.SystemError;
import tech.twocats.admin.common.model.vo.Result;
import tech.twocats.admin.exception.BaseException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Order
@ControllerAdvice(assignableTypes = {})
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ModelAndView baseExceptionHandler(BaseException e, HttpServletRequest request){
        log.error("error:", e);

        ModelAndView modelAndView = new ModelAndView();
        if (request.getHeader("Accept").contains("application/json")) {
            modelAndView.setView(getMappingJackson2JsonView());
            modelAndView.addObject(Result.fail(e.getErrorType()));
        } else {
            modelAndView.setViewName("view/exception/404");
        }
        return modelAndView;
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

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(Exception e, HttpServletRequest request){
        log.error("error:", e);

        ModelAndView modelAndView = new ModelAndView();
        if (request.getHeader("Accept").contains("application/json")) {
            modelAndView.setView(getMappingJackson2JsonView());
            if (e instanceof HttpRequestMethodNotSupportedException) {
                // http 请求方式错误
                modelAndView.addObject(Result.fail(SystemError.METHOD_NOT_ALLOWED));
            }else if (e instanceof TypeMismatchException) {
                // 参数类型错误
                modelAndView.addObject(Result.fail(SystemError.INVALID_PARAMETER));
            }else if (e instanceof HttpMessageNotReadableException) {
                // json 格式错误
                modelAndView.addObject(Result.fail(SystemError.JSON_FORMAT_ERROR));
            }else if (e instanceof MethodArgumentNotValidException) {
                // 参数校验未通过
                List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
                List<String> msgList = fieldErrors.stream().map(FieldError :: getDefaultMessage).collect(Collectors.toList());
                modelAndView.addObject(Result.fail(SystemError.INVALID_PARAMETER, String.join(",", msgList)));
            }else {
                modelAndView.addObject(Result.fail());
            }
        }else {
            modelAndView.setViewName("view/exception/404");
        }

        return modelAndView;
    }

    @ExceptionHandler(value = {Throwable.class})
    public ModelAndView systemError(Throwable throwable, HttpServletRequest request){
        log.error(throwable.getMessage(), throwable);
        ModelAndView modelAndView = new ModelAndView();
        if (request.getHeader("Accept").contains("application/json")) {
            modelAndView.setView(getMappingJackson2JsonView());
            modelAndView.addObject(Result.fail());
        }else {
            modelAndView.setViewName("view/exception/500");
        }
        return modelAndView;
    }

    public MappingJackson2JsonView getMappingJackson2JsonView(){
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setExtractValueFromSingleKeyModel(true);
        return view;
    }

}


