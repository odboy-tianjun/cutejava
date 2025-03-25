package cn.odboy.modules.system.service.impl;

import cn.odboy.base.PageResult;
import cn.odboy.constant.CacheKey;
import cn.odboy.exception.BadRequestException;
import cn.odboy.exception.EntityExistException;
import cn.odboy.model.system.domain.Job;
import cn.odboy.model.system.dto.JobQueryCriteria;
import cn.odboy.modules.system.mapper.JobMapper;
import cn.odboy.modules.system.mapper.UserMapper;
import cn.odboy.modules.system.service.JobService;
import cn.odboy.util.FileUtil;
import cn.odboy.util.PageUtil;
import cn.odboy.util.RedisUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements JobService {
    private final JobMapper jobMapper;
    private final UserMapper userMapper;
    private final RedisUtil redisUtil;

    @Override
    public PageResult<Job> queryAll(JobQueryCriteria criteria, Page<Object> page) {
        return PageUtil.toPage(jobMapper.findAll(criteria, page));
    }

    @Override
    public List<Job> queryAll(JobQueryCriteria criteria) {
        return jobMapper.findAll(criteria);
    }

    @Override
    public Job findById(Long id) {
        String key = CacheKey.JOB_ID + id;
        Job job = redisUtil.get(key, Job.class);
        if (job == null) {
            job = getById(id);
            redisUtil.set(key, job, 1, TimeUnit.DAYS);
        }
        return job;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Job resources) {
        Job job = jobMapper.findByName(resources.getName());
        if (job != null) {
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Job resources) {
        Job job = getById(resources.getId());
        Job old = jobMapper.findByName(resources.getName());
        if (old != null && !old.getId().equals(resources.getId())) {
            throw new EntityExistException(Job.class, "name", resources.getName());
        }
        resources.setId(job.getId());
        saveOrUpdate(resources);
        // 删除缓存
        delCaches(resources.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        removeBatchByIds(ids);
        // 删除缓存
        ids.forEach(this::delCaches);
    }

    @Override
    public void download(List<Job> jobs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Job job : jobs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("岗位名称", job.getName());
            map.put("岗位状态", job.getEnabled() ? "启用" : "停用");
            map.put("创建日期", job.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public void verification(Set<Long> ids) {
        if (userMapper.countByJobs(ids) > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }

    public void delCaches(Long id) {
        redisUtil.del(CacheKey.JOB_ID + id);
    }
}