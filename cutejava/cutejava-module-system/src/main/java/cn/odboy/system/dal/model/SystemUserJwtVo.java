package cn.odboy.system.dal.model;

import cn.odboy.system.dal.dataobject.SystemUserTb;
import com.alibaba.fastjson2.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class SystemUserJwtVo implements UserDetails {

    @ApiModelProperty(value = "用户")
    private final SystemUserTb user;

    @ApiModelProperty(value = "数据权限")
    private final List<Long> dataScopes;

    @ApiModelProperty(value = "角色")
    private final List<SystemRoleCodeVo> authorities;

    public Set<String> getRoles() {
        return authorities.stream().map(SystemRoleCodeVo::getAuthority).collect(Collectors.toSet());
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
