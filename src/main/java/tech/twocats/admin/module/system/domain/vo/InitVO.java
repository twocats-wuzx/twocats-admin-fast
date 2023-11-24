package tech.twocats.admin.module.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import tech.twocats.admin.module.user.domain.vo.MenuVO;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitVO {

    /**
     * 首页信息
     */
    private BaseInfo homeInfo;

    /**
     * Logo信息
     */
    private BaseInfo logoInfo;

    /**
     * 菜单信息
     */
    private List<MenuVO> menuInfo;

    @Getter
    @Setter
    public static class BaseInfo{
        /**
         * 标题
         */
        private String title;

        /**
         * 链接
         */
        private String href;

        /**
         * Logo
         */
        private String image;

        public BaseInfo(String title, String href) {
            this.title = title;
            this.href = href;
        }

        public BaseInfo(String title, String href, String image) {
            this.title = title;
            this.href = href;
            this.image = image;
        }
    }

}
