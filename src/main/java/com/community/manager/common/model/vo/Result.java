package com.community.manager.common.model.vo;


import com.community.manager.common.error.Error;
import com.community.manager.common.error.ErrorType;
import com.community.manager.common.error.SystemError;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@Getter
public class Result<T> {

    private static final Error SUCCESS = new Error(0, "请求成功");

    private int code;
    private String msg;
    private final String traceId;
    private T data;

    public Result(){
        this.traceId = getCurrentRequestId();
    }

    public Result(int code, String msg){
        this(code, msg, null);
    }

    public Result(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.traceId = getCurrentRequestId();
        this.data = data;
    }


    public static Result<Void> ok(){
        return ok(SUCCESS, null);
    }

    public static <T> Result<T> ok(T data){
        return ok(SUCCESS, data);
    }

    public static <T> Result<T> ok(ErrorType result, T data){
        return new Result<>(result.getCode(), result.getMsg(), data);
    }

    public static Result<Object> fail(){
        return fail(SystemError.INTERNAL_SERVER_ERROR);
    }

    public static <T> Result<T> fail(ErrorType errorType){
        return fail(errorType, null);
    }

    public static <T> Result<T> fail(ErrorType errorType, String msg){
        return fail(errorType.getCode(), Optional.ofNullable(msg).orElse(errorType.getMsg()));
    }

    public static <T> Result<T> fail(ErrorType errorType, T data){
        return fail(errorType.getCode(), errorType.getMsg(), data);
    }

    public static <T> Result<T> fail(ErrorType errorType, String msg, T data){
        Assert.notNull(errorType, "error type is null");
        return fail(errorType.getCode(), Optional.ofNullable(msg).orElse(errorType.getMsg()), data);
    }

    public static <T> Result<T> fail(int code, String msg){
        return fail(code, msg, null);
    }

    public static <T> Result<T> fail(int code, String msg, T data){
        return new Result<>(code, msg, data);
    }

    private String getCurrentRequestId(){
        return UUID.randomUUID().toString();
    }
}
