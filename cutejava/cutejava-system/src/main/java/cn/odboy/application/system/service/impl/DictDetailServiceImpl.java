package cn.odboy.application.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.odboy.application.system.mapper.DictDetailMapper;
import cn.odboy.application.system.mapper.DictMapper;
import cn.odboy.application.system.service.DictDetailService;
import cn.odboy.base.PageResult;
import cn.odboy.constant.SystemRedisKey;
import cn.odboy.model.system.domain.Dict;
import cn.odboy.model.system.domain.DictDetail;
import cn.odboy.model.system.request.CreateDictDetailRequest;
import cn.odboy.model.system.request.QueryDictDetailRequest;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DictDetailServiceImpl extends ServiceImpl<DictDetailMapper, DictDetail> implements DictDetailService {
    private final DictMapper dictMapper;
    private final DictDetailMapper dictDetailMapper;
    private final RedisHelper redisHelper;

    @Override
    public PageResult<DictDetail> describeDictDetailPage(QueryDictDetailRequest criteria, Page<Object> page) {
        return PageUtil.toPage(dictDetailMapper.queryDictDetailPageByArgs(criteria, page));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDictDetail(CreateDictDetailRequest resources) {
        DictDetail dictDetail = BeanUtil.copyProperties(resources, DictDetail.class);
        dictDetail.setDictId(resources.getDict().getId());
        save(dictDetail);
        // 清理缓存
        delCaches(dictDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDictDetailById(DictDetail resources) {
        DictDetail dictDetail = getById(resources.getId());
        resources.setId(dictDetail.getId());
        // 更新数据
        saveOrUpdate(resources);
        // 清理缓存
        delCaches(dictDetail);
    }

    @Override
    public List<DictDetail> describeDictDetailListByName(String name) {
        String key = SystemRedisKey.DICT_NAME + name;
        List<DictDetail> dictDetails = redisHelper.getList(key, DictDetail.class);
        if (CollUtil.isEmpty(dictDetails)) {
            dictDetails = dictDetailMapper.queryDictDetailListByDictName(name);
            redisHelper.set(key, dictDetails, 1, TimeUnit.DAYS);
        }
        return dictDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictDetailById(Long id) {
        DictDetail dictDetail = getById(id);
        removeById(id);
        // 清理缓存
        delCaches(dictDetail);
    }

    public void delCaches(DictDetail dictDetail) {
        Dict dict = dictMapper.selectById(dictDetail.getDictId());
        redisHelper.del(SystemRedisKey.DICT_NAME + dict.getName());
    }
}