package cn.odboy.system.framework.permission.core.handler;

import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemRoleCodeVo;
import cn.odboy.system.dal.model.SystemUserJwtVo;
import cn.odboy.system.dal.redis.SystemUserInfoDAO;
import cn.odboy.system.service.SystemDataService;
import cn.odboy.system.service.SystemRoleService;
import cn.odboy.system.service.SystemUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("userDetailsService")
public class UserDetailsHandler implements UserDetailsService {
    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemDataService systemDataService;
    @Autowired
    private SystemUserInfoDAO systemUserInfoDAO;

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
