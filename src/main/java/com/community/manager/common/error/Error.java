package com.community.manager.common.error;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created on 2022/4/28
 */
public class Error implements ErrorType {

    private static final Map<Integer, ErrorType> errorTypeMap = new ConcurrentHashMap<>();

    private final int code;

    private final String msg;

    public Error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        errorTypeMap.put(code, this);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    // 获取统一管理的Error
    public static ErrorType getError(int code){
        return errorTypeMap.get(code);
    }

    // 获取统一管理的Error
    public static Collection<ErrorType> getAllError(int code){
        return errorTypeMap.values();
    }
}
