package tech.twocats.admin.module.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import tech.twocats.admin.common.AppConstant;
import tech.twocats.admin.common.enums.MenuTypeEnum;
import tech.twocats.admin.common.error.SystemError;
import tech.twocats.admin.common.model.vo.LongListWrapper;
import tech.twocats.admin.exception.BaseException;
import tech.twocats.admin.module.admin.domain.entity.Menu;
import tech.twocats.admin.module.admin.domain.entity.RoleMenu;
import tech.twocats.admin.module.admin.domain.entity.User;
import tech.twocats.admin.module.admin.domain.entity.UserRole;
import tech.twocats.admin.module.admin.domain.vo.UserQuery;
import tech.twocats.admin.module.admin.domain.vo.UserRequest;
import tech.twocats.admin.module.admin.domain.vo.UserVO;
import tech.twocats.admin.module.admin.mapper.UserMapper;
import tech.twocats.admin.module.admin.service.IMenuService;
import tech.twocats.admin.module.admin.service.IRoleMenuService;
import tech.twocats.admin.module.admin.service.IUserRoleService;
import tech.twocats.admin.module.admin.service.IUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IUserService {

    private final IMenuService menuService;
    private final IUserRoleService userRoleService;
    private final IRoleMenuService roleMenuService;

    public UserServiceImpl(IMenuService menuService,
                           IUserRoleService userRoleService,
                           IRoleMenuService roleMenuService) {
        this.menuService = menuService;
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
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
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
        // 通过ID判断用户是否存在，不存在抛出异常
        this.lambdaQuery()
                .eq(User::getId, request.getId())
                .oneOpt()
                .orElseThrow(() -> new BaseException(SystemError.INVALID_PARAMETER, "用户不存在"));
        this.lambdaUpdate()
                .eq(User::getId, request.getId())
                .set(User::getNickname, request.getNickname())
                .set(User::getRealName, request.getRealName())
                .set(User::getGender, request.getGender())
                .set(User::getEmail, request.getEmail())
                .set(User::getMobile, request.getMobile())
                .update();

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
        this.lambdaUpdate()
                .eq(User::getId, request.getId())
                .set(User::getStatus, request.getStatus())
                .update();
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
        roleIds.remove(AppConstant.SUPER_ADMIN_ID);
        return UserVO.fromUser(user, roleIds);
    }

}




