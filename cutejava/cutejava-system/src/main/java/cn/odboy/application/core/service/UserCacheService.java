package cn.odboy.application.core.service;

import cn.odboy.model.system.dto.UserJwtDto;
import org.springframework.scheduling.annotation.Async;

public interface UserCacheService {
    /**
     * 返回用户缓存
     *
     * @param username 用户名
     * @return UserJwtDto
     */
    UserJwtDto getUserCacheByUsername(String username);

    /**
     * 添加缓存到Redis
     *
     * @param userName 用户名
     */
    @Async
    void addUserCache(String userName, UserJwtDto user);

    /**
     * 清理用户缓存信息
     * 用户信息变更时
     *
     * @param userName 用户名
     */
    @Async
    void cleanUserCacheByUsername(String userName);
}
