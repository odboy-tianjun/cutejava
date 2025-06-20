package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemLocalStorageTb;
import cn.odboy.core.dal.mysql.system.SystemLocalStorageMapper;
import cn.odboy.core.dal.model.system.QuerySystemLocalStorageArgs;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemLocalStorageApiImpl implements SystemLocalStorageApi {
    private final SystemLocalStorageMapper localStorageMapper;

    @Override
    public CsResultVo<List<SystemLocalStorageTb>> describeLocalStoragePage(QuerySystemLocalStorageArgs criteria, Page<SystemLocalStorageTb> page) {
        return PageUtil.toPage(localStorageMapper.queryLocalStoragePageByArgs(criteria, page));
    }

    @Override
    public List<SystemLocalStorageTb> describeLocalStorageList(QuerySystemLocalStorageArgs criteria) {
        return localStorageMapper.queryLocalStoragePageByArgs(criteria, PageUtil.getCount(localStorageMapper)).getRecords();
    }
}
