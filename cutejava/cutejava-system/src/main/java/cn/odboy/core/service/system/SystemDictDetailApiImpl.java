package cn.odboy.core.service.system;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.SystemDictDetailTb;
import cn.odboy.core.dal.mysql.system.SystemDictDetailMapper;
import cn.odboy.core.dal.model.system.QuerySystemDictDetailArgs;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SystemDictDetailApiImpl implements SystemDictDetailApi {
    private final SystemDictDetailMapper dictDetailMapper;
    private final RedisHelper redisHelper;

    @Override
    public CsResultVo<List<SystemDictDetailTb>> queryDictDetailListByArgs(QuerySystemDictDetailArgs criteria, Page<Object> page) {
        return PageUtil.toPage(dictDetailMapper.queryDictDetailListByArgs(criteria, page));
    }

    @Override
    public List<SystemDictDetailTb> describeDictDetailListByName(String name) {
        String key = SystemRedisKey.DICT_NAME + name;
        List<SystemDictDetailTb> dictDetails = redisHelper.getList(key, SystemDictDetailTb.class);
        if (CollUtil.isEmpty(dictDetails)) {
            dictDetails = dictDetailMapper.queryDictDetailListByDictName(name);
            redisHelper.set(key, dictDetails, 1, TimeUnit.DAYS);
        }
        return dictDetails;
    }
}
