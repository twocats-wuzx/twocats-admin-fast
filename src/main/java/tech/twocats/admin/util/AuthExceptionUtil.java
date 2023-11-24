package tech.twocats.admin.util;

import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tech.twocats.admin.common.error.ErrorType;
import tech.twocats.admin.common.error.SecurityError;

public class AuthExceptionUtil {

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
