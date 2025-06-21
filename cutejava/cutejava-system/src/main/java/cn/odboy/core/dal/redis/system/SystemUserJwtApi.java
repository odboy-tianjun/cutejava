package cn.odboy.core.dal.redis.system;


import cn.odboy.core.dal.model.system.SystemUserJwtVo;

public interface SystemUserJwtApi {
    /**
     * 返回用户缓存
     *
     * @param username 用户名
     * @return UserJwtVo
     */
    SystemUserJwtVo queryUserJwtModelByUsername(String username);
}
