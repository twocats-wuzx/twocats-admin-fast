package com.community.manager.util;

import com.community.manager.common.error.ErrorType;
import com.community.manager.common.error.SecurityError;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthExceptionUtil {

    /**
     * 根据异常获取错误类型
     * @param exception 异常
     * @param <T> 异常类型
     * @return {@link ErrorType}
     */
    public static <T extends AuthenticationException> ErrorType getErrorTypeByException(T exception){
        ErrorType errorType = SecurityError.AUTH_EXCEPTION;
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException){
            errorType = SecurityError.BAD_CREDENTIALS;
        }
        if (exception instanceof LockedException){
            errorType = SecurityError.ACCOUNT_LOCKED;
        }
        if (exception instanceof AccountExpiredException){
            errorType = SecurityError.ACCOUNT_EXPIRED;
        }
        if (exception instanceof CredentialsExpiredException){
            errorType = SecurityError.CREDENTIALS_EXPIRED;
        }
        if (exception instanceof DisabledException){
            errorType = SecurityError.ACCOUNT_DISABLED;
        }
        return errorType;
    }

}
