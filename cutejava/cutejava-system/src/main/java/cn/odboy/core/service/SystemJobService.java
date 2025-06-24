package cn.odboy.core.service;

import cn.hutool.core.bean.BeanUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.SystemJobTb;
import cn.odboy.core.dal.model.CreateSystemJobArgs;
import cn.odboy.core.dal.model.QuerySystemJobArgs;
import cn.odboy.core.dal.mysql.SystemJobMapper;
import cn.odboy.core.dal.mysql.SystemUserMapper;
import cn.odboy.exception.BadRequestException;
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
public class SystemJobService {
    private final SystemJobMapper systemJobMapper;
    private final SystemUserMapper systemUserMapper;

    /**
     * 创建
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void saveJob(CreateSystemJobArgs resources) {
        SystemJobTb job = systemJobMapper.getJobByName(resources.getName());
        if (job != null) {
            throw new BadRequestException("职位名称已存在");
        }
        systemJobMapper.insert(BeanUtil.copyProperties(resources, SystemJobTb.class));
    }

    /**
     * 编辑
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyJobById(SystemJobTb resources) {
        SystemJobTb job = systemJobMapper.selectById(resources.getId());
        SystemJobTb old = systemJobMapper.getJobByName(resources.getName());
        if (old != null && !old.getId().equals(resources.getId())) {
            throw new BadRequestException("职位名称已存在");
        }
        resources.setId(job.getId());
        systemJobMapper.insertOrUpdate(resources);
    }

    /**
     * 删除
     *
     * @param ids /
     */

    @Transactional(rollbackFor = Exception.class)
    public void removeJobByIds(Set<Long> ids) {
        systemJobMapper.deleteByIds(ids);
    }

    /**
     * 导出数据
     *
     * @param jobs     待导出的数据
     * @param response /
     * @throws IOException
     */

    public void exportJobExcel(List<SystemJobTb> jobs, HttpServletResponse response) throws IOException {
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

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return
     */


    public CsResultVo<List<SystemJobTb>> queryJobByArgs(QuerySystemJobArgs criteria, Page<SystemJobTb> page) {
        return CsPageUtil.toPage(systemJobMapper.selectJobByArgs(criteria, page));
    }

    /**
     * 查询全部数据
     *
     * @param criteria /
     * @return /
     */

    public List<SystemJobTb> queryJobByArgs(QuerySystemJobArgs criteria) {
        return systemJobMapper.selectJobByArgs(criteria, CsPageUtil.getCount(systemJobMapper)).getRecords();
    }

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */

    public void verifyBindRelationByIds(Set<Long> ids) {
        if (systemUserMapper.countUserByJobIds(ids) > 0) {
            throw new BadRequestException("所选的岗位中存在用户关联，请解除关联再试！");
        }
    }
}
