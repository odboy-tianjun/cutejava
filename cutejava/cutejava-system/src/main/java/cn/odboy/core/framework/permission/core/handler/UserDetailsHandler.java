package cn.odboy.core.framework.permission.core.handler;

import cn.odboy.core.dal.model.system.SystemRoleCodeVo;
import cn.odboy.core.dal.model.system.SystemUserJwtVo;
import cn.odboy.core.dal.redis.system.SystemUserJwtApi;
import cn.odboy.core.dal.redis.system.SystemUserJwtService;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.service.system.ookkoko.SystemDataService;
import cn.odboy.core.service.system.SystemRoleApi;
import cn.odboy.core.service.system.SystemUserApi;
import cn.odboy.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsHandler implements UserDetailsService {
    private final SystemRoleApi systemRoleApi;
    private final SystemUserApi systemUserApi;
    private final SystemDataService systemDataService;
    private final SystemUserJwtService systemUserJwtService;
    private final SystemUserJwtApi systemUserJwtApi;

    @Override
    public SystemUserJwtVo loadUserByUsername(String username) {
        SystemUserJwtVo userJwtVo = systemUserJwtApi.describeUserJwtModelByUsername(username);
        if (userJwtVo == null) {
            SystemUserTb user = systemUserApi.describeUserByUsername(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                // 获取用户的权限
                List<SystemRoleCodeVo> authorities = systemRoleApi.buildUserRolePermissions(user);
                // 初始化JwtUserDto
                userJwtVo = new SystemUserJwtVo(user, systemDataService.describeDeptIdListByUserIdWithDeptId(user), authorities);
                // 添加缓存数据
                systemUserJwtService.saveUserJwtModelByUserName(username, userJwtVo);
            }
        }
        return userJwtVo;
    }
}
