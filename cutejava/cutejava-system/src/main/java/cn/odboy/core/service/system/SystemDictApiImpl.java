package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemDictTb;
import cn.odboy.core.dal.mysql.system.SystemDictMapper;
import cn.odboy.core.dal.model.system.QuerySystemDictArgs;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemDictApiImpl implements SystemDictApi {
    private final SystemDictMapper dictMapper;

    @Override
    public CsResultVo<List<SystemDictTb>> describeDictPage(QuerySystemDictArgs criteria, Page<SystemDictTb> page) {
        IPage<SystemDictTb> dicts = dictMapper.queryDictPageByArgs(criteria, page);
        return PageUtil.toPage(dicts);
    }

    @Override
    public List<SystemDictTb> describeDictList(QuerySystemDictArgs criteria) {
        return dictMapper.queryDictPageByArgs(criteria, PageUtil.getCount(dictMapper)).getRecords();
    }
}
