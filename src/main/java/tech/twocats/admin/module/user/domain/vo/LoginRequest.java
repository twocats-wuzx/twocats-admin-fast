package tech.twocats.admin.module.user.domain.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest{

    private String username;

    private String password;

}
