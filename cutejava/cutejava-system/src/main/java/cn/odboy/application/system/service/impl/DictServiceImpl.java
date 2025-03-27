package cn.odboy.application.system.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.application.system.mapper.DictDetailMapper;
import cn.odboy.application.system.mapper.DictMapper;
import cn.odboy.application.system.service.DictService;
import cn.odboy.base.PageResult;
import cn.odboy.constant.SystemRedisKey;
import cn.odboy.model.system.domain.Dict;
import cn.odboy.model.system.domain.DictDetail;
import cn.odboy.model.system.dto.DictQueryCriteria;
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
    public PageResult<Dict> queryDictPage(DictQueryCriteria criteria, Page<Object> page) {
        IPage<Dict> dicts = dictMapper.queryDictPage(criteria, page);
        return PageUtil.toPage(dicts);
    }

    @Override
    public List<Dict> selectDictByCriteria(DictQueryCriteria criteria) {
        return dictMapper.queryDictPage(criteria, PageUtil.getCount(dictMapper)).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDict(Dict resources) {
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictById(Dict resources) {
        // 清理缓存
        delCaches(resources);
        Dict dict = getById(resources.getId());
        dict.setName(resources.getName());
        dict.setDescription(resources.getDescription());
        saveOrUpdate(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictByIds(Set<Long> ids) {
        // 清理缓存
        List<Dict> dicts = dictMapper.selectByIds(ids);
        for (Dict dict : dicts) {
            delCaches(dict);
        }
        // 删除字典
        dictMapper.deleteByIds(ids);
        // 删除字典详情
        dictDetailMapper.deleteDictDetailsByDictIds(ids);
    }

    @Override
    public void downloadExcel(List<Dict> dicts, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Dict dict : dicts) {
            List<DictDetail> dictDetails = dictDetailMapper.selectDictDetailByDictName(dict.getName());
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
        redisUtil.del(SystemRedisKey.DICT_NAME + dict.getName());
    }
}