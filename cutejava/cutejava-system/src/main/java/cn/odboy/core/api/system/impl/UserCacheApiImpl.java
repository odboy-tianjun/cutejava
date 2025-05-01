package cn.odboy.core.api.system.impl;

import cn.odboy.core.api.system.UserCacheApi;
import cn.odboy.core.constant.SystemRedisKey;
import cn.odboy.core.service.system.dto.UserJwtVo;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
@RequiredArgsConstructor
public class UserCacheApiImpl implements UserCacheApi {
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
}
