package cn.odboy.application.system.service;

import cn.odboy.base.PageResult;
import cn.odboy.model.system.domain.Job;
import cn.odboy.model.system.dto.JobQueryCriteria;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface JobService extends IService<Job> {

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    Job getJobById(Long id);

    /**
     * 创建
     *
     * @param resources /
     */
    void saveJob(Job resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void updateJobById(Job resources);

    /**
     * 删除
     *
     * @param ids /
     */
    void deleteJobByIds(Set<Long> ids);

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<Job> queryJobPage(JobQueryCriteria criteria, Page<Object> page);

    /**
     * 查询全部数据
     *
     * @param criteria /
     * @return /
     */
    List<Job> selectJobByCriteria(JobQueryCriteria criteria);

    /**
     * 导出数据
     *
     * @param jobs     待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadExcel(List<Job> jobs, HttpServletResponse response) throws IOException;

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */
    void verifyBindRelationByIds(Set<Long> ids);
}