package cn.odboy.core.service.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.model.system.QuerySystemDictArgs;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.SystemDictDetailTb;
import cn.odboy.core.dal.dataobject.system.SystemDictTb;
import cn.odboy.core.dal.mysql.system.SystemDictDetailMapper;
import cn.odboy.core.dal.mysql.system.SystemDictMapper;
import cn.odboy.core.dal.model.system.CreateSystemDictArgs;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.FileUtil;
import cn.odboy.util.PageUtil;
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
public class SystemDictServiceImpl extends ServiceImpl<SystemDictMapper, SystemDictTb> implements SystemDictService {
    private final SystemDictMapper systemDictMapper;
    private final SystemDictDetailMapper systemDictDetailMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDict(CreateSystemDictArgs resources) {
        save(BeanUtil.copyProperties(resources, SystemDictTb.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDictById(SystemDictTb resources) {
        SystemDictTb dict = getById(resources.getId());
        dict.setName(resources.getName());
        dict.setDescription(resources.getDescription());
        saveOrUpdate(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDictByIds(Set<Long> ids) {
        // 删除字典
        systemDictMapper.deleteByIds(ids);
        // 删除字典详情
        systemDictDetailMapper.deleteDictDetailByDictIds(ids);
    }

    @Override
    public void downloadDictExcel(List<SystemDictTb> dicts, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemDictTb dict : dicts) {
            List<SystemDictDetailTb> dictDetails = systemDictDetailMapper.queryDictDetailListByDictName(dict.getName());
            if (CollectionUtil.isNotEmpty(dictDetails)) {
                for (SystemDictDetailTb dictDetail : dictDetails) {
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

    @Override
    public CsResultVo<List<SystemDictTb>> queryDictPage(QuerySystemDictArgs criteria, Page<SystemDictTb> page) {
        IPage<SystemDictTb> dicts = systemDictMapper.queryDictPageByArgs(criteria, page);
        return PageUtil.toPage(dicts);
    }

    @Override
    public List<SystemDictTb> queryDictList(QuerySystemDictArgs criteria) {
        return systemDictMapper.queryDictPageByArgs(criteria, PageUtil.getCount(systemDictMapper)).getRecords();
    }
}
