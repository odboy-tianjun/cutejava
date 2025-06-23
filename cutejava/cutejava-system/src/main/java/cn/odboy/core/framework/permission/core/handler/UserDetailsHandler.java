package cn.odboy.core.framework.permission.core.handler;

import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.model.system.SystemRoleCodeVo;
import cn.odboy.core.dal.model.system.SystemUserJwtVo;
import cn.odboy.core.dal.redis.system.SystemUserJwtDAO;
import cn.odboy.core.service.system.SystemDataService;
import cn.odboy.core.service.system.SystemRoleService;
import cn.odboy.core.service.system.SystemUserService;
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
    private final SystemRoleService systemRoleService;
    private final SystemUserService systemUserService;
    private final SystemDataService systemDataService;
    private final SystemUserJwtDAO systemUserJwtDAO;

    @Override
    public SystemUserJwtVo loadUserByUsername(String username) {
        SystemUserJwtVo userJwtVo = systemUserJwtDAO.queryUserJwtModelByUsername(username);
        if (userJwtVo == null) {
            SystemUserTb user = systemUserService.queryUserByUsername(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活！");
                }
                // 获取用户的权限
                List<SystemRoleCodeVo> authorities = systemRoleService.buildUserRolePermissions(user);
                // 初始化JwtUserDto
                userJwtVo = new SystemUserJwtVo(user, systemDataService.queryDeptIdListByArgs(user), authorities);
                // 添加缓存数据
                systemUserJwtDAO.saveUserJwtModelByUserName(username, userJwtVo);
            }
        }
        return userJwtVo;
    }
}
