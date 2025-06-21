package cn.odboy.core.service.system;

import cn.hutool.core.bean.BeanUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemJobTb;
import cn.odboy.core.dal.model.system.CreateSystemJobArgs;
import cn.odboy.core.dal.model.system.QuerySystemJobArgs;
import cn.odboy.core.dal.mysql.system.SystemJobMapper;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.exception.BadRequestException;
import cn.odboy.exception.EntityExistException;
import cn.odboy.util.FileUtil;
import cn.odboy.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SystemJobServiceImpl extends ServiceImpl<SystemJobMapper, SystemJobTb> implements SystemJobService {
    private final SystemJobMapper systemJobMapper;
    private final SystemUserMapper systemUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveJob(CreateSystemJobArgs resources) {
        SystemJobTb job = systemJobMapper.getJobByName(resources.getName());
        if (job != null) {
            throw new EntityExistException(SystemJobTb.class, "name", resources.getName());
        }
        save(BeanUtil.copyProperties(resources, SystemJobTb.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyJobById(SystemJobTb resources) {
        SystemJobTb job = getById(resources.getId());
        SystemJobTb old = systemJobMapper.getJobByName(resources.getName());
        if (old != null && !old.getId().equals(resources.getId())) {
            throw new EntityExistException(SystemJobTb.class, "name", resources.getName());
        }
        resources.setId(job.getId());
        saveOrUpdate(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeJobByIds(Set<Long> ids) {
        removeBatchByIds(ids);
    }

    @Override
    public void downloadJobExcel(List<SystemJobTb> jobs, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemJobTb job : jobs) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("岗位名称", job.getName());
            map.put("岗位状态", job.getEnabled() ? "启用" : "停用");
            map.put("创建日期", job.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


    @Override
    public CsResultVo<List<SystemJobTb>> describeJobPage(QuerySystemJobArgs criteria, Page<SystemJobTb> page) {
        return PageUtil.toPage(systemJobMapper.queryJobPageByArgs(criteria, page));
    }

    @Override
    public List<SystemJobTb> describeJobList(QuerySystemJobArgs criteria) {
        return systemJobMapper.queryJobPageByArgs(criteria, PageUtil.getCount(systemJobMapper)).getRecords();
    }

    @Override
    public SystemJobTb describeJobById(Long id) {
        return systemJobMapper.selectById(id);
    }


    @Override
    public void verifyBindRelationByIds(Set<Long> ids) {
        if (systemUserMapper.getUserCountByJobIds(ids) > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }
}
