package cn.odboy.core.dal.redis.system;

import cn.hutool.core.util.RandomUtil;
import cn.odboy.core.dal.model.system.SystemUserJwtVo;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户缓存管理
 */
@Service
public class SystemUserJwtServiceImpl implements SystemUserJwtService {
    @Resource
    private RedisHelper redisHelper;

    @Override
    public void saveUserJwtModelByUserName(String userName, SystemUserJwtVo user) {
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
