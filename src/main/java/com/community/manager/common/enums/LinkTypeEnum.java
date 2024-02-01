package com.community.manager.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import org.springframework.util.StringUtils;

@Getter
public enum LinkTypeEnum  implements IEnum<String> {

    /**
     * 当前页面
     */
    SELF("_self", "当前页面"),

    /**
     * 新页面
     */
    BLANK("_blank", "新页面"),
    ;

    @JsonValue
    private final String code;

    private final String desc;

    LinkTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return name();
    }

    @JsonCreator
    public static LinkTypeEnum fromString(String code) {
        if (!StringUtils.hasLength(code)){
            return null;
        }
        for (LinkTypeEnum e : values()) {
            if (e.code.equals(code)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + code);
    }
}
