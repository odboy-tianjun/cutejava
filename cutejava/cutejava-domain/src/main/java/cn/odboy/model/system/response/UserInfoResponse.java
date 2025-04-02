package cn.odboy.model.system.response;

import cn.odboy.base.MyObject;
import cn.odboy.model.system.dto.RoleCodeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfoResponse extends MyObject {
    private SimpleUserResponse user;
    private List<Long> dataScopes;
    private List<RoleCodeDto> authorities;

    public Set<String> getRoles() {
        return authorities.stream().map(RoleCodeDto::getAuthority).collect(Collectors.toSet());
    }
}
