package tech.twocats.admin.module.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import tech.twocats.admin.module.admin.domain.entity.UserRole;
import tech.twocats.admin.module.admin.service.IUserRoleService;
import tech.twocats.admin.module.admin.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements IUserRoleService {

}




