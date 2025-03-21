package cn.odboy.modules.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.constant.CacheKey;
import cn.odboy.modules.system.domain.Dict;
import cn.odboy.modules.system.domain.DictDetail;
import cn.odboy.modules.system.domain.dto.DictDetailQueryCriteria;
import cn.odboy.modules.system.mapper.DictDetailMapper;
import cn.odboy.modules.system.mapper.DictMapper;
import cn.odboy.modules.system.service.DictDetailService;
import cn.odboy.util.PageResult;
import cn.odboy.util.PageUtil;
import cn.odboy.util.RedisUtil;
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
    private final RedisUtil redisUtil;

    @Override
    public PageResult<DictDetail> queryAll(DictDetailQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(dictDetailMapper.findAll(criteria, page));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(DictDetail resources) {
        resources.setDictId(resources.getDict().getId());
        save(resources);
        // 清理缓存
        delCaches(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetail resources) {
        DictDetail dictDetail = getById(resources.getId());
        resources.setId(dictDetail.getId());
        // 更新数据
        saveOrUpdate(resources);
        // 清理缓存
        delCaches(dictDetail);
    }

    @Override
    public List<DictDetail> getDictByName(String name) {
        String key = CacheKey.DICT_NAME + name;
        List<DictDetail> dictDetails = redisUtil.getList(key, DictDetail.class);
        if (CollUtil.isEmpty(dictDetails)) {
            dictDetails = dictDetailMapper.findByDictName(name);
            redisUtil.set(key, dictDetails, 1, TimeUnit.DAYS);
        }
        return dictDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DictDetail dictDetail = getById(id);
        removeById(id);
        // 清理缓存
        delCaches(dictDetail);
    }

    public void delCaches(DictDetail dictDetail) {
        Dict dict = dictMapper.selectById(dictDetail.getDictId());
        redisUtil.del(CacheKey.DICT_NAME + dict.getName());
    }
}