package com.community.manager.common;

public interface AppConstant {

    /**
     * 系统自带的超管账号ID
     */
    long DEFAULT_SUPER_ADMIN_ID = 1L;

    /**
     * 系统自带的超管账号权限码
     */
    String SUPER_ADMIN_AUTH_CODE = "SUPER_ADMIN";

    /**
     * 超管角色ID
     */
    long SUPER_ADMIN_ROLE_ID = 1L;

    /**
     * 超管角色编码
     */
    String SUPER_ADMIN_ROLE_CODE = "super_admin";

    /**
     * 空字符串
     */
    String EMPTY_STRING = "";

    /**
     * 冒号
     */
    String COLON = ":";

    /**
     * 请求携带的Key
     */
    String REQUEST_AUTH_KEY = "token";
}
