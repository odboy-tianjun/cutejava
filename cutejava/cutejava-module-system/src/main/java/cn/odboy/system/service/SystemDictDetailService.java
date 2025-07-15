package cn.odboy.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.model.SystemCreateDictDetailArgs;
import cn.odboy.system.dal.model.SystemQueryDictDetailArgs;
import cn.odboy.system.dal.mysql.SystemDictDetailMapper;
import cn.odboy.util.CsPageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemDictDetailService {
    private final SystemDictDetailMapper systemDictDetailMapper;

    /**
     * 创建
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void saveDictDetail(SystemCreateDictDetailArgs resources) {
        SystemDictDetailTb dictDetail = BeanUtil.copyProperties(resources, SystemDictDetailTb.class);
        dictDetail.setDictId(resources.getDict().getId());
        systemDictDetailMapper.insert(dictDetail);
    }

    /**
     * 编辑
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyDictDetailById(SystemDictDetailTb resources) {
        SystemDictDetailTb dictDetail = systemDictDetailMapper.selectById(resources.getId());
        resources.setId(dictDetail.getId());
        systemDictDetailMapper.insertOrUpdate(resources);
    }

    /**
     * 删除
     *
     * @param id /
     */

    @Transactional(rollbackFor = Exception.class)
    public void removeDictDetailById(Long id) {
        systemDictDetailMapper.deleteById(id);
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */

    public CsResultVo<List<SystemDictDetailTb>> queryDictDetailByArgs(SystemQueryDictDetailArgs criteria, Page<Object> page) {
        return CsPageUtil.toPage(systemDictDetailMapper.selectDictDetailByArgs(criteria, page));
    }

    /**
     * 根据字典名称获取字典详情
     *
     * @param name 字典名称
     * @return /
     */

    public List<SystemDictDetailTb> queryDictDetailByName(String name) {
        SystemQueryDictDetailArgs criteria = new SystemQueryDictDetailArgs();
        criteria.setDictName(name);
        return systemDictDetailMapper.selectDictDetailByArgs(criteria);
    }
}
