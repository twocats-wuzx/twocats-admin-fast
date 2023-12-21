package tech.twocats.admin.module.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import tech.twocats.admin.module.admin.domain.entity.RoleMenu;
import tech.twocats.admin.module.admin.service.IRoleMenuService;
import tech.twocats.admin.module.admin.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;


@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu>
    implements IRoleMenuService {

}




