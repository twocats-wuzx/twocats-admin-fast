package com.community.manager.module.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.community.manager.module.admin.domain.entity.UserRole;
import com.community.manager.module.admin.mapper.UserRoleMapper;
import com.community.manager.module.admin.service.IRoleService;
import com.community.manager.module.admin.service.IUserRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements IUserRoleService {

    private final IRoleService roleService;

    public UserRoleServiceImpl(IRoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 保存用户的角色信息
     * @param id 用户ID
     * @param roleIds 角色ID列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserRoles(Long id, List<Long> roleIds) {
        this.removeByUserId(id);
        roleIds = roleService.validateRoleIds(roleIds);

        if (!CollectionUtils.isEmpty(roleIds)){
            List<UserRole> userRoles = roleIds.stream()
                    .map(roleId -> {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(id);
                        userRole.setRoleId(roleId);
                        return userRole;
                    }).collect(Collectors.toList());
            this.saveBatch(userRoles);
        }
    }

    /**
     * 通过用户ID删除用户角色关联信息
     * @param id 用户ID
     */
    private void removeByUserId(Long id) {
        this.lambdaUpdate()
                .eq(UserRole::getUserId, id)
                .remove();
    }
}




