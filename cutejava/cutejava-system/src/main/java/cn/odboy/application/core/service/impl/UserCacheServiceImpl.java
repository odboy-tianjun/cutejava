package cn.odboy.application.core.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.odboy.application.core.config.LoginProperties;
import cn.odboy.application.core.service.UserCacheService;
import cn.odboy.model.system.dto.JwtUserDto;
import cn.odboy.util.RedisUtil;
import cn.odboy.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户缓存管理
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {
    @Resource
    private RedisUtil redisUtil;

    @Override
    public JwtUserDto getUserCacheByUsername(String username) {
        // 转小写
        username = StringUtil.lowerCase(username);
        if (StringUtil.isNotEmpty(username)) {
            // 获取数据
            return redisUtil.get(LoginProperties.cacheKey + username, JwtUserDto.class);
        }
        return null;
    }


    @Override
    public void addUserCache(String userName, JwtUserDto user) {
        // 转小写
        userName = StringUtil.lowerCase(userName);
        if (StringUtil.isNotEmpty(userName)) {
            // 添加数据, 避免数据同时过期（2小时左右）
            long time = 7200 + RandomUtil.randomInt(900, 1800);
            redisUtil.set(LoginProperties.cacheKey + userName, user, time);
        }
    }


    @Override
    public void cleanUserCacheByUsername(String userName) {
        // 转小写
        userName = StringUtil.lowerCase(userName);
        if (StringUtil.isNotEmpty(userName)) {
            // 清除数据
            redisUtil.del(LoginProperties.cacheKey + userName);
        }
    }
}