package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.dataobject.system.SystemJobTb;
import cn.odboy.core.dal.mysql.system.SystemJobMapper;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.core.dal.model.system.QuerySystemJobArgs;
import cn.odboy.exception.BadRequestException;
import cn.odboy.redis.RedisHelper;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SystemJobApiImpl implements SystemJobApi {
    private final SystemJobMapper jobMapper;
    private final SystemUserMapper userMapper;
    private final RedisHelper redisHelper;

    @Override
    public CsResultVo<List<SystemJobTb>> describeJobPage(QuerySystemJobArgs criteria, Page<SystemJobTb> page) {
        return PageUtil.toPage(jobMapper.queryJobPageByArgs(criteria, page));
    }

    @Override
    public List<SystemJobTb> describeJobList(QuerySystemJobArgs criteria) {
        return jobMapper.queryJobPageByArgs(criteria, PageUtil.getCount(jobMapper)).getRecords();
    }

    @Override
    public SystemJobTb describeJobById(Long id) {
        String key = SystemRedisKey.JOB_ID + id;
        SystemJobTb job = redisHelper.get(key, SystemJobTb.class);
        if (job == null) {
            job = jobMapper.selectById(id);
            redisHelper.set(key, job, 1, TimeUnit.DAYS);
        }
        return job;
    }


    @Override
    public void verifyBindRelationByIds(Set<Long> ids) {
        if (userMapper.getUserCountByJobIds(ids) > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }
}
