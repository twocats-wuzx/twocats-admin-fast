package tech.twocats.admin.module.system.domain.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.twocats.admin.common.AppConstant;
import tech.twocats.admin.common.enums.GenderEnum;
import tech.twocats.admin.module.admin.domain.entity.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDetailDTO implements UserDetails {

    private Long id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 MALE.男 FEMALE.女
     */
    private GenderEnum gender;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 状态 0.禁用 1.启用
     */
    private Boolean status;

    private List<SimpleGrantedAuthority> authorities;

    private UserDetailDTO(){

    }

    public static UserDetailDTO build(User user, List<String> permissions){
        UserDetailDTO userDetail = new UserDetailDTO();
        userDetail.setId(user.getId());
        userDetail.setUsername(user.getUsername());
        userDetail.setPassword(user.getPassword());
        userDetail.setRealName(user.getRealName());
        userDetail.setNickname(user.getNickname());
        userDetail.setAvatar(user.getAvatar());
        userDetail.setGender(user.getGender());
        userDetail.setEmail(user.getEmail());
        userDetail.setMobile(user.getMobile());
        userDetail.setStatus(user.getStatus());


        Optional.ofNullable(permissions)
                        .ifPresent(list -> {
                            userDetail.setAuthorities(list
                                    .stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toList())
                            );
                        });
        if (userDetail.getAuthorities() == null){
            userDetail.setAuthorities(new ArrayList<>(1));
        }
        if (user.getId().equals(AppConstant.DEFAULT_SUPER_ADMIN_ID)){
            userDetail.authorities.add(new SimpleGrantedAuthority(AppConstant.SUPER_ADMIN_AUTH_CODE));
        }
        return userDetail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }
}
