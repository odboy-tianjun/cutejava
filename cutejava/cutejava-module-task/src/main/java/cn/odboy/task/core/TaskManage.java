/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.odboy.task.core;

import cn.odboy.framework.exception.web.BadRequestException;
import cn.odboy.task.constant.TaskChangeTypeEnum;
import cn.odboy.task.constant.TaskStatusEnum;
import cn.odboy.task.dal.dataobject.TaskInstanceInfoTb;
import cn.odboy.task.service.TaskInstanceInfoService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Component
public class TaskManage {
    @Resource
    private Scheduler scheduler;
    @Autowired
    private TaskInstanceInfoService taskInstanceInfoService;

    /**
     * 创建任务单
     *
     * @param contextName 上下文名称，这里特指应用名
     * @param changeType  变更类型
     * @param envAlias    环境别名
     * @param source      来源
     * @param reason      变更原因
     * @param dataMap     任务参数
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskInstanceInfoTb createJob(String contextName, TaskChangeTypeEnum changeType, String language, String envAlias, String source, String reason, JobDataMap dataMap) {
        if (dataMap == null) {
            dataMap = new JobDataMap();
        }
        // ========================== 传递参数 ==========================
        // 应用名、资源类型
        dataMap.put("contextName", contextName);
        // 开发语言、资源版本
        dataMap.put("language", language);
        dataMap.put("envAlias", envAlias);
        // ========================== 创建任务 ==========================
        TaskInstanceInfoTb newInstance = new TaskInstanceInfoTb();
        newInstance.setContextName(contextName);
        newInstance.setChangeType(changeType.getCode());
        newInstance.setEnvAlias(envAlias);
        newInstance.setStatus(TaskStatusEnum.Running.getCode());
        newInstance.setFinishTime(null);
        newInstance.setSource(source);
        newInstance.setReason(reason);
        newInstance.setJobData(JSON.toJSONString(dataMap));
        taskInstanceInfoService.save(newInstance);
        dataMap.put("id", newInstance.getId());
        // ========================== 执行任务 ==========================
        JobKey jobKey = JobKey.jobKey(changeType.getCode(), contextName);
        TriggerKey triggerKey = TriggerKey.triggerKey(changeType.getCode(), contextName);
        JobDetail jobDetail = JobBuilder.newJob(TaskJobBean.class).withIdentity(jobKey).usingJobData(dataMap).build();
        Trigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().build();
        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (ObjectAlreadyExistsException e) {
            throw new BadRequestException("任务已存在，跳过加载");
        } catch (SchedulerException e) {
            log.error("任务执行失败", e);
            throw new BadRequestException(e);
        }
        return newInstance;
    }

    @Transactional(rollbackFor = Exception.class)
    public void stopJob(Long instanceId) {
        TaskInstanceInfoTb taskInstanceInfoTb = taskInstanceInfoService.getRunningById(instanceId);

        String changeType = taskInstanceInfoTb.getChangeType();
        String contextName = taskInstanceInfoTb.getContextName();

        taskInstanceInfoTb.setStatus(TaskStatusEnum.Fail.getCode());
        taskInstanceInfoTb.setErrorMessage("任务被中断");
        taskInstanceInfoTb.setFinishTime(new Date());
        taskInstanceInfoService.updateById(taskInstanceInfoTb);

        try {
            JobKey jobKey = JobKey.jobKey(changeType, contextName);
            TriggerKey triggerKey = TriggerKey.triggerKey(changeType, contextName);
            // 停止触发器
            scheduler.resumeTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除并中断任务
            scheduler.deleteJob(jobKey);
            scheduler.interrupt(jobKey);
        } catch (SchedulerException e) {
            log.error("任务停止失败", e);
            throw new RuntimeException(e);
        }
    }
}
