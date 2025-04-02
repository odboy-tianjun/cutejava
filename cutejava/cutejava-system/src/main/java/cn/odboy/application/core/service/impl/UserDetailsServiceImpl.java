package cn.odboy.application.core.service.impl;

import cn.odboy.application.core.service.UserCacheService;
import cn.odboy.application.system.service.DataService;
import cn.odboy.application.system.service.RoleService;
import cn.odboy.application.system.service.UserService;
import cn.odboy.exception.BadRequestException;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.model.UserJwtModel;
import cn.odboy.model.system.model.RoleCodeModel;
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
    public UserJwtModel loadUserByUsername(String username) {
        UserJwtModel userJwtModel = userCacheService.getUserCacheByUsername(username);
        if (userJwtModel == null) {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                // 获取用户的权限
                List<RoleCodeModel> authorities = roleService.buildPermissions(user);
                // 初始化JwtUserDto
                userJwtModel = new UserJwtModel(user, dataService.selectDeptIdByUserIdWithDeptId(user), authorities);
                // 添加缓存数据
                userCacheService.addUserCache(username, userJwtModel);
            }
        }
        return userJwtModel;
    }
}
