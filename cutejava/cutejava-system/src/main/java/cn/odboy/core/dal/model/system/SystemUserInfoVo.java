package cn.odboy.core.dal.model.system;

import cn.odboy.base.CsSerializeObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class SystemUserInfoVo extends CsSerializeObject {
    private SystemSimpleUserVo user;
    private List<Long> dataScopes;
    private List<SystemRoleCodeVo> authorities;

    public Set<String> getRoles() {
        return authorities.stream().map(SystemRoleCodeVo::getAuthority).collect(Collectors.toSet());
    }
}
