package cn.odboy.modules.security.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.odboy.model.system.dto.JwtUserDto;
import cn.odboy.modules.security.config.LoginProperties;
import cn.odboy.modules.security.service.UserCacheService;
import cn.odboy.util.RedisUtil;
import cn.odboy.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户缓存管理
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {
    @Resource
    private RedisUtil redisUtil;
    @Value("${login.user-cache.idle-time}")
    private long idleTime;

    @Override
    public JwtUserDto getUserCache(String userName) {
        // 转小写
        userName = StringUtil.lowerCase(userName);
        if (StringUtil.isNotEmpty(userName)) {
            // 获取数据
            return redisUtil.get(LoginProperties.cacheKey + userName, JwtUserDto.class);
        }
        return null;
    }


    @Override
    public void addUserCache(String userName, JwtUserDto user) {
        // 转小写
        userName = StringUtil.lowerCase(userName);
        if (StringUtil.isNotEmpty(userName)) {
            // 添加数据, 避免数据同时过期
            long time = idleTime + RandomUtil.randomInt(900, 1800);
            redisUtil.set(LoginProperties.cacheKey + userName, user, time);
        }
    }


    @Override
    public void cleanUserCache(String userName) {
        // 转小写
        userName = StringUtil.lowerCase(userName);
        if (StringUtil.isNotEmpty(userName)) {
            // 清除数据
            redisUtil.del(LoginProperties.cacheKey + userName);
        }
    }
}