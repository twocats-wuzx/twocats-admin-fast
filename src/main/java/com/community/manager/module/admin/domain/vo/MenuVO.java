package com.community.manager.module.admin.domain.vo;

import com.community.manager.common.enums.LinkTypeEnum;
import com.community.manager.common.enums.MenuTypeEnum;
import com.community.manager.module.admin.domain.entity.Menu;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
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
    private LinkTypeEnum target;

    /**
     * 类型: MENU.菜单 PERMISSION.权限
     */
    private MenuTypeEnum type;

    /**
     * 状态
     */
    private Boolean status;

    /**
     * 授权标识
     */
    private String authority;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 子菜单
     */
    private List<MenuVO> child;

    public MenuVO() {
    }


    public static MenuVO fromMenu(Menu menu){
        if (menu == null){
            return null;
        }
        MenuVO menuVO = new MenuVO();
        menuVO.setId(menu.getId());
        menuVO.setPid(menu.getPid());
        menuVO.setTitle(menu.getTitle());
        menuVO.setIcon(menu.getIcon());
        menuVO.setSort(menu.getSort());
        menuVO.setHref(menu.getHref());
        menuVO.setAuthority(menu.getAuthority());
        menuVO.setStatus(menu.getStatus());
        menuVO.setType(menu.getType());
        menuVO.setTarget(menu.getTarget());
        menuVO.setCreateTime(menu.getCreateTime());
        menuVO.child = new ArrayList<>();
        return menuVO;
    }

}
