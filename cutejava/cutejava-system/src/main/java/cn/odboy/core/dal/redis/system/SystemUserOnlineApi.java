package cn.odboy.core.dal.redis.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.model.system.SystemUserOnlineVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SystemUserOnlineApi {

    /**
     * 查询全部数据
     *
     * @param username /
     * @param pageable /
     * @return /
     */
    CsResultVo<List<SystemUserOnlineVo>> queryUserOnlineModelPage(String username, Pageable pageable);

    /**
     * 查询全部数据，不分页
     *
     * @param username /
     * @return /
     */
    List<SystemUserOnlineVo> queryUserOnlineModelListByUsername(String username);

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    SystemUserOnlineVo queryUserOnlineModelByKey(String key);
}
