package cn.odboy.core.dal.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

/**
 * 避免序列化问题
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemRoleCodeVo implements GrantedAuthority {

    @ApiModelProperty(value = "角色名")
    private String authority;
}
