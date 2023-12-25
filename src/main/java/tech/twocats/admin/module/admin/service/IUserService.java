package tech.twocats.admin.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import tech.twocats.admin.common.model.vo.LongListWrapper;
import tech.twocats.admin.module.admin.domain.entity.Menu;
import tech.twocats.admin.module.admin.domain.entity.User;
import tech.twocats.admin.module.admin.domain.vo.UserQuery;
import tech.twocats.admin.module.admin.domain.vo.UserRequest;
import tech.twocats.admin.module.admin.domain.vo.UserVO;
import tech.twocats.admin.module.system.domain.vo.RePasswordRequest;
import tech.twocats.admin.module.system.domain.vo.UserSettingRequest;

import java.util.List;


public interface IUserService extends IService<User> {

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User queryByUsername(String username);

    /**
     * 根据用户ID获取用户权限
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Menu> getPermissionsByUserId(Long userId);

    /**
     * 保存用户信息
     * @param request 用户信息
     */
    void saveUser(UserRequest request);

    /**
     * 删除用户信息
     * @param ids 用户ID列表
     */
    void deleteUser(LongListWrapper ids);

    /**
     * 修改用户状态
     * @param request 用户信息
     */
    void editUser(UserRequest request);

    /**
     * 修改用户状态
     * @param request 用户信息
     */
    void changeUserStatus(UserRequest request);

    /**
     * 获取用户列表
     * @param query 查询字段，包含分页，筛选字段
     * @return 用户列表
     */
    IPage<User> getUsers(UserQuery query);

    /**
     * 获取用户详情
     * @param key 用户ID
     * @return 用户详情
     */
    UserVO getUserDetail(Long key);

    /**
     * 重新设置用户密码
     * @param request 请求类
     */
    void resetUserPassword(RePasswordRequest request);

    /**
     * 重新设置用户密码
     * @param request 请求类
     */
    void updateUserInfo(UserSettingRequest request);
}
