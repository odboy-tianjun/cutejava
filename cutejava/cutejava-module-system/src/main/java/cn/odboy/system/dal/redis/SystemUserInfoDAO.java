package cn.odboy.system.dal.redis;

import cn.hutool.core.util.RandomUtil;
import cn.odboy.framework.redis.RedisHelper;
import cn.odboy.system.dal.model.SystemUserJwtVo;
import cn.odboy.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用户缓存管理
 */
@Component
@RequiredArgsConstructor
public class SystemUserInfoDAO {
    private final RedisHelper redisHelper;

    /**
     * 添加缓存到Redis
     *
     * @param userName 用户名
     */
    @Async
    public void saveUserLoginInfoByUserName(String userName, SystemUserJwtVo user) {
        if (StringUtil.isNotEmpty(userName)) {
            // 添加数据, 避免数据同时过期（2小时左右）
            long time = 7200 + RandomUtil.randomInt(900, 1800);
            redisHelper.set(SystemRedisKey.USER_INFO + userName, user, time);
        }
    }

    /**
     * 清理用户缓存信息
     * 用户信息变更时
     *
     * @param userName 用户名
     */
    @Async
    public void deleteUserLoginInfoByUserName(String userName) {
        if (StringUtil.isNotEmpty(userName)) {
            // 清除数据
            redisHelper.del(SystemRedisKey.USER_INFO + userName);
        }
    }

    /**
     * 返回用户缓存
     *
     * @param username 用户名
     * @return UserJwtVo
     */

    public SystemUserJwtVo getUserLoginInfoByUserName(String username) {
        if (StringUtil.isNotEmpty(username)) {
            // 获取数据
            return redisHelper.get(SystemRedisKey.USER_INFO + username, SystemUserJwtVo.class);
        }
        return null;
    }
}
