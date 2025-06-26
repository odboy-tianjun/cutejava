package cn.odboy.system.framework.permission.core.handler;

import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemRoleCodeVo;
import cn.odboy.system.dal.model.SystemUserJwtVo;
import cn.odboy.system.dal.redis.SystemUserInfoDAO;
import cn.odboy.system.service.SystemDataService;
import cn.odboy.system.service.SystemRoleService;
import cn.odboy.system.service.SystemUserService;
import cn.odboy.framework.exception.BadRequestException;
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
    private final SystemUserInfoDAO systemUserInfoDAO;

    @Override
    public SystemUserJwtVo loadUserByUsername(String username) {
        SystemUserJwtVo userJwtVo = systemUserInfoDAO.getUserLoginInfoByUserName(username);
        if (userJwtVo == null) {
            SystemUserTb user = systemUserService.getUserByUsername(username);
            if (user == null) {
                throw new BadRequestException("用户不存在");
            } else {
                if (!user.getEnabled()) {
                    throw new BadRequestException("账号未激活");
                }
                // 获取用户的权限
                List<SystemRoleCodeVo> authorities = systemRoleService.buildUserRolePermissions(user);
                // 初始化JwtUserDto
                userJwtVo = new SystemUserJwtVo(user, systemDataService.queryDeptIdListByArgs(user), authorities);
                // 添加缓存数据
                systemUserInfoDAO.saveUserLoginInfoByUserName(username, userJwtVo);
            }
        }
        return userJwtVo;
    }
}
