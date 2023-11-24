package tech.twocats.admin.module.user.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.twocats.admin.common.enums.LinkTypeEnum;
import tech.twocats.admin.common.enums.MenuTypeEnum;


@Data
@TableName(value ="t_menu")
@EqualsAndHashCode(callSuper = true)
public class Menu extends BaseEntity implements Serializable {


    /**
     * PID
     */
    @TableField(value = "pid")
    private Long pid;

    /**
     * 标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 图标
     */
    @TableField(value = "icon")
    private String icon;

    /**
     * 路由地址
     */
    @TableField(value = "href")
    private String href;

    /**
     * 链接打开方式 SELF.当前页面 BLANK.新页面
     */
    @TableField(value = "target")
    private LinkTypeEnum target;

    /**
     * 类型: MENU.菜单 PERMISSION.权限
     */
    @TableField(value = "type")
    private MenuTypeEnum type;

    /**
     * 授权标识
     */
    @TableField(value = "authority")
    private String authority;

    /**
     * 菜单顺序
     */
    @TableField(value = "sort")
    private Integer sort;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}