package com.community.manager.module.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.community.manager.common.AppConstant;
import com.community.manager.common.enums.StatusEnum;
import com.community.manager.common.error.SystemError;
import com.community.manager.common.model.vo.LongListWrapper;
import com.community.manager.exception.BaseException;
import com.community.manager.module.admin.domain.entity.Menu;
import com.community.manager.module.admin.domain.entity.Role;
import com.community.manager.module.admin.domain.entity.RoleMenu;
import com.community.manager.module.admin.domain.vo.RoleQuery;
import com.community.manager.module.admin.domain.vo.RoleRequest;
import com.community.manager.module.admin.domain.vo.RoleVO;
import com.community.manager.module.admin.mapper.RoleMapper;
import com.community.manager.module.admin.service.IRoleMenuService;
import com.community.manager.module.admin.service.IRoleService;
import com.community.manager.module.system.domain.dto.UserDetailDTO;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements IRoleService {

    private final IRoleMenuService roleMenuService;

    public RoleServiceImpl(IRoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    /**
     * 获取超级管理员ID
     * @return 超级管理员ID
     */
    @Override
    public Long getSuperAdminId() {
        Role superAdmin = ChainWrappers.lambdaQueryChain(this.baseMapper)
                .eq(Role::getRoleCode, AppConstant.SUPER_ADMIN_ROLE_CODE)
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
                .ne(Role::getRoleCode, AppConstant.SUPER_ADMIN_ROLE_CODE)
                .like(StringUtils.hasLength(query.getRoleName()), Role::getRoleName, query.getRoleName())
                .like(StringUtils.hasLength(query.getRoleCode()), Role::getRoleCode, query.getRoleCode())
                .page(query.getPage());
    }

    /**
     * 获取角色明细信息，包含角色的菜单和权限
     * @param key 角色ID
     * @return 角色明细信息
     */
    @Override
    public RoleVO getRoleDetail(Long key) {
        Role role = this.lambdaQuery()
                .eq(Role::getId, key)
                .one();
        if (role == null || role.getRoleCode().equals(AppConstant.SUPER_ADMIN_ROLE_CODE)){
            throw new BaseException(SystemError.INVALID_PARAMETER, "角色不存在");
        }

        List<Menu> menuList = roleMenuService.getMenusByRoleIds(Collections.singletonList(key), null);
        Map<Long, Menu> menuMap = menuList.stream().collect(Collectors.toMap(Menu::getId, o -> o));
        menuList.forEach(menu -> {
            menuMap.remove(menu.getPid());
        });
        return RoleVO.fromRole(role, new LinkedList<>(menuMap.keySet()));
    }

    /**
     * 新增角色
     * @param request 角色信息
     */
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

        if (Objects.equals(request.getRoleCode(), AppConstant.SUPER_ADMIN_AUTH_CODE)){
            throw new BaseException(SystemError.INVALID_PARAMETER, "不允许添加超级管理员角色");
        }
        Role role = new Role();
        role.setRoleName(request.getRoleName());
        role.setRoleCode(request.getRoleCode());
        role.setRemark(request.getRemark());
        role.setStatus(request.getStatus());
        this.save(role);
        // 保存角色菜单
        roleMenuService.saveRoleMenus(role.getId(), request.getMenuIds());
    }

    /**
     * 删除角色
     * @param ids 角色信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(LongListWrapper ids) {
        ids.getKeys().remove(AppConstant.SUPER_ADMIN_ROLE_ID);
        roleMenuService.lambdaUpdate()
                .in(RoleMenu::getRoleId, ids.getKeys())
                .remove();
        this.removeByIds(ids.getKeys());
    }

    /**
     * 编辑角色
     * @param request 角色信息
     */
    @Override
    public void editRole(RoleRequest request) {

        if (Objects.equals(request.getRoleCode(), AppConstant.SUPER_ADMIN_AUTH_CODE)){
            throw new BaseException(SystemError.INVALID_PARAMETER, "不允许修改超级管理员角色");
        }

        Role role = this.lambdaQuery()
                .eq(Role::getId, request.getId())
                .ne(Role::getRoleCode, AppConstant.SUPER_ADMIN_ROLE_CODE)
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "角色不存在"));

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaUpdateChainWrapper<Role> updateWrapper = this.lambdaUpdate()
                .eq(Role::getId, request.getId())
                .set(Role::getRoleName, request.getRoleName())
                .set(Role::getRoleCode, request.getRoleCode())
                .set(Role::getRemark, request.getRemark())
                .set(Role::getUpdateTime, new Date());

        if (principal instanceof UserDetailDTO){
            UserDetailDTO userDetailDTO = (UserDetailDTO) principal;
            updateWrapper.set(Role::getUpdateBy, userDetailDTO.getUsername());
        }
        updateWrapper.update();
        // 保存角色菜单
        roleMenuService.saveRoleMenus(role.getId(), request.getMenuIds());
    }

    /**
     * 修改角色状态
     * @param request 角色信息
     */
    @Override
    public void changeRoleStatus(RoleRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        LambdaUpdateChainWrapper<Role> updateWrapper = this.lambdaUpdate()
                .eq(Role::getId, request.getId())
                .ne(Role::getRoleCode, AppConstant.SUPER_ADMIN_ROLE_CODE)
                .set(Role::getStatus, request.getStatus())
                .set(Role::getUpdateTime, new Date());
        if (principal instanceof UserDetailDTO){
            UserDetailDTO userDetailDTO = (UserDetailDTO) principal;
            updateWrapper.set(Role::getUpdateBy, userDetailDTO.getUsername());
        }
        this.update(updateWrapper);
    }

    /**
     * 校验角色ID是否存在
     * @param roleIds 角色ID列表
     * @return 角色ID列表
     */
    @Override
    @NonNull
    public List<Long> validateRoleIds(List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)){
            return new ArrayList<>(1);
        }
        roleIds.remove(AppConstant.SUPER_ADMIN_ROLE_ID);
        return this.lambdaQuery()
                .select(Role::getId)
                .in(Role::getId, roleIds)
                .list()
                .stream()
                .map(Role::getId)
                .collect(Collectors.toList());

    }

    @Override
    public List<RoleVO> getRoleTransfer() {
        return this.lambdaQuery()
                .eq(Role::getStatus, StatusEnum.ENABLED.getKey())
                .ne(Role::getRoleCode, AppConstant.SUPER_ADMIN_ROLE_CODE)
                .list()
                .stream()
                .map(role -> {
                    RoleVO roleVO = new RoleVO();
                    roleVO.setId(role.getId());
                    roleVO.setRoleName(role.getRoleName() + " | " + role.getRoleCode());
                    return roleVO;
                }).collect(Collectors.toList());

    }
}




