package tech.twocats.admin.module.admin.domain.vo;

import lombok.Getter;
import lombok.Setter;
import tech.twocats.admin.common.ValidateGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class RoleRequest {

    @NotNull(message = "角色ID不能为空",
            groups = {ValidateGroup.Edit.class, ValidateGroup.StatusChange.class})
    private Long id;

    @NotBlank(message = "角色名称不能为空",
            groups = {ValidateGroup.Add.class, ValidateGroup.Edit.class})
    private String roleName;

    @NotBlank(message = "角色编码不能为空",
            groups = {ValidateGroup.Add.class, ValidateGroup.Edit.class})
    private String roleCode;

    private Boolean status;

    private String remark;

    private List<Long> menuIds;

}
