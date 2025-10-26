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
import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.context.CsSpringBeanHolder;
import cn.odboy.framework.exception.web.BadRequestException;
import cn.odboy.task.constant.TaskChangeTypeEnum;
import cn.odboy.task.constant.TaskJobKeys;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TaskManage {
    private final Scheduler scheduler;
    private final TaskTemplateInfoService taskTemplateInfoService;
    private final TaskInstanceInfoService taskInstanceInfoService;
    private final TaskInstanceDetailService taskInstanceDetailService;

    /**
     * 创建任务单(注：同一应用同一变更类型无法并发多实例)
     *
     * @param contextName    上下文名称，这里特指应用名
     * @param changeTypeEnum 变更类型
     * @param language       开发语言、资源版本
     * @param envAlias       环境别名
     * @param source         来源
     * @param reason         变更原因
     * @param dataMap        任务参数
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskInstanceInfoTb createJob(String contextName, TaskChangeTypeEnum changeTypeEnum, String language, String envAlias, String source, String reason, JobDataMap dataMap) {
        if (StrUtil.isBlank(contextName)) {
            throw new BadRequestException("参数contextName必填");
        }
        if (changeTypeEnum == null) {
            throw new BadRequestException("参数changeTypeEnum必填");
        }
        if (StrUtil.isBlank(language)) {
            throw new BadRequestException("参数language必填");
        }
        if (StrUtil.isBlank(envAlias)) {
            throw new BadRequestException("参数envAlias必填");
        }
        if (StrUtil.isBlank(source)) {
            throw new BadRequestException("参数source必填");
        }
        if (dataMap == null) {
            dataMap = new JobDataMap();
        }
        // ========================== 获取任务编排模板 ==========================
        TaskTemplateInfoVo taskInstanceInfoVo = taskTemplateInfoService.getTemplateInfoByECL(envAlias, contextName, language, changeTypeEnum.getCode());
        if (taskInstanceInfoVo == null) {
            throw new BadRequestException("没有查询到任务编排模板");
        }
        String templateInfo = taskInstanceInfoVo.getTemplateInfo();
        List<TaskTemplateNodeVo> taskTemplateNodeVos;
        try {
            taskTemplateNodeVos = JSON.parseArray(templateInfo, TaskTemplateNodeVo.class);
        } catch (Exception e) {
            log.error("任务编排模板解析失败", e);
            throw new BadRequestException("任务编排模板解析失败");
        }
        if (taskTemplateNodeVos == null || taskTemplateNodeVos.isEmpty()) {
            throw new BadRequestException("没有查询到任务编排明细");
        }
        // ========================== 创建任务 ==========================
        TaskManage taskManage = CsSpringBeanHolder.getBean(TaskManage.class);
        TaskInstanceInfoTb newInstance = taskManage.saveTaskInstanceInfoTb(contextName, changeTypeEnum, language, envAlias, source, reason, dataMap, templateInfo);
        dataMap.put(TaskJobKeys.ID, newInstance.getId());
        // 应用名、资源类型
        dataMap.put(TaskJobKeys.CONTEXT_NAME, contextName);
        // 开发语言、资源版本
        dataMap.put(TaskJobKeys.LANGUAGE, language);
        dataMap.put(TaskJobKeys.ENV_ALIAS, envAlias);
        // 变更类型
        dataMap.put(TaskJobKeys.CHANGE_TYPE, changeTypeEnum.getCode());
        buildAndSchedule(changeTypeEnum.getCode(), contextName, dataMap);
        return newInstance;
    }

    @Transactional(rollbackFor = Exception.class)
    public TaskInstanceInfoTb saveTaskInstanceInfoTb(String contextName, TaskChangeTypeEnum changeTypeEnum, String language, String envAlias, String source, String reason, JobDataMap dataMap, String templateInfo) {
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
        return newInstance;
    }

    private void buildAndSchedule(String changeTypeEnum, String contextName, JobDataMap dataMap) {
        // ========================== 执行任务 ==========================
        JobKey jobKey = JobKey.jobKey(changeTypeEnum, contextName);
        TriggerKey triggerKey = TriggerKey.triggerKey(changeTypeEnum, contextName);
        JobDetail jobDetail = JobBuilder.newJob(TaskJobBean.class).withIdentity(jobKey).usingJobData(dataMap).build();
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().build();
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (ObjectAlreadyExistsException e) {
            log.error("任务jobKey={},triggerKey={}任务已存在，跳过加载", jobKey.getName(), triggerKey.getName(), e);
            throw new BadRequestException("任务已存在，跳过加载");
        } catch (SchedulerException e) {
            log.error("任务jobKey={},triggerKey={}执行失败", jobKey.getName(), triggerKey.getName(), e);
            throw new BadRequestException(e);
        }
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
            scheduler.pauseTrigger(triggerKey);
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
        dataMap.put(TaskJobKeys.RETRY_NODE_CODE, retryNodeCode);
        String changeType = taskInstanceInfoTb.getChangeType();
        String contextName = taskInstanceInfoTb.getContextName();
        buildAndSchedule(changeType, contextName, dataMap);
        return taskInstanceInfoTb;
    }

    public TaskInstanceInfoVo getLastInfo(String contextName, String language, String envAlias, String changeType) {
        TaskInstanceInfoVo record = new TaskInstanceInfoVo();

        TaskInstanceInfoTb historyInstance = taskInstanceInfoService.getLastHistoryInstance(contextName, language, envAlias, changeType);
        if (historyInstance == null) {
            // 仅返回模板
            TaskTemplateInfoVo templateInfo = taskTemplateInfoService.getTemplateInfoByECL(envAlias, contextName, language, changeType);
            if (templateInfo == null) {
                throw new BadRequestException("没有查询到任务编排模板");
            }
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
