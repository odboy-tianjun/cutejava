package cn.odboy.core.service.system;

import cn.odboy.core.service.system.dto.UserJwtVo;
import org.springframework.scheduling.annotation.Async;

public interface UserCacheService {
    /**
     * 返回用户缓存
     *
     * @param username 用户名
     * @return UserJwtVo
     */
    UserJwtVo describeUserJwtModelByUsername(String username);

    /**
     * 添加缓存到Redis
     *
     * @param userName 用户名
     */
    @Async
    void saveUserJwtModelByUserName(String userName, UserJwtVo user);

    /**
     * 清理用户缓存信息
     * 用户信息变更时
     *
     * @param userName 用户名
     */
    @Async
    void cleanUserJwtModelCacheByUsername(String userName);
}
