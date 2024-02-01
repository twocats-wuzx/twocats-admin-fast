package com.community.manager.module.system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.community.manager.module.system.domain.vo.InitVO;
import com.community.manager.module.system.domain.dto.UserDetailDTO;
import com.community.manager.module.admin.domain.vo.MenuVO;
import com.community.manager.module.admin.service.IMenuService;

import java.util.List;

@Controller
public class BaseController {

    @Value("${app.home-info.title}")
    private String homeTitle;
    @Value("${app.home-info.href}")
    private String homeHref;
    @Value("${app.logo-info.title}")
    private String logoTitle;
    @Value("${app.logo-info.href}")
    private String logoHref;
    @Value("${app.logo-info.image}")
    private String logoImage;

    private final IMenuService menuService;
    private final SecurityContextHolderStrategy securityContextHolderStrategy
            = SecurityContextHolder.getContextHolderStrategy();

    public BaseController(IMenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 后台首页
     */
    @RequestMapping(value = {"/admin/index"})
    public String adminIndexView(@AuthenticationPrincipal UserDetailDTO userDetail, Model model) {
        String name = userDetail.getUsername();
        if (StringUtils.hasLength(userDetail.getNickname())){
            name = userDetail.getNickname();
        }
        if (StringUtils.hasLength(userDetail.getRealName())){
            name = userDetail.getRealName();
        }
        model.addAttribute("name", name);
        model.addAttribute("avatar", userDetail.getAvatar());
        return "view/index";
    }

    /**
     * 后台首页
     */
    @RequestMapping(value = {"/", "/index"})
    public String indexView(@AuthenticationPrincipal UserDetailDTO userDetail, Model model) {
        return "index";
    }

    /**
     * 首屏
     */
    @RequestMapping(value = {"/admin/welcome"})
    public String welcomeView() {
        return "view/dashboard/welcome";
    }

    @ResponseBody
    @RequestMapping("/admin/init")
    public InitVO initSystem(@AuthenticationPrincipal UserDetailDTO userDetail){
        List<MenuVO> menus = menuService.getMenusByUserId(userDetail.getId());
        InitVO initVO = new InitVO();
        initVO.setHomeInfo(new InitVO.BaseInfo(homeTitle, homeHref));
        initVO.setLogoInfo(new InitVO.BaseInfo(logoTitle, logoHref, logoImage));
        initVO.setMenuInfo(menus);
        return initVO;
    }

}
