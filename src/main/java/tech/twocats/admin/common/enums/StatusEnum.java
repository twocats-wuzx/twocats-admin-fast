package tech.twocats.admin.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum StatusEnum implements IEnum<String>{
    ENABLED(1, "启用"),
    DISABLED(0, "禁用"),
    ;

    StatusEnum(Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    private final Integer key;
    private final String desc;

    public static StatusEnum getStatusEnumByValue(String value){
        if (value == null || value.trim().isEmpty()){
            return null;
        }
        for (StatusEnum statusEnum : values()) {
            if (Objects.equals(statusEnum.getValue(), value)){
                return statusEnum;
            }
        }
        return null;
    }

    @Override
    public String getValue() {
        return name();
    }
}
