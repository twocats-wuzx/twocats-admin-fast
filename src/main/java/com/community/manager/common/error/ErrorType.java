package com.community.manager.common.error;

/**
 * Created on 2022/4/28
 */
public interface ErrorType {

    /**
     * 响应码
     * @return  int
     */
    int getCode();

    /**
     * 响应消息
     * @return  string
     */
    String getMsg();

}
