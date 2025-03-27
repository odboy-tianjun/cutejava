package cn.odboy.application.job.service;

import cn.odboy.base.PageResult;
import cn.odboy.model.job.domain.QuartzJob;
import cn.odboy.model.job.domain.QuartzLog;
import cn.odboy.model.job.dto.QuartzJobQueryCriteria;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface QuartzJobService extends IService<QuartzJob> {

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<QuartzJob> queryJobPage(QuartzJobQueryCriteria criteria, Page<Object> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<QuartzJob> selectJobByCriteria(QuartzJobQueryCriteria criteria);

    /**
     * 分页查询日志
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    PageResult<QuartzLog> queryLogPage(QuartzJobQueryCriteria criteria, Page<Object> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<QuartzLog> selectLogByCriteria(QuartzJobQueryCriteria criteria);

    /**
     * 创建
     *
     * @param resources /
     */
    void createJob(QuartzJob resources);

    /**
     * 编辑
     *
     * @param resources /
     */
    void updateJob(QuartzJob resources);

    /**
     * 删除任务
     *
     * @param ids /
     */
    void deleteJobByIds(Set<Long> ids);

    /**
     * 更改定时任务状态
     *
     * @param quartzJob /
     */
    void switchJobPauseStatus(QuartzJob quartzJob);

    /**
     * 立即执行定时任务
     *
     * @param quartzJob /
     */
    void executionJob(QuartzJob quartzJob);

    /**
     * 导出定时任务
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadJobExcel(List<QuartzJob> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 导出定时任务日志
     *
     * @param queryAllLog 待导出的数据
     * @param response    /
     * @throws IOException /
     */
    void downloadLogExcel(List<QuartzLog> queryAllLog, HttpServletResponse response) throws IOException;

    /**
     * 执行子任务
     *
     * @param tasks /
     * @throws InterruptedException /
     */
    void executionSubJob(String[] tasks) throws InterruptedException;
}
