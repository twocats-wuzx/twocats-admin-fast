package tech.twocats.admin.module.admin.service.impl;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.twocats.admin.common.AppConstant;
import tech.twocats.admin.common.enums.MenuTypeEnum;
import tech.twocats.admin.common.error.SystemError;
import tech.twocats.admin.common.model.vo.LongListWrapper;
import tech.twocats.admin.exception.BaseException;
import tech.twocats.admin.module.system.domain.dto.UserDetailDTO;
import tech.twocats.admin.module.admin.domain.entity.Menu;
import tech.twocats.admin.module.admin.domain.entity.UserRole;
import tech.twocats.admin.module.admin.domain.vo.MenuQuery;
import tech.twocats.admin.module.admin.domain.vo.MenuRequest;
import tech.twocats.admin.module.admin.domain.vo.MenuVO;
import tech.twocats.admin.module.admin.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import tech.twocats.admin.module.admin.service.IMenuService;
import tech.twocats.admin.module.admin.service.IRoleMenuService;
import tech.twocats.admin.module.admin.service.IUserRoleService;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements IMenuService {

    private final IUserRoleService userRoleService;
    private final IRoleMenuService roleMenuService;

    public MenuServiceImpl(IUserRoleService userRoleService,
                           IRoleMenuService roleMenuService) {
        this.userRoleService = userRoleService;
        this.roleMenuService = roleMenuService;
    }

    @Override
    public List<MenuVO> getPermissions() {
        List<Menu> menus = this.lambdaQuery()
                .select(Menu::getId, Menu::getPid, Menu::getTitle, Menu::getStatus, Menu::getSort)
                .orderByAsc(Menu::getPid)
                .orderByAsc(Menu::getSort)
                .list();
        if (CollectionUtils.isEmpty(menus)){
            return new ArrayList<>();
        }
        return toMenuTree(menus);
    }

    @Override
    public List<MenuVO> getMenusByUserId(Long userId) {
        return getUserMenus(userId, MenuTypeEnum.MENU);
    }

    @Override
    public List<MenuVO> getMenus(MenuQuery query) {
        List<Menu> menus = this.lambdaQuery()
                .like(StringUtils.hasLength(query.getTitle()), Menu::getTitle, query.getTitle())
                .orderByAsc(Menu::getPid)
                .orderByAsc(Menu::getSort)
                .list();
        if (CollectionUtils.isEmpty(menus)){
            return new ArrayList<>();
        }
        return toMenuTree(menus);
    }

    @Override
    public List<MenuVO> queryMenuOptions() {
        List<Menu> menus = this.lambdaQuery()
                .select(Menu::getId, Menu::getPid, Menu::getTitle, Menu::getStatus, Menu::getSort)
                .eq(Menu::getType, MenuTypeEnum.MENU)
                .orderByAsc(Menu::getPid)
                .orderByAsc(Menu::getSort)
                .list();
        if (CollectionUtils.isEmpty(menus)){
            return new ArrayList<>();
        }
        return toMenuTree(menus);
    }

    @Override
    public void saveMenu(MenuRequest request) {
        MenuRequest.checkMenu(request);
        // 判断父级菜单是否存在
        if (request.getPid() != null){
            this.lambdaQuery()
                    .eq(Menu::getId, request.getPid())
                    .oneOpt()
                    .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "父级菜单不存在"));
        }
        Menu menu = MenuRequest.toMenu(request);
        this.save(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMenu(@NotNull LongListWrapper ids) {
        // 通过ids删除菜单
        this.removeByIds(ids.getKeys());
    }

    @Override
    public void editMenu(MenuRequest request) {
        MenuRequest.checkMenu(request);
        // 判断菜单是否存在
        this.lambdaQuery()
                .eq(Menu::getId, request.getId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "菜单不存在"));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaUpdateChainWrapper<Menu> updateWrapper = this.lambdaUpdate();
        updateWrapper
                .eq(Menu::getId, request.getId())
                .set(Menu::getPid, request.getPid() == null ? -1 : request.getPid())
                .set(Menu::getTitle, request.getTitle())
                .set(Menu::getSort, request.getSort())
                .set(Menu::getType, request.getType())
                .set(Menu::getStatus, Boolean.TRUE)
                .set(Menu::getUpdateTime, new Date());

        // 菜单和权限设置不同的值
        switch (request.getType()) {
            case MENU:
                updateWrapper
                        .set(Menu::getHref, request.getHref())
                        .set(Menu::getIcon, request.getIcon())
                        .set(Menu::getTarget, request.getTarget() == null ? AppConstant.EMPTY_STRING : request.getTarget() );
                break;
            case PERMISSION:
                updateWrapper
                        .set(Menu::getAuthority, request.getAuthority())
                        .set(Menu::getHref, AppConstant.EMPTY_STRING)
                        .set(Menu::getIcon, AppConstant.EMPTY_STRING)
                        .set(Menu::getTarget, null);
                break;
            default:
                break;
        }

        if (principal instanceof UserDetailDTO){
            UserDetailDTO userDetailDTO = (UserDetailDTO) principal;
            updateWrapper.set(Menu::getUpdateBy, userDetailDTO.getUsername());
        }
        updateWrapper.update();
    }

    @Override
    public void changeMenuStatus(MenuRequest request) {
        // 判断菜单是否存在
        this.lambdaQuery()
                .eq(Menu::getId, request.getId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "菜单不存在"));

        // 更新数据库中菜单的状态
        Menu menu = new Menu();
        menu.setId(request.getId());
        menu.setStatus(request.getStatus());
        this.updateById(menu);
    }

    @Override
    public List<Long> validateMenuIds(List<Long> menuIds) {
        if (CollectionUtils.isEmpty(menuIds)){
            return new ArrayList<>(1);
        }
        return this.lambdaQuery()
                .select(Menu::getId)
                .eq(Menu::getStatus, true)
                .in(Menu::getId, menuIds)
                .list()
                .stream()
                .map(Menu::getId)
                .filter(menuIds::contains)
                .collect(Collectors.toList());
    }

    /**
     * 查询用户有权限的菜单
     * @param userId 用户ID
     * @param menuType 菜单类型, 不传则代表不区分类型
     * @return 菜单列表
     */
    private List<MenuVO> getUserMenus(Long userId, MenuTypeEnum menuType) {
        List<UserRole> userRoles = userRoleService.lambdaQuery()
                .eq(UserRole::getUserId, userId).list();
        if (CollectionUtils.isEmpty(userRoles)){
            return new ArrayList<>(1);
        }
        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<Menu> menus = roleMenuService.getMenusByRoleIds(roleIds, menuType);
        return toMenuTree(menus);
    }

    /**
     * 将菜单列表转换为菜单树
     * @param menus 菜单列表
     * @return 菜单树
     */
    public List<MenuVO> toMenuTree(List<Menu> menus){
        List<Long> ids = menus.stream().map(Menu::getId).collect(Collectors.toList());
        Map<Long, MenuVO> menuVOMap = menus.stream()
                .map(MenuVO::fromMenu).collect(Collectors.toMap(
                        MenuVO::getId,
                        o -> o,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));

        List<MenuVO> menuVOS = new ArrayList<>();
        menuVOMap.forEach((key, value) -> {
            if (ids.contains(value.getPid())){
                MenuVO parentMenuVO = menuVOMap.get(value.getPid());
                Optional.ofNullable(parentMenuVO)
                        .ifPresent(parentMenu -> parentMenuVO.getChild().add(value));
            }else {
                menuVOS.add(value);
            }
        });
        menuVOMap.forEach((key, value) -> {
            if (!value.getStatus()){
                value.setChild(null);
            }
        });

        menuVOS.sort(Comparator.comparingInt(MenuVO::getSort));
        return menuVOS;
    }

}




