package cn.odboy.devops.framework.pipeline;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.devops.dal.dataobject.pipeline.PipelineInstanceTb;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class PipelineJobManage {
    @Resource
    private Scheduler scheduler;

    /**
     * 启动 job
     */
    public void startJob(PipelineInstanceTb pipelineInstance) {
        try {
            // 构建 JobDetail
            JobDetail jobDetail = JobBuilder
                    .newJob(PipelineJobBean.class)
                    .withIdentity(pipelineInstance.getPipelineInstanceId())
                    .build();
            // 构建Trigger
            Trigger cronTrigger = TriggerBuilder.newTrigger()
                    .withIdentity(pipelineInstance.getPipelineInstanceId())
                    .startNow()
                    .build();
            // 添加流水线实例
            cronTrigger.getJobDataMap().put(PipelineConst.INSTANCE_ID_KEY, pipelineInstance.getPipelineInstanceId());
            try {
                scheduler.scheduleJob(jobDetail, cronTrigger);
            } catch (ObjectAlreadyExistsException e) {
                log.warn("定时任务已存在，跳过加载");
            }
        } catch (Exception e) {
            log.error("创建定时任务失败", e);
            throw new BadRequestException("创建定时任务失败");
        }
    }

    /**
     * 删除job
     */
    public void deleteJob(PipelineInstanceTb pipelineInstance) {
        try {
            JobKey jobKey = JobKey.jobKey(pipelineInstance.getPipelineInstanceId());
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            log.error("删除定时任务失败", e);
            throw new BadRequestException("删除定时任务失败");
        }
    }
}
