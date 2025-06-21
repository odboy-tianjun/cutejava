package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemQuartzJobTb;
import cn.odboy.core.dal.dataobject.system.SystemQuartzLogTb;
import cn.odboy.core.dal.model.system.QuerySystemQuartzJobArgs;
import cn.odboy.core.dal.model.system.UpdateSystemQuartzJobArgs;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;


public interface SystemQuartzJobService extends IService<SystemQuartzJobTb> {
    /**
     * 创建
     *
     * @param resources /
     */
    void createJob(SystemQuartzJobTb resources);

    /**
     * 修改任务并重新调度
     *
     * @param resources /
     */
    void modifyQuartzJobResumeCron(UpdateSystemQuartzJobArgs resources);

    /**
     * 删除任务
     *
     * @param ids /
     */
    void removeJobByIds(Set<Long> ids);

    /**
     * 更改定时任务状态
     *
     * @param quartzJob /
     */
    void switchQuartzJobStatus(SystemQuartzJobTb quartzJob);

    /**
     * 立即执行定时任务
     *
     * @param quartzJob /
     */
    void startQuartzJob(SystemQuartzJobTb quartzJob);

    /**
     * 导出定时任务
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws IOException /
     */
    void downloadQuartzJobExcel(List<SystemQuartzJobTb> queryAll, HttpServletResponse response) throws IOException;

    /**
     * 导出定时任务日志
     *
     * @param queryAllLog 待导出的数据
     * @param response    /
     * @throws IOException /
     */
    void downloadQuartzLogExcel(List<SystemQuartzLogTb> queryAllLog, HttpServletResponse response) throws IOException;

    /**
     * 执行子任务
     *
     * @param tasks /
     * @throws InterruptedException /
     */
    void startSubQuartJob(String[] tasks) throws InterruptedException;

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemQuartzJobTb>> describeQuartzJobPage(QuerySystemQuartzJobArgs criteria, Page<SystemQuartzJobTb> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemQuartzJobTb> describeQuartzJobList(QuerySystemQuartzJobArgs criteria);

    /**
     * 分页查询日志
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    CsResultVo<List<SystemQuartzLogTb>> describeQuartzLogPage(QuerySystemQuartzJobArgs criteria, Page<SystemQuartzLogTb> page);

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    List<SystemQuartzLogTb> describeQuartzLogList(QuerySystemQuartzJobArgs criteria);
}
