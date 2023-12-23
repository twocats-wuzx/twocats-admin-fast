package tech.twocats.admin.module.admin.service;

import tech.twocats.admin.module.admin.domain.entity.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IUserRoleService extends IService<UserRole> {

    /**
     * 保存用户的角色信息
     * @param id 用户ID
     * @param roleIds 角色ID列表
     */
    void saveUserRoles(Long id, List<Long> roleIds);
}
