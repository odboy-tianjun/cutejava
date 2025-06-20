package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemQuartzJobTb;
import cn.odboy.core.dal.dataobject.system.SystemQuartzLogTb;
import cn.odboy.core.dal.mysql.system.SystemQuartzJobMapper;
import cn.odboy.core.dal.mysql.system.SystemQuartzLogMapper;
import cn.odboy.core.dal.model.system.QuerySystemQuartzJobArgs;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemQuartzJobApiImpl implements SystemQuartzJobApi {
    private final SystemQuartzJobMapper quartzJobMapper;
    private final SystemQuartzLogMapper quartzLogMapper;

    @Override
    public CsResultVo<List<SystemQuartzJobTb>> describeQuartzJobPage(QuerySystemQuartzJobArgs criteria, Page<SystemQuartzJobTb> page) {
        return PageUtil.toPage(quartzJobMapper.queryQuartzJobPageByArgs(criteria, page));
    }

    @Override
    public CsResultVo<List<SystemQuartzLogTb>> describeQuartzLogPage(QuerySystemQuartzJobArgs criteria, Page<SystemQuartzLogTb> page) {
        return PageUtil.toPage(quartzLogMapper.queryQuartzLogPageByArgs(criteria, page));
    }

    @Override
    public List<SystemQuartzJobTb> describeQuartzJobList(QuerySystemQuartzJobArgs criteria) {
        return quartzJobMapper.queryQuartzJobPageByArgs(criteria, PageUtil.getCount(quartzJobMapper)).getRecords();
    }

    @Override
    public List<SystemQuartzLogTb> describeQuartzLogList(QuerySystemQuartzJobArgs criteria) {
        return quartzLogMapper.queryQuartzLogPageByArgs(criteria, PageUtil.getCount(quartzLogMapper)).getRecords();
    }
}
