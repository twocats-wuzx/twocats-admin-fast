package com.community.manager.common.enums;

 import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Getter
public enum MenuTypeEnum implements IEnum<String>{

    /**
     * 当前页面
     */
    MENU("菜单"),

    /**
     * 新页面
     */
    PERMISSION("权限"),
    ;

    private final String desc;

    MenuTypeEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static MenuTypeEnum fromValue(String value) {
        if (!StringUtils.hasLength(value)){
            return null;
        }
        for (MenuTypeEnum e : values()) {
            if (Objects.equals(e.getValue(), value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }

}
