package com.community.manager.common.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
public enum GenderEnum implements IEnum<String> {

    /**
     * 男
     */
    MALE("男"),

    /**
     * 女
     */
    FEMALE("女"),

    /**
     * 保密
     */
    SECRECY("保密"),
    ;

    private final String desc;

    GenderEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static GenderEnum fromValue(String value) {
        if (!StringUtils.hasLength(value)){
            return null;
        }
        for (GenderEnum e : values()) {
            if (Objects.equals(e.getValue(), value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
