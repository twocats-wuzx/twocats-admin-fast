package tech.twocats.admin.module.user.service;

import tech.twocats.admin.module.user.domain.entity.Menu;
import tech.twocats.admin.module.user.domain.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.twocats.admin.module.user.domain.vo.MenuVO;

import java.util.List;


public interface IRoleService extends IService<Role> {

    Long getSuperAdminId();

}
