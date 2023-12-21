package tech.twocats.admin.module.admin.service;

import tech.twocats.admin.module.admin.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IRoleService extends IService<Role> {

    Long getSuperAdminId();

}
