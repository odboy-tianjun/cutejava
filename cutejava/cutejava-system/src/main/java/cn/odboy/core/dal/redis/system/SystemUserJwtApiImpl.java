package cn.odboy.core.dal.redis.system;

import cn.odboy.core.dal.model.system.SystemUserJwtVo;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@RequiredArgsConstructor
public class SystemUserJwtApiImpl implements SystemUserJwtApi {
    @Resource
    private RedisHelper redisHelper;

    @Override
    public SystemUserJwtVo describeUserJwtModelByUsername(String username) {
        // 转小写
        username = StringUtil.lowerCase(username);
        if (StringUtil.isNotEmpty(username)) {
            // 获取数据
            return redisHelper.get(SystemRedisKey.USER_INFO + username, SystemUserJwtVo.class);
        }
        return null;
    }
}
