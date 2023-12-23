package tech.twocats.admin.module.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import tech.twocats.admin.common.AppConstant;
import tech.twocats.admin.common.ValidateGroup;
import tech.twocats.admin.common.enums.LinkTypeEnum;
import tech.twocats.admin.common.enums.MenuTypeEnum;
import tech.twocats.admin.common.error.SystemError;
import tech.twocats.admin.exception.BaseException;
import tech.twocats.admin.module.admin.domain.entity.Menu;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MenuRequest {

    /**
     * id
     */
    @NotNull(message = "菜单ID不能为空",
            groups = {ValidateGroup.Edit.class, ValidateGroup.StatusChange.class})
    private Long id;

    /**
     * pid
     */
    private Long pid;

    /**
     * 标题
     */
    @NotNull(message = "标题不能为空", groups = {ValidateGroup.Add.class, ValidateGroup.Edit.class})
    private String title;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单顺序
     */
    @NotNull(message = "菜单顺序不能为空", groups = {ValidateGroup.Add.class, ValidateGroup.Edit.class})
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
    @NotNull(message = "菜单类型不能为空", groups = {ValidateGroup.Add.class, ValidateGroup.Edit.class})
    private MenuTypeEnum type;

    /**
     * 状态
     */
    @NotNull(message = "菜单状态不能为空", groups = {ValidateGroup.StatusChange.class})
    private Boolean status;

    /**
     * 授权标识
     */
    private String authority;


    public static Menu toMenu(MenuRequest request) {
        Menu menu = new Menu();
        menu.setId(request.getId());
        menu.setPid(request.pid == null ? -1 : request.getPid());
        menu.setTitle(request.getTitle());
        menu.setSort(request.getSort());
        menu.setType(request.getType());
        menu.setAuthority(request.getAuthority());
        menu.setStatus(Boolean.TRUE);

        // 菜单和权限设置不同的值
        switch (request.getType()) {
            case MENU:
                menu.setHref(request.getHref());
                menu.setIcon(request.getIcon());
                menu.setTarget(request.getTarget());
                break;
            case PERMISSION:
                menu.setHref(AppConstant.EMPTY_STRING);
                menu.setIcon(AppConstant.EMPTY_STRING);
                menu.setTarget(null);
                break;
            default:
                break;
        }
        return menu;
    }

    public static void checkMenu(MenuRequest request) {
        // 对菜单和权限不同的关键字进行校验
        List<String> errorMessage = new ArrayList<>();
        if (!StringUtils.hasLength(request.getAuthority())){
            errorMessage.add("授权标识不能为空");
        }
        switch (request.getType()){
            case MENU:
                if (request.getTarget() == null && StringUtils.hasLength(request.getHref())){
                    errorMessage.add("链接打开方式和路由地址必须同时存在");
                }
                if (request.getTarget() != null && !StringUtils.hasLength(request.getHref())){
                    errorMessage.add("链接打开方式和路由地址必须同时存在");
                }
                if (!StringUtils.hasLength(request.getIcon())){
                    errorMessage.add("图标不能为空");
                }
                break;
            case PERMISSION:
                break;
            default:
                break;
        }
        if (!errorMessage.isEmpty()){
            throw new BaseException(SystemError.INVALID_PARAMETER, String.join(", ", errorMessage));
        }
    }


}
