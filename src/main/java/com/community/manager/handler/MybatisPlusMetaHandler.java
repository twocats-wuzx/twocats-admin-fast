package com.community.manager.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.community.manager.module.system.domain.dto.UserDetailDTO;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailDTO){
            UserDetailDTO userDetailDTO = (UserDetailDTO) principal;
            this.setFieldValByName("updateBy", userDetailDTO.getUsername(), metaObject);
        }
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

}
