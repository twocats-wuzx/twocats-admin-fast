package tech.twocats.admin.module.user.service;

import tech.twocats.admin.module.user.domain.entity.Menu;
import tech.twocats.admin.module.user.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.twocats.admin.module.user.domain.vo.MenuVO;

import java.util.List;


public interface IUserService extends IService<User> {

    User queryByUsername(String username);

    List<Menu> getPermissionsByUserId(Long userId);

}
