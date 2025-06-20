package cn.odboy.core.service.system;

import cn.odboy.core.dal.dataobject.system.SystemQuartzJobTb;
import cn.odboy.core.dal.dataobject.system.SystemQuartzLogTb;
import cn.odboy.core.dal.model.system.UpdateSystemQuartzJobArgs;
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
}
