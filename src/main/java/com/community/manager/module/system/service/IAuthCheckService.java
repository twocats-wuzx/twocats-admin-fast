package com.community.manager.module.system.service;

public interface IAuthCheckService {

    /**
     * 权限校验
     * @param permissions 需要的权限
     */
    boolean check(String ...permissions);

}
