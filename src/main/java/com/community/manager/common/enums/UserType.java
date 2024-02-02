package com.community.manager.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Objects;

public enum UserType implements IEnum<Integer> {

    /**
     * 普通用户
     */
    NORMAL(1, "normal"),
    /**
     * 管理员
     */
    ADMIN(2, "admin"),
    ;

    @EnumValue
    private final Integer code;

    @Getter
    private final String redisKey;

    UserType(Integer code, String redisKey) {
        this.code = code;
        this.redisKey = redisKey;
    }

    @Override
    public Integer getValue() {
        return code;
    }

    @JsonCreator
    public static UserType fromValue(String value) {
        if (!StringUtils.hasLength(value)){
            return null;
        }
        for (UserType e : values()) {
            if (Objects.equals(e.name(), value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }


}
