package tech.twocats.admin.module.user.domain.vo;

import lombok.Getter;
import lombok.Setter;
import tech.twocats.admin.module.user.domain.entity.Menu;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuVO {

    /**
     * id
     */
    private Long id;

    /**
     * pid
     */
    private Long pid;

    /**
     * 标题
     */
    private String title;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单顺序
     */
    private Integer sort;

    /**
     * 路由地址
     */
    private String href;

    /**
     * 链接打开方式 SELF.当前页面 BLANK.新页面
     */
    private String target;

    /**
     * 类型: MENU.菜单 PERMISSION.权限
     */
    private String type;

    /**
     * 授权标识
     */
    private String authority;

    /**
     * 子菜单
     */
    private List<MenuVO> child;

    public MenuVO(Menu menu){
        this.setId(menu.getId());
        this.setPid(menu.getPid());
        this.setTitle(menu.getTitle());
        this.setIcon(menu.getIcon());
        this.setSort(menu.getSort());
        this.setHref(menu.getHref());
        this.setAuthority(menu.getAuthority());
        this.setType(menu.getType().name());
        this.setTarget(menu.getTarget().getCode());
        this.child = new ArrayList<>();
    }

}
