package tech.twocats.admin.module.admin.service;

import org.springframework.lang.NonNull;
import tech.twocats.admin.common.model.vo.LongListWrapper;
import tech.twocats.admin.module.admin.domain.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.twocats.admin.module.admin.domain.vo.MenuQuery;
import tech.twocats.admin.module.admin.domain.vo.MenuRequest;
import tech.twocats.admin.module.admin.domain.vo.MenuVO;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface IMenuService extends IService<Menu> {

    /**
     * 查询用户有权限的菜单
     * @param userId 用户ID
     */
    List<MenuVO> getMenusByUserId(Long userId);

    /**
     * 获取用户所有的权限
     * @return 权限列表
     */
    List<MenuVO> getPermissions();


    /**
     * 查询菜单树
     */
    List<MenuVO> getMenus(MenuQuery query);

    /**
     * 查询选择框
     */
    List<MenuVO> queryMenuOptions();

    /**
     * 保存菜单
     * @param request 菜单
     */
    void saveMenu(MenuRequest request);

    /**
     * 删除菜单
     * @param ids 菜单ID列表
     */
    void deleteMenu(@NotNull LongListWrapper ids);

    /**
     * 编辑菜单
     * @param request 菜单
     */
    void editMenu(MenuRequest request);

    /**
     * 变更菜单状态
     * @param request 菜单
     */
    void changeMenuStatus(MenuRequest request);

    /**
     * 校验菜单ID是否存在
     * @param menuIds 菜单ID列表
     * @return 校验之后的菜单ID列表
     */
    @NonNull
    List<Long> validateMenuIds(List<Long> menuIds);
}
