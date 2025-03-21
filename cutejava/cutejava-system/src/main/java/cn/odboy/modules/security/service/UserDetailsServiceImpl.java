package cn.odboy.modules.security.service;

import cn.odboy.exception.BadRequestException;
import cn.odboy.modules.security.service.dto.AuthorityDto;
import cn.odboy.modules.security.service.dto.JwtUserDto;
import cn.odboy.modules.system.domain.User;
import cn.odboy.modules.system.service.DataService;
import cn.odboy.modules.system.service.RoleService;
import cn.odboy.modules.system.service.UserService;
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
    private final UserCacheManager userCacheManager;

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        if (jwtUserDto == null) {
            User user = userService.getLoginData(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                // 获取用户的权限
                List<AuthorityDto> authorities = roleService.buildPermissions(user);
                // 初始化JwtUserDto
                jwtUserDto = new JwtUserDto(user, dataService.getDeptIds(user), authorities);
                // 添加缓存数据
                userCacheManager.addUserCache(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
