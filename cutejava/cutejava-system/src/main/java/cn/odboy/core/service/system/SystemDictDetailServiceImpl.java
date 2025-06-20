package cn.odboy.core.service.system;

import cn.hutool.core.bean.BeanUtil;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.SystemDictDetailTb;
import cn.odboy.core.dal.dataobject.system.SystemDictTb;
import cn.odboy.core.dal.mysql.system.SystemDictDetailMapper;
import cn.odboy.core.dal.mysql.system.SystemDictMapper;
import cn.odboy.core.dal.model.system.CreateSystemDictDetailArgs;
import cn.odboy.redis.RedisHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemDictDetailServiceImpl extends ServiceImpl<SystemDictDetailMapper, SystemDictDetailTb> implements SystemDictDetailService {
    private final SystemDictMapper dictMapper;
    private final RedisHelper redisHelper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDictDetail(CreateSystemDictDetailArgs resources) {
        SystemDictDetailTb dictDetail = BeanUtil.copyProperties(resources, SystemDictDetailTb.class);
        dictDetail.setDictId(resources.getDict().getId());
        save(dictDetail);
        // 清理缓存
        delCaches(dictDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDictDetailById(SystemDictDetailTb resources) {
        SystemDictDetailTb dictDetail = getById(resources.getId());
        resources.setId(dictDetail.getId());
        // 更新数据
        saveOrUpdate(resources);
        // 清理缓存
        delCaches(dictDetail);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDictDetailById(Long id) {
        SystemDictDetailTb dictDetail = getById(id);
        removeById(id);
        // 清理缓存
        delCaches(dictDetail);
    }

    public void delCaches(SystemDictDetailTb dictDetail) {
        SystemDictTb dict = dictMapper.selectById(dictDetail.getDictId());
        redisHelper.del(SystemRedisKey.DICT_NAME + dict.getName());
    }
}
