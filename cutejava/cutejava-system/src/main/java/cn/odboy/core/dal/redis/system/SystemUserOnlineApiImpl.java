package cn.odboy.core.dal.redis.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.model.system.SystemUserOnlineVo;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.PageUtil;
import cn.odboy.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemUserOnlineApiImpl implements SystemUserOnlineApi {
    private final RedisHelper redisHelper;

    @Override
    public CsResultVo<List<SystemUserOnlineVo>> queryUserOnlineModelPage(String username, Pageable pageable) {
        List<SystemUserOnlineVo> onlineUserList = queryUserOnlineModelListByUsername(username);
        List<SystemUserOnlineVo> paging = PageUtil.softPaging(pageable.getPageNumber(), pageable.getPageSize(), onlineUserList);
        return PageUtil.toPage(paging, onlineUserList.size());
    }

    @Override
    public List<SystemUserOnlineVo> queryUserOnlineModelListByUsername(String username) {
        String loginKey = SystemRedisKey.ONLINE_USER + (StringUtil.isBlank(username) ? "" : "*" + username);
        List<String> keys = redisHelper.scan(loginKey + "*");
        Collections.reverse(keys);
        List<SystemUserOnlineVo> onlineUserList = new ArrayList<>();
        for (String key : keys) {
            onlineUserList.add(redisHelper.get(key, SystemUserOnlineVo.class));
        }
        onlineUserList.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
        return onlineUserList;
    }

    @Override
    public SystemUserOnlineVo queryUserOnlineModelByKey(String key) {
        return redisHelper.get(key, SystemUserOnlineVo.class);
    }
}
