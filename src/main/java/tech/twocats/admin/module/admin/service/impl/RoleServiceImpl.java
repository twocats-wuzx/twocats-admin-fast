package tech.twocats.admin.module.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.twocats.admin.common.AppConstant;
import tech.twocats.admin.common.error.SystemError;
import tech.twocats.admin.common.model.vo.LongListWrapper;
import tech.twocats.admin.exception.BaseException;
import tech.twocats.admin.module.admin.domain.entity.Menu;
import tech.twocats.admin.module.admin.domain.entity.Role;
import tech.twocats.admin.module.admin.domain.entity.RoleMenu;
import tech.twocats.admin.module.admin.domain.vo.RoleQuery;
import tech.twocats.admin.module.admin.domain.vo.RoleRequest;
import tech.twocats.admin.module.admin.domain.vo.RoleVO;
import tech.twocats.admin.module.admin.service.IRoleMenuService;
import tech.twocats.admin.module.admin.service.IRoleService;
import tech.twocats.admin.module.admin.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements IRoleService {

    private final IRoleMenuService roleMenuService;

    public RoleServiceImpl(IRoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    @Override
    public Long getSuperAdminId() {
        Role superAdmin = ChainWrappers.lambdaQueryChain(this.baseMapper)
                .eq(Role::getRoleCode, AppConstant.SUPER_ADMIN_CODE)
                .one();
        if (superAdmin != null){
            return superAdmin.getId();
        }
        return -1L;
    }

    /**
     * 获取角色列表
     * @param query 查询字段，包含分页，筛选字段
     * @return 角色列表
     */
    @Override
    public IPage<Role> getRoles(RoleQuery query) {
        return this.lambdaQuery()
                .like(StringUtils.hasLength(query.getRoleName()), Role::getRoleName, query.getRoleName())
                .like(StringUtils.hasLength(query.getRoleCode()), Role::getRoleCode, query.getRoleCode())
                .page(query.getPage());
    }

    @Override
    public RoleVO getRoleDetail(Long key) {
        Role role = this.lambdaQuery()
                .eq(Role::getId, key)
                .one();
        if (role == null){
            throw new BaseException(SystemError.INVALID_PARAMETER, "角色不存在");
        }
        List<RoleMenu> roleMenus = roleMenuService
                .lambdaQuery()
                .select(RoleMenu::getMenuId)
                .eq(RoleMenu::getRoleId, key)
                .list();

        List<Menu> menuList = roleMenuService.getMenusByRoleIds(Collections.singletonList(key), null);
        Map<Long, Menu> menuMap = menuList.stream().collect(Collectors.toMap(Menu::getId, o -> o));
        menuList.forEach(menu -> {
            menuMap.remove(menu.getPid());
        });
        return RoleVO.fromRole(role, new LinkedList<>(menuMap.keySet()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(RoleRequest request) {
        this.lambdaQuery()
                .eq(Role::getRoleName, request.getRoleName())
                .or()
                .eq(Role::getRoleCode, request.getRoleCode())
                .oneOpt()
                .ifPresent(role -> {
                    throw new BaseException(SystemError.INVALID_PARAMETER, "角色名称或角色编码已存在");
                });

        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setRoleCode(request.getRoleCode());
        role.setRemark(request.getRemark());
        role.setStatus(request.getStatus());
        this.save(role);
        // 保存角色菜单
        roleMenuService.saveRoleMenus(role.getId(), request.getMenus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(LongListWrapper ids) {
        roleMenuService.lambdaUpdate()
                .in(RoleMenu::getRoleId, ids.getKeys())
                .remove();
        this.removeByIds(ids.getKeys());
    }

    @Override
    public void editRole(RoleRequest request) {
        Role role = this.lambdaQuery()
                .eq(Role::getId, request.getId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "角色不存在"));

        this.lambdaUpdate()
                        .eq(Role::getId, request.getId())
                        .set(Role::getRoleName, request.getRoleName())
                        .set(Role::getRoleCode, request.getRoleCode())
                        .set(Role::getRemark, request.getRemark())
                        .update();
        this.updateById(role);
        // 保存角色菜单
        roleMenuService.saveRoleMenus(role.getId(), request.getMenus());
    }

    @Override
    public void changeMenuStatus(RoleRequest request) {
        this.lambdaUpdate()
                .eq(Role::getId, request.getId())
                .set(Role::getStatus, request.getStatus())
                .update();
    }
}




