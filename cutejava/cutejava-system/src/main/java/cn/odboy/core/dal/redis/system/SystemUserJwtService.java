package cn.odboy.core.dal.redis.system;

import cn.odboy.core.dal.model.system.SystemUserJwtVo;
import org.springframework.scheduling.annotation.Async;

public interface SystemUserJwtService {
    /**
     * 添加缓存到Redis
     *
     * @param userName 用户名
     */
    @Async
    void saveUserJwtModelByUserName(String userName, SystemUserJwtVo user);

    /**
     * 清理用户缓存信息
     * 用户信息变更时
     *
     * @param userName 用户名
     */
    @Async
    void cleanUserJwtModelCacheByUsername(String userName);
}
