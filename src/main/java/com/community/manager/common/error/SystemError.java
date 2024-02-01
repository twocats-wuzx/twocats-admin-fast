package com.community.manager.common.error;

/**
 * Created on 2022/4/28
 */
public interface SystemError{

    ErrorType ACCESS_DENIED_ERROR = new Error(40300, "非法操作");
    ErrorType METHOD_NOT_ALLOWED = new Error(40500, "请求方法错误");
    ErrorType INTERNAL_SERVER_ERROR = new Error(50000, "系统繁忙");
    ErrorType INVALID_PARAMETER = new Error(50400, "请求参数错误");
    ErrorType JSON_FORMAT_ERROR = new Error(50401, "请求JSON格式错误");
    ErrorType CALL_API_FIELD = new Error(50200, "接口调用失败");
    ErrorType IMAOTAI_APP_VERSION_ERROR = new Error(50200, "查询i茅台版本失败");
}
