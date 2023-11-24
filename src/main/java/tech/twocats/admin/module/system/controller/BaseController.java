package tech.twocats.admin.module.system.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tech.twocats.admin.module.system.domain.vo.InitVO;
import tech.twocats.admin.module.user.domain.dto.UserDetailDTO;
import tech.twocats.admin.module.user.domain.entity.User;
import tech.twocats.admin.module.user.domain.vo.MenuVO;
import tech.twocats.admin.module.user.service.IMenuService;

import javax.servlet.http.HttpSession;
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
     * 首页
     */
    @RequestMapping(value = {"/", "/index"})
    @PreAuthorize("@authCheck.check('dashbroad')")
    public String index(@AuthenticationPrincipal UserDetailDTO userDetail, Model model) {
        model.addAttribute("name", StringUtils.hasLength(userDetail.getRealName())
                ? userDetail.getRealName()
                : userDetail.getNickname());
        return "/index";
    }

    /**
     * 首屏
     */
    @RequestMapping(value = {"/welcome"})
    @PreAuthorize("@authCheck.check('dashbroad')")
    public String welcomeView() {
        return "/view/dashboard/welcome-1";
    }

    @ResponseBody
    @RequestMapping("/init")
    public InitVO initSystem(@AuthenticationPrincipal UserDetailDTO userDetail){
        List<MenuVO> menus = menuService.getMenusByUserId(userDetail.getId());
        InitVO initVO = new InitVO();
        initVO.setHomeInfo(new InitVO.BaseInfo(homeTitle, homeHref));
        initVO.setLogoInfo(new InitVO.BaseInfo(logoTitle, logoHref, logoImage));
        initVO.setMenuInfo(menus);
        return initVO;
    }

}
