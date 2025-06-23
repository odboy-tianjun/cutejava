package cn.odboy.core.service.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemDictDetailTb;
import cn.odboy.core.dal.dataobject.system.SystemDictTb;
import cn.odboy.core.dal.model.system.CreateSystemDictArgs;
import cn.odboy.core.dal.model.system.QuerySystemDictArgs;
import cn.odboy.core.dal.model.system.QuerySystemDictDetailArgs;
import cn.odboy.core.dal.mysql.system.SystemDictDetailMapper;
import cn.odboy.core.dal.mysql.system.SystemDictMapper;
import cn.odboy.util.CsPageUtil;
import cn.odboy.util.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SystemDictService {
    private final SystemDictMapper systemDictMapper;
    private final SystemDictDetailMapper systemDictDetailMapper;

    /**
     * 创建
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void saveDict(CreateSystemDictArgs resources) {
        systemDictMapper.insert(BeanUtil.copyProperties(resources, SystemDictTb.class));
    }

    /**
     * 编辑
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyDictById(SystemDictTb resources) {
        SystemDictTb dict = systemDictMapper.selectById(resources.getId());
        dict.setName(resources.getName());
        dict.setDescription(resources.getDescription());
        systemDictMapper.insertOrUpdate(dict);
    }

    /**
     * 删除
     *
     * @param ids /
     */

    @Transactional(rollbackFor = Exception.class)
    public void removeDictByIds(Set<Long> ids) {
        // 删除字典
        systemDictMapper.deleteByIds(ids);
        // 删除字典详情
        systemDictDetailMapper.deleteDictDetailByDictIds(ids);
    }

    /**
     * 导出数据
     *
     * @param dicts    待导出的数据
     * @param response /
     * @throws IOException /
     */

    public void exportDictExcel(List<SystemDictTb> dicts, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemDictTb dict : dicts) {
            QuerySystemDictDetailArgs criteria = new QuerySystemDictDetailArgs();
            criteria.setDictName(dict.getName());
            List<SystemDictDetailTb> dictDetails = systemDictDetailMapper.selectDictDetailByArgs(criteria);
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

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */

    public CsResultVo<List<SystemDictTb>> queryDictByArgs(QuerySystemDictArgs criteria, Page<SystemDictTb> page) {
        return CsPageUtil.toPage(systemDictMapper.selectDictByArgs(criteria, page));
    }

    /**
     * 查询全部数据
     *
     * @param criteria /
     * @return /
     */

    public List<SystemDictTb> queryDictByArgs(QuerySystemDictArgs criteria) {
        return systemDictMapper.selectDictByArgs(criteria);
    }

    public SystemDictTb getById(int id) {
        return systemDictMapper.selectById(id);
    }
}
