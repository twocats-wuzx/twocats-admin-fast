package com.community.manager.exception;


import com.community.manager.common.error.Error;
import com.community.manager.common.error.ErrorType;
import com.community.manager.common.error.SystemError;
import lombok.Getter;

/**
 * Created on 2022/4/28
 */
@Getter
public class BaseException extends RuntimeException{

    private final ErrorType errorType;

    public BaseException() {
        super();
        errorType = SystemError.INTERNAL_SERVER_ERROR;
    }

    public BaseException(String message) {
        super(message);
        errorType = SystemError.INTERNAL_SERVER_ERROR;
    }

    public BaseException(ErrorType errorType) {
        super(errorType.getMsg());
        this.errorType = errorType;
    }

    public BaseException(ErrorType errorType, String message) {
        super(message);
        this.errorType = new Error(errorType.getCode(), message);
    }

    public BaseException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public BaseException(Throwable cause) {
        super(cause);
        errorType = SystemError.INTERNAL_SERVER_ERROR;
    }

    protected BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        errorType = SystemError.INTERNAL_SERVER_ERROR;
    }

}
