package cn.odboy.core.service.system.impl;

import cn.odboy.core.api.system.DataApi;
import cn.odboy.core.api.system.RoleApi;
import cn.odboy.core.api.system.UserApi;
import cn.odboy.core.api.system.UserCacheApi;
import cn.odboy.core.dal.dataobject.system.User;
import cn.odboy.core.service.system.UserCacheService;
import cn.odboy.core.service.system.dto.RoleCodeVo;
import cn.odboy.core.service.system.dto.UserJwtVo;
import cn.odboy.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private final RoleApi roleApi;
    private final UserApi userApi;
    private final DataApi dataApi;
    private final UserCacheService userCacheService;
    private final UserCacheApi userCacheApi;

    @Override
    public UserJwtVo loadUserByUsername(String username) {
        UserJwtVo userJwtVo = userCacheApi.describeUserJwtModelByUsername(username);
        if (userJwtVo == null) {
            User user = userApi.describeUserByUsername(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                // 获取用户的权限
                List<RoleCodeVo> authorities = roleApi.buildUserRolePermissions(user);
                // 初始化JwtUserDto
                userJwtVo = new UserJwtVo(user, dataApi.describeDeptIdListByUserIdWithDeptId(user), authorities);
                // 添加缓存数据
                userCacheService.saveUserJwtModelByUserName(username, userJwtVo);
            }
        }
        return userJwtVo;
    }
}
