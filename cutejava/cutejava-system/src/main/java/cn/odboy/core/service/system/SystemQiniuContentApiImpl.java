package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemQiniuContentTb;
import cn.odboy.core.dal.mysql.system.SystemQiniuContentMapper;
import cn.odboy.core.dal.model.system.QuerySystemQiniuArgs;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "qiNiu")
public class SystemQiniuContentApiImpl implements SystemQiniuContentApi {
    private final SystemQiniuContentMapper qiniuContentMapper;

    @Override
    public CsResultVo<List<SystemQiniuContentTb>> describeQiniuContentPage(QuerySystemQiniuArgs criteria, Page<SystemQiniuContentTb> page) {
        return PageUtil.toPage(qiniuContentMapper.queryQiniuContentPageByArgs(criteria, page));
    }

    @Override
    public List<SystemQiniuContentTb> describeQiniuContentList(QuerySystemQiniuArgs criteria) {
        return qiniuContentMapper.queryQiniuContentPageByArgs(criteria, PageUtil.getCount(qiniuContentMapper)).getRecords();
    }
}
