package cn.odboy.application.core.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.odboy.application.core.service.UserCacheService;
import cn.odboy.constant.SystemRedisKey;
import cn.odboy.model.system.model.UserJwtVo;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.StringUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * 用户缓存管理
 */
@Service
public class UserCacheServiceImpl implements UserCacheService {
    @Resource
    private RedisHelper redisHelper;

    @Override
    public UserJwtVo describeUserJwtModelByUsername(String username) {
        // 转小写
        username = StringUtil.lowerCase(username);
        if (StringUtil.isNotEmpty(username)) {
            // 获取数据
            return redisHelper.get(SystemRedisKey.USER_INFO + username, UserJwtVo.class);
        }
        return null;
    }


    @Override
    public void saveUserJwtModelByUserName(String userName, UserJwtVo user) {
        // 转小写
        userName = StringUtil.lowerCase(userName);
        if (StringUtil.isNotEmpty(userName)) {
            // 添加数据, 避免数据同时过期（2小时左右）
            long time = 7200 + RandomUtil.randomInt(900, 1800);
            redisHelper.set(SystemRedisKey.USER_INFO + userName, user, time);
        }
    }


    @Override
    public void cleanUserJwtModelCacheByUsername(String userName) {
        // 转小写
        userName = StringUtil.lowerCase(userName);
        if (StringUtil.isNotEmpty(userName)) {
            // 清除数据
            redisHelper.del(SystemRedisKey.USER_INFO + userName);
        }
    }
}