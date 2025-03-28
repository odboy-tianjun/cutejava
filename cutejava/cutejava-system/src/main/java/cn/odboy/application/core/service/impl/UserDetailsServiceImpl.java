package cn.odboy.application.core.service.impl;

import cn.odboy.application.core.service.UserCacheService;
import cn.odboy.application.system.service.DataService;
import cn.odboy.application.system.service.RoleService;
import cn.odboy.application.system.service.UserService;
import cn.odboy.exception.BadRequestException;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.dto.RoleCodeDto;
import cn.odboy.model.system.dto.UserJwtDto;
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
    public UserJwtDto loadUserByUsername(String username) {
        UserJwtDto userJwtDto = userCacheService.getUserCacheByUsername(username);
        if (userJwtDto == null) {
            User user = userService.getUserByUsername(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                // 获取用户的权限
                List<RoleCodeDto> authorities = roleService.buildPermissions(user);
                // 初始化JwtUserDto
                userJwtDto = new UserJwtDto(user, dataService.selectDeptIdByUserIdWithDeptId(user), authorities);
                // 添加缓存数据
                userCacheService.addUserCache(username, userJwtDto);
            }
        }
        return userJwtDto;
    }
}
