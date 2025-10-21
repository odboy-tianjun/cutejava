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

import cn.hutool.core.bean.BeanUtil;
import cn.odboy.framework.exception.web.BadRequestException;
import cn.odboy.task.constant.TaskChangeTypeEnum;
import cn.odboy.task.constant.TaskStatusEnum;
import cn.odboy.task.dal.dataobject.TaskInstanceDetailTb;
import cn.odboy.task.dal.dataobject.TaskInstanceInfoTb;
import cn.odboy.task.dal.model.TaskInstanceInfoVo;
import cn.odboy.task.dal.model.TaskInstanceNodeVo;
import cn.odboy.task.dal.model.TaskTemplateInfoVo;
import cn.odboy.task.dal.model.TaskTemplateNodeVo;
import cn.odboy.task.service.TaskInstanceDetailService;
import cn.odboy.task.service.TaskInstanceInfoService;
import cn.odboy.task.service.TaskTemplateInfoService;
import cn.odboy.util.CsDateUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TaskManage {
    @Resource
    private Scheduler scheduler;
    @Autowired
    private TaskTemplateInfoService taskTemplateInfoService;
    @Autowired
    private TaskInstanceInfoService taskInstanceInfoService;
    @Autowired
    private TaskInstanceDetailService taskInstanceDetailService;

    /**
     * 创建任务单
     *
     * @param contextName    上下文名称，这里特指应用名
     * @param changeTypeEnum 变更类型
     * @param envAlias       环境别名
     * @param source         来源
     * @param reason         变更原因
     * @param dataMap        任务参数
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskInstanceInfoTb createJob(String contextName, TaskChangeTypeEnum changeTypeEnum, String language, String envAlias, String source, String reason, JobDataMap dataMap) {
        if (dataMap == null) {
            dataMap = new JobDataMap();
        }
        // ========================== 获取任务编排模板 ==========================
        TaskTemplateInfoVo taskInstanceInfoVo = taskTemplateInfoService.getTemplateInfoByECL(envAlias, contextName, language, changeTypeEnum.getCode());
        if (taskInstanceInfoVo == null) {
            throw new BadRequestException("没有查询到任务编排模板");
        }
        String templateInfo = taskInstanceInfoVo.getTemplateInfo();
        List<TaskTemplateNodeVo> taskTemplateNodeVos = JSON.parseArray(templateInfo, TaskTemplateNodeVo.class);
        if (taskTemplateNodeVos == null || taskTemplateNodeVos.isEmpty()) {
            throw new BadRequestException("没有查询到任务编排明细");
        }
        // ========================== 传递参数 ==========================
        // 应用名、资源类型
        dataMap.put("contextName", contextName);
        // 开发语言、资源版本
        dataMap.put("language", language);
        dataMap.put("envAlias", envAlias);
        // 变更类型
        dataMap.put("changeType", changeTypeEnum.getCode());
        // ========================== 创建任务 ==========================
        TaskInstanceInfoTb newInstance = new TaskInstanceInfoTb();
        newInstance.setContextName(contextName);
        newInstance.setLanguage(language);
        newInstance.setChangeType(changeTypeEnum.getCode());
        newInstance.setEnvAlias(envAlias);
        newInstance.setStatus(TaskStatusEnum.Running.getCode());
        newInstance.setFinishTime(null);
        newInstance.setSource(source);
        newInstance.setReason(reason);
        newInstance.setTemplate(templateInfo);
        newInstance.setJobData(JSON.toJSONString(dataMap));
        taskInstanceInfoService.save(newInstance);
        dataMap.put("id", newInstance.getId());
        // ========================== 执行任务 ==========================
        JobKey jobKey = JobKey.jobKey(changeTypeEnum.getCode(), contextName);
        TriggerKey triggerKey = TriggerKey.triggerKey(changeTypeEnum.getCode(), contextName);
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

    @Transactional(rollbackFor = Exception.class)
    public TaskInstanceInfoTb retryJob(Long id, String retryNodeCode) {
        // ========================== 查询任务 ==========================
        TaskInstanceInfoTb taskInstanceInfoTb = taskInstanceInfoService.getById(id);
        if (taskInstanceInfoTb == null) {
            throw new BadRequestException("任务不存在");
        }
        // ========================== 失败的任务才能重试 ==========================
        if (TaskStatusEnum.Success.getCode().equals(taskInstanceInfoTb.getStatus())) {
            throw new BadRequestException("任务已执行完毕，无法重试");
        }
        // 参数重放
        JobDataMap dataMap = JSON.parseObject(taskInstanceInfoTb.getJobData(), JobDataMap.class);
        if (dataMap == null) {
            dataMap = new JobDataMap();
        }
        taskInstanceInfoTb.setStatus(TaskStatusEnum.Running.getCode());
        taskInstanceInfoTb.setFinishTime(null);
        taskInstanceInfoService.updateById(taskInstanceInfoTb);
        // ========================== 执行任务 ==========================
        dataMap.put("retryNodeCode", retryNodeCode);
        String changeType = taskInstanceInfoTb.getChangeType();
        String contextName = taskInstanceInfoTb.getContextName();
        JobKey jobKey = JobKey.jobKey(changeType, contextName);
        TriggerKey triggerKey = TriggerKey.triggerKey(changeType, contextName);
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
        return taskInstanceInfoTb;
    }

    public TaskInstanceInfoVo getLastInfo(String contextName, String language, String envAlias, String changeType) {
        TaskInstanceInfoVo record = new TaskInstanceInfoVo();

        TaskInstanceInfoTb historyInstance = taskInstanceInfoService.getLastHistoryInstance(contextName, language, envAlias, changeType);
        if (historyInstance == null) {
            // 仅返回模板
            TaskTemplateInfoVo templateInfo = taskTemplateInfoService.getTemplateInfoByECL(envAlias, contextName, language, changeType);
            record.setTemplate(templateInfo.getTemplateInfo());
            return record;
        }

        record = BeanUtil.copyProperties(historyInstance, TaskInstanceInfoVo.class);
        record.setHistory(buildNodeList(record));

        TaskInstanceInfoTb runningInstance = taskInstanceInfoService.getLastRunningInstance(contextName, language, envAlias, changeType);
        if (runningInstance != null) {
            TaskInstanceInfoVo taskInstanceInfoVo = BeanUtil.copyProperties(runningInstance, TaskInstanceInfoVo.class);
            record.setCurrent(buildNodeList(taskInstanceInfoVo));
        }
        return record;
    }

    private List<TaskInstanceNodeVo> buildNodeList(TaskInstanceInfoVo record) {
        List<TaskInstanceNodeVo> records = new ArrayList<>();

        // 节点明细
        List<TaskInstanceDetailTb> taskInstanceDetails = taskInstanceDetailService.queryByInstanceId(record.getId());
        for (TaskInstanceDetailTb taskInstanceDetail : taskInstanceDetails) {
            TaskInstanceNodeVo instanceNodeVo = new TaskInstanceNodeVo();
            instanceNodeVo.setCode(taskInstanceDetail.getBizCode());
            instanceNodeVo.setName(taskInstanceDetail.getBizName());
            instanceNodeVo.setStartTime(taskInstanceDetail.getStartTime());
            instanceNodeVo.setFinishTime(taskInstanceDetail.getFinishTime());
            if (taskInstanceDetail.getFinishTime() == null) {
                instanceNodeVo.setDurationDesc(CsDateUtil.formatSecondsDuration(taskInstanceDetail.getStartTime(), new Date()));
            } else {
                instanceNodeVo.setDurationDesc(CsDateUtil.formatSecondsDuration(taskInstanceDetail.getStartTime(), taskInstanceDetail.getFinishTime()));
            }
            instanceNodeVo.setRunningDesc(taskInstanceDetail.getExecuteInfo());
            instanceNodeVo.setStatus(taskInstanceDetail.getExecuteStatus());
            instanceNodeVo.setStatusDesc(TaskStatusEnum.getDesc(taskInstanceDetail.getExecuteStatus()));
            records.add(instanceNodeVo);
        }

        return records;
    }
}
