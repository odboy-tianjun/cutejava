package cn.odboy.context;

import cn.odboy.util.SecurityUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "el")
public class AuthorityConfig {

    /**
     * 判断接口是否有权限
     *
     * @param permissions 权限
     * @return /
     */
    public Boolean check(String... permissions) {
        // 获取当前用户的所有权限
        List<String> roleList = SecurityUtil.getCurrentUser().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        // 判断当前用户的所有权限是否包含接口上定义的权限
        return roleList.contains("admin") || Arrays.stream(permissions).anyMatch(roleList::contains);
    }
}
