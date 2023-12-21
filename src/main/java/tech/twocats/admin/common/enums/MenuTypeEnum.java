package tech.twocats.admin.common.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

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


}
