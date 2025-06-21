package cn.odboy.core.service.system;

import cn.hutool.core.bean.BeanUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemDictDetailTb;
import cn.odboy.core.dal.model.system.CreateSystemDictDetailArgs;
import cn.odboy.core.dal.model.system.QuerySystemDictDetailArgs;
import cn.odboy.core.dal.mysql.system.SystemDictDetailMapper;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemDictDetailServiceImpl extends ServiceImpl<SystemDictDetailMapper, SystemDictDetailTb> implements SystemDictDetailService {
    private final SystemDictDetailMapper systemDictDetailMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDictDetail(CreateSystemDictDetailArgs resources) {
        SystemDictDetailTb dictDetail = BeanUtil.copyProperties(resources, SystemDictDetailTb.class);
        dictDetail.setDictId(resources.getDict().getId());
        save(dictDetail);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyDictDetailById(SystemDictDetailTb resources) {
        SystemDictDetailTb dictDetail = getById(resources.getId());
        resources.setId(dictDetail.getId());
        // 更新数据
        saveOrUpdate(resources);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeDictDetailById(Long id) {
        removeById(id);
    }


    @Override
    public CsResultVo<List<SystemDictDetailTb>> queryDictDetailListByArgs(QuerySystemDictDetailArgs criteria, Page<Object> page) {
        return PageUtil.toPage(systemDictDetailMapper.queryDictDetailListByArgs(criteria, page));
    }

    @Override
    public List<SystemDictDetailTb> queryDictDetailListByName(String name) {
        return systemDictDetailMapper.queryDictDetailListByDictName(name);
    }
}
