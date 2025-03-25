package cn.odboy.modules.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.PageResult;
import cn.odboy.constant.CacheKey;
import cn.odboy.model.system.domain.Dict;
import cn.odboy.model.system.domain.DictDetail;
import cn.odboy.model.system.dto.DictQueryCriteria;
import cn.odboy.modules.system.mapper.DictDetailMapper;
import cn.odboy.modules.system.mapper.DictMapper;
import cn.odboy.modules.system.service.DictService;
import cn.odboy.util.FileUtil;
import cn.odboy.util.PageUtil;
import cn.odboy.util.RedisUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
    private final DictMapper dictMapper;
    private final DictDetailMapper dictDetailMapper;
    private final RedisUtil redisUtil;

    @Override
    public PageResult<Dict> queryAll(DictQueryCriteria criteria, Page<Object> page) {
        IPage<Dict> dicts = dictMapper.selectByPage(criteria, page);
        return PageUtil.toPage(dicts);
    }

    @Override
    public List<Dict> queryAll(DictQueryCriteria criteria) {
        return dictMapper.selectByPage(criteria);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Dict resources) {
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Dict resources) {
        // 清理缓存
        delCaches(resources);
        Dict dict = getById(resources.getId());
        dict.setName(resources.getName());
        dict.setDescription(resources.getDescription());
        saveOrUpdate(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        // 清理缓存
        List<Dict> dicts = dictMapper.selectBatchIds(ids);
        for (Dict dict : dicts) {
            delCaches(dict);
        }
        // 删除字典
        dictMapper.deleteBatchIds(ids);
        // 删除字典详情
        dictDetailMapper.deleteBatchByDictIds(ids);
    }

    @Override
    public void download(List<Dict> dicts, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Dict dict : dicts) {
            List<DictDetail> dictDetails = dictDetailMapper.selectByDictName(dict.getName());
            if (CollectionUtil.isNotEmpty(dictDetails)) {
                for (DictDetail dictDetail : dictDetails) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("字典名称", dict.getName());
                    map.put("字典描述", dict.getDescription());
                    map.put("字典标签", dictDetail.getLabel());
                    map.put("字典值", dictDetail.getValue());
                    map.put("创建日期", dictDetail.getCreateTime());
                    list.add(map);
                }
            } else {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("字典名称", dict.getName());
                map.put("字典描述", dict.getDescription());
                map.put("字典标签", null);
                map.put("字典值", null);
                map.put("创建日期", dict.getCreateTime());
                list.add(map);
            }
        }
        FileUtil.downloadExcel(list, response);
    }

    public void delCaches(Dict dict) {
        redisUtil.del(CacheKey.DICT_NAME + dict.getName());
    }
}