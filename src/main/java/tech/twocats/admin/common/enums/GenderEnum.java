package tech.twocats.admin.common.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

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
    ;

    private final String desc;

    GenderEnum(String desc) {
        this.desc = desc;
    }

    @Override
    public String getValue() {
        return name();
    }
}
