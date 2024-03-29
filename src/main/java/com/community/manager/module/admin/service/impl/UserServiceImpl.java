package com.community.manager.module.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import com.community.manager.common.AppConstant;
import com.community.manager.common.enums.MenuTypeEnum;
import com.community.manager.common.error.SystemError;
import com.community.manager.common.model.vo.LongListWrapper;
import com.community.manager.exception.BaseException;
import com.community.manager.module.admin.domain.entity.Menu;
import com.community.manager.module.admin.domain.entity.RoleMenu;
import com.community.manager.module.admin.domain.entity.User;
import com.community.manager.module.admin.domain.entity.UserRole;
import com.community.manager.module.admin.domain.vo.UserQuery;
import com.community.manager.module.admin.domain.vo.UserRequest;
import com.community.manager.module.admin.domain.vo.UserVO;
import com.community.manager.module.admin.mapper.UserMapper;
import com.community.manager.module.admin.service.IMenuService;
import com.community.manager.module.admin.service.IRoleMenuService;
import com.community.manager.module.admin.service.IUserRoleService;
import com.community.manager.module.admin.service.IUserService;
import com.community.manager.module.system.domain.dto.UserDetailDTO;
import com.community.manager.module.system.domain.vo.RePasswordRequest;
import com.community.manager.module.system.domain.vo.UserSettingRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IUserService {

    private final IMenuService menuService;
    private final PasswordEncoder passwordEncoder;
    private final IUserRoleService userRoleService;
    private final IRoleMenuService roleMenuService;

    public UserServiceImpl(IMenuService menuService,
                           PasswordEncoder passwordEncoder,
                           IUserRoleService userRoleService,
                           IRoleMenuService roleMenuService) {
        this.menuService = menuService;
        this.passwordEncoder = passwordEncoder;
        this.userRoleService = userRoleService;
        this.roleMenuService = roleMenuService;
    }

    /**
     * 通过用户名查询账号信息
     * @param username 用户名
     * @return @link { User }
     */
    @Override
    public User queryByUsername(String username){
        return ChainWrappers.lambdaQueryChain(this.baseMapper)
                .eq(User::getUsername, username)
                .one();
    }

    @Override
    public List<Menu> getPermissionsByUserId(Long userId) {
        List<UserRole> userRoles = ChainWrappers.lambdaQueryChain(userRoleService.getBaseMapper())
                .eq(UserRole::getUserId, userId).list();
        if (CollectionUtils.isEmpty(userRoles)){
            return new ArrayList<>(1);
        }

        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<RoleMenu> roleMenus = ChainWrappers.lambdaQueryChain(roleMenuService.getBaseMapper())
                .in(RoleMenu::getRoleId, roleIds).list();
        if (CollectionUtils.isEmpty(roleMenus)){
            return new ArrayList<>(1);
        }

        List<Long> menuIds = roleMenus.stream().map(RoleMenu::getMenuId).distinct().collect(Collectors.toList());
        return ChainWrappers.lambdaQueryChain(menuService.getBaseMapper())
                .in(Menu::getId, menuIds)
                .eq(Menu::getType, MenuTypeEnum.PERMISSION)
                .list();
    }

    /**
     * 保存用户信息
     * @param request 用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(UserRequest request) {
        this.lambdaQuery()
                .eq(User::getUsername, request.getUsername())
                .oneOpt()
                .ifPresent(user -> {
                    throw new BaseException(SystemError.INVALID_PARAMETER, "用户名已存在");
                });
        String encodePassword = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encodePassword);
        user.setNickname(request.getNickname());
        user.setRealName(request.getRealName());
        user.setGender(request.getGender());
        user.setEmail(request.getEmail());
        user.setMobile(request.getMobile());
        user.setStatus(Boolean.TRUE);
        this.save(user);

        userRoleService.saveUserRoles(user.getId(), request.getRoleIds());
    }

    /**
     * 删除用户信息
     * @param ids 用户ID列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(LongListWrapper ids) {
        userRoleService.lambdaUpdate()
                .in(UserRole::getUserId, ids.getKeys())
                .remove();
        this.removeByIds(ids.getKeys());
    }

    /**
     * 修改用户状态
     * @param request 用户信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editUser(UserRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 通过ID判断用户是否存在，不存在抛出异常
        this.lambdaQuery()
                .eq(User::getId, request.getId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "用户不存在"));
        LambdaUpdateChainWrapper<User> updateWrapper = this.lambdaUpdate()
                .eq(User::getId, request.getId())
                .set(User::getNickname, request.getNickname())
                .set(User::getRealName, request.getRealName())
                .set(User::getGender, request.getGender())
                .set(User::getEmail, request.getEmail())
                .set(User::getMobile, request.getMobile())
                .set(User::getUpdateTime, new Date());

        if (principal instanceof UserDetailDTO){
            UserDetailDTO userDetailDTO = (UserDetailDTO) principal;
            updateWrapper.set(User::getUpdateBy, userDetailDTO.getUsername());
        }
        updateWrapper.update();
        userRoleService.saveUserRoles(request.getId(), request.getRoleIds());
    }

    /**
     * 修改用户状态
     * @param request 用户信息
     */
    @Override
    public void changeUserStatus(UserRequest request) {
        // 通过ID判断用户是否存在，不存在抛出异常
        this.lambdaQuery()
                .eq(User::getId, request.getId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "用户不存在"));
        User user = new User();
        user.setId(request.getId());
        user.setStatus(request.getStatus());
        this.updateById(user);
    }

    /**
     * 获取用户列表
     * @param query 查询字段，包含分页，筛选字段
     * @return 用户列表
     */
    @Override
    public IPage<User> getUsers(UserQuery query) {
        return this.lambdaQuery()
                .like(StringUtils.hasLength(query.getUsername()), User::getUsername, query.getUsername())
                .like(StringUtils.hasLength(query.getNickname()), User::getNickname, query.getNickname())
                .like(StringUtils.hasLength(query.getRealName()), User::getRealName, query.getRealName())
                .like(StringUtils.hasLength(query.getEmail()), User::getEmail, query.getEmail())
                .like(StringUtils.hasLength(query.getMobile()), User::getMobile, query.getMobile())
                .eq(query.getGender() != null, User::getGender, query.getGender())
                .eq(query.getStatus() != null, User::getStatus, query.getStatus())
                .orderByDesc(User::getCreateTime)
                .page(query.getPage());
    }

    /**
     * 获取用户详情
     * @param key 用户ID
     * @return 用户详情
     */
    @Override
    public UserVO getUserDetail(Long key) {
        User user = this.lambdaQuery()
                .eq(User::getId, key)
                .one();
        if (user == null){
            throw new BaseException(SystemError.INVALID_PARAMETER, "用户不存在");
        }
        List<Long> roleIds = userRoleService.lambdaQuery()
                .select(UserRole::getRoleId)
                .eq(UserRole::getUserId, key)
                .list()
                .stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
        roleIds.remove(AppConstant.SUPER_ADMIN_ROLE_ID);
        return UserVO.fromUser(user, roleIds);
    }

    /**
     * 重新设置用户密码
     * @param request 请求类
     */
    @Override
    public void resetUserPassword(RePasswordRequest request) {
        // 判断用户是否存在，不存在抛出异常，存在则修改密码
        User user = this.lambdaQuery()
                .eq(User::getId, request.getUserId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "请求异常"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new BaseException(SystemError.INVALID_PARAMETER, "历史密码错误，如忘记密码请联系管理员!!!");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        this.updateById(user);
    }

    /**
     * 更新用户信息
     * @param request 用户信息参数
     */
    @Override
    public void updateUserInfo(UserSettingRequest request) {
        User user = this.lambdaQuery()
                .eq(User::getId, request.getUserId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "请求异常"));
        user = UserSettingRequest.updateUser(user, request);
        this.updateById(user);
    }

}




