package cn.odboy.application.core.service.impl;

import cn.odboy.application.core.service.UserCacheService;
import cn.odboy.application.system.service.DataService;
import cn.odboy.application.system.service.RoleService;
import cn.odboy.application.system.service.UserService;
import cn.odboy.exception.BadRequestException;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.model.RoleCodeVo;
import cn.odboy.model.system.model.UserJwtVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserService userService;
    private final RoleService roleService;
    private final DataService dataService;
    private final UserCacheService userCacheService;

    @Override
    public UserJwtVo loadUserByUsername(String username) {
        UserJwtVo userJwtVo = userCacheService.describeUserJwtModelByUsername(username);
        if (userJwtVo == null) {
            User user = userService.describeUserByUsername(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                // 获取用户的权限
                List<RoleCodeVo> authorities = roleService.buildUserRolePermissions(user);
                // 初始化JwtUserDto
                userJwtVo = new UserJwtVo(user, dataService.describeDeptIdListByUserIdWithDeptId(user), authorities);
                // 添加缓存数据
                userCacheService.saveUserJwtModelByUserName(username, userJwtVo);
            }
        }
        return userJwtVo;
    }
}
