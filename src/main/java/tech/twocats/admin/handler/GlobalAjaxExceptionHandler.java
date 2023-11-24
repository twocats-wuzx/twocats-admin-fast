package tech.twocats.admin.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.twocats.admin.common.error.SystemError;
import tech.twocats.admin.common.model.Result;
import tech.twocats.admin.exception.BaseException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Order
@RestControllerAdvice
public class GlobalAjaxExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<Object> exceptionHandler(Exception e){
        log.error("error:", e);

        // http 请求方式错误
        if (e instanceof HttpRequestMethodNotSupportedException) {
            return Result.fail(SystemError.METHOD_NOT_ALLOWED);
        }

        // 参数类型错误
        if (e instanceof TypeMismatchException) {
            return Result.fail(SystemError.INVALID_PARAMETER);
        }

        // json 格式错误
        if (e instanceof HttpMessageNotReadableException) {
            return Result.fail(SystemError.JSON_FORMAT_ERROR);
        }

        // 参数校验未通过
        if (e instanceof MethodArgumentNotValidException) {
            List<FieldError> fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
            List<String> msgList = fieldErrors.stream().map(FieldError :: getDefaultMessage).collect(Collectors.toList());
            return Result.fail(SystemError.INVALID_PARAMETER, String.join(",", msgList));
        }
        return Result.fail();
    }

    @ExceptionHandler(BaseException.class)
    public Result<Object> baseExceptionHandler(BaseException e){
        log.error("error:", e);
        return Result.fail(e.getErrorType());
    }

    @ExceptionHandler(value = {Throwable.class})
    public Result<Object> systemError(Throwable throwable){
        log.error(throwable.getMessage(), throwable);
        return Result.fail();
    }

}


