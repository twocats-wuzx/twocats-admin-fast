package com.community.manager.common.error;

public interface SecurityError {

    ErrorType AUTH_EXCEPTION = new Error(40100, "认证失败");
    ErrorType ACCOUNT_LOCKED = new Error(40100, "账号已被锁定, 请联系管理员");
    ErrorType BAD_CREDENTIALS = new Error(40100, "用户名或密码错误");
    ErrorType ACCOUNT_EXPIRED = new Error(40100, "账号已过期, 请联系管理员");
    ErrorType ACCOUNT_DISABLED = new Error(40100, "用户已失效, 请联系管理员");
    ErrorType CREDENTIALS_EXPIRED = new Error(40100, "用户凭证已过期, 请重新认证");
    ErrorType UN_AUTHENTICATION = new Error(40300, "用户未认证");
    ErrorType UNAUTHORIZED = new Error(40300, "请求未授权");
    ErrorType UN_ACCESS = new Error(40300, "无资源访问权限");
    ErrorType FORBIDDEN = new Error(40300, "访问被禁止");

}
