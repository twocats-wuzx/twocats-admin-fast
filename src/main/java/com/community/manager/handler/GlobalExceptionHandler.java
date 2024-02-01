package com.community.manager.handler;

import com.community.manager.common.error.ErrorType;
import com.community.manager.common.error.SystemError;
import com.community.manager.common.model.vo.Result;
import com.community.manager.exception.BaseException;
import com.community.manager.util.AuthExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Order
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public Result<Object> baseExceptionHandler(BaseException e){
        log.error("error:", e);
        return Result.fail(e.getErrorType());
    }

    /**
     * 认证异常相关异常统一处理
     * @param authenticationException 认证异常
     * @return {@link Result<Object>
     */
    @ExceptionHandler(value = {AuthenticationException.class})
    public Result<Object> authenticationE(AuthenticationException authenticationException){
        log.error(authenticationException.getMessage());
        ErrorType errorType = AuthExceptionUtil.getErrorTypeByException(authenticationException);
        return Result.fail(errorType);
    }

    /**
     * 权限异常处理
     * @param accessDeniedException 权限异常
     * @param request 请求
     * @return {@link Result<Object>}
     */
    @ExceptionHandler(value = {AccessDeniedException.class})
    public Result<Object> systemError(AccessDeniedException accessDeniedException,
                                      HttpServletRequest request){

        log.info(accessDeniedException.getMessage() + " url: " + request.getRequestURI());
        return Result.fail(SystemError.ACCESS_DENIED_ERROR);
    }

    /**
     * 全局异常处理
     * @param e 异常
     * @return {@link Result<Object>}
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> exceptionHandler(Exception e){
        log.error("error:", e);

        if (e instanceof HttpRequestMethodNotSupportedException) {
            // http 请求方式错误
            return Result.fail(SystemError.METHOD_NOT_ALLOWED);
        }else if (e instanceof TypeMismatchException) {
            // 参数类型错误
            return Result.fail(SystemError.INVALID_PARAMETER);
        }else if (e instanceof HttpMessageNotReadableException) {
            // json 格式错误
            return Result.fail(SystemError.JSON_FORMAT_ERROR);
        }else if (e instanceof MethodArgumentNotValidException) {
            // 参数校验未通过
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
            List<String> msgList = fieldErrors.stream().map(FieldError :: getDefaultMessage).collect(Collectors.toList());
            return Result.fail(SystemError.INVALID_PARAMETER, String.join(",", msgList));
        }else {
            return Result.fail();
        }
    }

    /**
     * 系统异常处理
     * @param throwable 异常
     * @return {@link Result<Object>}
     */
    @ExceptionHandler(value = {Throwable.class})
    public Result<Object> systemError(Throwable throwable){
        log.error(throwable.getMessage(), throwable);
        return Result.fail();
    }

}


