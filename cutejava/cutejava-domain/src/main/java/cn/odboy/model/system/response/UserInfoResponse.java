package cn.odboy.model.system.response;

import cn.odboy.base.MyObject;
import cn.odboy.model.system.model.RoleCodeModel;
import cn.odboy.model.system.model.SimpleUserModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserInfoResponse extends MyObject {
    private SimpleUserModel user;
    private List<Long> dataScopes;
    private List<RoleCodeModel> authorities;

    public Set<String> getRoles() {
        return authorities.stream().map(RoleCodeModel::getAuthority).collect(Collectors.toSet());
    }
}
