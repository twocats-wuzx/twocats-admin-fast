package tech.twocats.admin.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import tech.twocats.admin.module.user.domain.entity.RoleMenu;
import tech.twocats.admin.module.user.service.IRoleMenuService;
import tech.twocats.admin.module.user.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;


@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements IRoleMenuService {

}




