package tech.twocats.admin.common.error;

/**
 * Created on 2022/4/28
 */
public interface SystemError{

    // 错误码, 错误提示
    ErrorType BAD_REQUEST = new Error(40000, "客户端错误");
    ErrorType NOT_FOUND = new Error(40400, "未找到页面");

    ErrorType METHOD_NOT_ALLOWED = new Error(40500, "请求方法错误");
    ErrorType HTTP_VERSION_NOT_SUPPORTED = new Error(40501, "HTTP请求版本不支持");
    ErrorType REQUEST_TIMEOUT = new Error(40502, "请求超时");



    ErrorType INTERNAL_SERVER_ERROR = new Error(50000, "系统繁忙");

    ErrorType BAD_GATEWAY = new Error(50200, "网关错误");
    ErrorType GATEWAY_TIMEOUT = new Error(50201, "网关超时");

    ErrorType SERVICE_UNAVAILABLE = new Error(50300, "服务不可用");

    ErrorType INVALID_PARAMETER = new Error(50400, "请求参数错误");
    ErrorType JSON_FORMAT_ERROR = new Error(50401, "请求JSON格式错误");
    ErrorType USER_ALREADY_EXISTS = new Error(50402, "用户已存在");
    ErrorType USER_ALREADY_NOT_EXISTS = new Error(50403, "用户不存在");

}
