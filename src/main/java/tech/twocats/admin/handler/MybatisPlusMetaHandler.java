package tech.twocats.admin.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import tech.twocats.admin.module.admin.domain.dto.UserDetailDTO;

@Component
public class MybatisPlusMetaHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailDTO){
            UserDetailDTO userDetailDTO = (UserDetailDTO) principal;
            this.setFieldValByName("createBy", userDetailDTO.getUsername(), metaObject);
            this.setFieldValByName("updateBy", userDetailDTO.getUsername(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailDTO){
            UserDetailDTO userDetailDTO = (UserDetailDTO) principal;
            this.setFieldValByName("updateBy", userDetailDTO.getUsername(), metaObject);
        }
    }

}
