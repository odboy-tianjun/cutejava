package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.core.dal.model.system.QuerySystemUserArgs;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SystemUserApiImpl implements SystemUserApi {
    private final SystemUserMapper userMapper;
    private final RedisHelper redisHelper;

    @Override
    public CsResultVo<List<SystemUserTb>> describeUserPage(QuerySystemUserArgs criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<SystemUserTb> users = userMapper.queryUserPageByArgs(criteria, PageUtil.getCount(userMapper)).getRecords();
        Long total = userMapper.getUserCountByArgs(criteria);
        return PageUtil.toPage(users, total);
    }

    @Override
    public List<SystemUserTb> describeUserList(QuerySystemUserArgs criteria) {
        return userMapper.queryUserPageByArgs(criteria, PageUtil.getCount(userMapper)).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SystemUserTb describeUserById(long id) {
        String key = SystemRedisKey.USER_ID + id;
        SystemUserTb user = redisHelper.get(key, SystemUserTb.class);
        if (user == null) {
            user = userMapper.selectById(id);
            redisHelper.set(key, user, 1, TimeUnit.DAYS);
        }
        return user;
    }

    @Override
    public SystemUserTb describeUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }
}
