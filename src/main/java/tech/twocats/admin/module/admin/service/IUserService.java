package tech.twocats.admin.module.admin.service;

import tech.twocats.admin.module.admin.domain.entity.Menu;
import tech.twocats.admin.module.admin.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


public interface IUserService extends IService<User> {

    User queryByUsername(String username);

    List<Menu> getPermissionsByUserId(Long userId);

}
