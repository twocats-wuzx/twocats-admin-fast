package tech.twocats.admin.module.user.service;

import tech.twocats.admin.module.user.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.twocats.admin.module.user.domain.vo.MenuQuery;
import tech.twocats.admin.module.user.domain.vo.MenuVO;

import java.util.List;


public interface IMenuService extends IService<Menu> {

    /**
     * 查询用户有权限的菜单
     * @param userId 用户ID
     */
    List<MenuVO> getMenusByUserId(Long userId);

    /**
     * 查询菜单树
     */
    List<MenuVO> getMenus(MenuQuery query);
}
