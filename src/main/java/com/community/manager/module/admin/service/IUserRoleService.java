package com.community.manager.module.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.community.manager.module.admin.domain.entity.UserRole;

import java.util.List;


public interface IUserRoleService extends IService<UserRole> {

    /**
     * 保存用户的角色信息
     * @param id 用户ID
     * @param roleIds 角色ID列表
     */
    void saveUserRoles(Long id, List<Long> roleIds);
}
