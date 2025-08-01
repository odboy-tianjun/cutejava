/*
 *  Copyright 2021-2025 Odboy
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cn.odboy.devops.framework.pipeline.core;

import cn.hutool.core.util.IdUtil;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.framework.pipeline.constant.PipelineConst;
import cn.odboy.framework.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 流水线任务管理器
 *
 * @author odboy
 * @date 2025-07-19
 */
@Slf4j
@Component
public class PipelineJobManage {
    private static final String JOB_KEY = "PIPELINE_JOB_%s";
    @Resource
    private Scheduler scheduler;

    /**
     * 启动 job
     */
    public PipelineInstanceTb startJob(PipelineInstanceTb pipelineInstance) {
        // 流水线Id(雪花)
        Long instanceId = IdUtil.getSnowflakeNextId();
        try {
            pipelineInstance.setInstanceId(instanceId);
            String keyName = String.format(JOB_KEY, instanceId);
            // 构建 JobDetail
            JobKey jobKey = JobKey.jobKey(keyName);
            JobDetail jobDetail = JobBuilder.newJob(PipelineJobBean.class).withIdentity(jobKey).build();
            // 构建Trigger
            TriggerKey triggerKey = TriggerKey.triggerKey(keyName);
            Trigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().build();
            // 任务参数
            JobDataMap jobDataMap = cronTrigger.getJobDataMap();
            jobDataMap.put(PipelineConst.INSTANCE, pipelineInstance);
            jobDataMap.put(PipelineConst.RETRY_NODE_CODE, "");
            try {
                // 在quartz的内部线程池中异步执行
                scheduler.scheduleJob(jobDetail, cronTrigger);
            } catch (ObjectAlreadyExistsException e) {
                log.warn("定时任务已存在，跳过加载");
            }
        } catch (Exception e) {
            log.error("创建定时任务失败", e);
            throw new BadRequestException("创建定时任务失败");
        }
        return pipelineInstance;
    }

    /**
     * 删除job
     */
    public void deleteJob(Long instanceId) {
        try {
            JobKey jobKey = JobKey.jobKey(String.format(JOB_KEY, instanceId));
            scheduler.pauseJob(jobKey);
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            log.error("删除定时任务失败", e);
            throw new BadRequestException("删除定时任务失败");
        }
    }

    /**
     * 中断正在执行的任务<br/> 任务终止需要配合响应中断或停止信号
     */
    public void interruptJob(Long instanceId) throws SchedulerException {
        JobKey jobKey = JobKey.jobKey(String.format(JOB_KEY, instanceId));
        scheduler.interrupt(jobKey);
    }

    public PipelineInstanceTb startJobByNodeCode(PipelineInstanceTb pipelineInstance, String retryNodeCode) {
        try {
            Long instanceId = pipelineInstance.getInstanceId();
            String keyName = String.format(JOB_KEY, instanceId);
            // 构建 JobDetail
            JobKey jobKey = JobKey.jobKey(keyName);
            JobDetail jobDetail = JobBuilder.newJob(PipelineJobBean.class).withIdentity(jobKey).build();
            // 构建Trigger
            TriggerKey triggerKey = TriggerKey.triggerKey(keyName);
            Trigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().build();
            // 任务参数
            JobDataMap jobDataMap = cronTrigger.getJobDataMap();
            jobDataMap.put(PipelineConst.INSTANCE, pipelineInstance);
            jobDataMap.put(PipelineConst.RETRY_NODE_CODE, retryNodeCode);
            try {
                // 在quartz的内部线程池中异步执行
                scheduler.scheduleJob(jobDetail, cronTrigger);
            } catch (ObjectAlreadyExistsException e) {
                log.warn("定时任务已存在，跳过加载");
            }
        } catch (Exception e) {
            log.error("创建定时任务失败", e);
            throw new BadRequestException("创建定时任务失败");
        }
        return pipelineInstance;
    }
}
