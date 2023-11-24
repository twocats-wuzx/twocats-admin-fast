package tech.twocats.admin.module.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;
import tech.twocats.admin.common.AppConstant;
import tech.twocats.admin.module.user.domain.entity.Role;
import tech.twocats.admin.module.user.service.IRoleService;
import tech.twocats.admin.module.user.mapper.RoleMapper;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements IRoleService {

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
}




