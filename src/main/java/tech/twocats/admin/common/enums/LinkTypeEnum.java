package tech.twocats.admin.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

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
}
