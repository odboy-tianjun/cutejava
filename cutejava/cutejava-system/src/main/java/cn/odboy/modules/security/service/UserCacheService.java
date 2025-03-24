package cn.odboy.modules.security.service;

import cn.odboy.model.system.dto.JwtUserDto;
import org.springframework.scheduling.annotation.Async;

public interface UserCacheService {
    /**
     * 返回用户缓存
     *
     * @param userName 用户名
     * @return JwtUserDto
     */
    JwtUserDto getUserCache(String userName);

    /**
     * 添加缓存到Redis
     *
     * @param userName 用户名
     */
    @Async
    void addUserCache(String userName, JwtUserDto user);
    /**
     * 清理用户缓存信息
     * 用户信息变更时
     *
     * @param userName 用户名
     */
    @Async
    void cleanUserCache(String userName);
}
