package tech.twocats.admin.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import tech.twocats.admin.module.user.domain.entity.UserRole;
import tech.twocats.admin.module.user.service.IUserRoleService;
import tech.twocats.admin.module.user.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements IUserRoleService {

}




