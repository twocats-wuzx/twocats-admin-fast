package tech.twocats.admin.module.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.lang.NonNull;
import tech.twocats.admin.common.enums.MenuTypeEnum;
import tech.twocats.admin.module.admin.domain.entity.Menu;
import tech.twocats.admin.module.admin.domain.entity.RoleMenu;
import tech.twocats.admin.module.admin.domain.vo.RoleVO;

import java.util.List;


public interface IRoleMenuService extends IService<RoleMenu> {

    /**
     * 通过角色ID查询所有的菜单
     * @param roleIds 角色ID列表
     * @param menuTypeEnum 菜单类型
     * @return 菜单列表
     */
    @NonNull
    List<Menu> getMenusByRoleIds(@NonNull List<Long> roleIds, MenuTypeEnum menuTypeEnum);

    /**
     * 保存角色的菜单信息
     * @param id 角色ID
     * @param menuIds 菜单ID列表
     */
    void saveRoleMenus(Long id, List<Long> menuIds);
}
