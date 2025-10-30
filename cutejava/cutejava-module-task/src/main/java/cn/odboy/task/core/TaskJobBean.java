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

import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.context.CsSpringBeanHolder;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.exception.ServerException;
import cn.odboy.task.constant.TaskJobKeys;
import cn.odboy.task.dal.dataobject.TaskInstanceDetailTb;
import cn.odboy.task.dal.dataobject.TaskInstanceInfoTb;
import cn.odboy.task.dal.model.TaskTemplateNodeVo;
import cn.odboy.task.service.TaskInstanceDetailService;
import cn.odboy.task.service.TaskInstanceInfoService;
import cn.odboy.task.service.TaskInstanceStepDetailService;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.InterruptableJob;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class TaskJobBean extends QuartzJobBean implements InterruptableJob {
    private Thread workThread = null;

    private String getBeanAlias(String code) {
        if (StrUtil.isBlank(code)) {
            throw new ServerException("参数code必填");
        }
        // node_init
        String[] s = code.split("_");
        return Arrays.stream(s).map(StrUtil::upperFirst).collect(Collectors.joining());
    }

    @Override
    public void executeInternal(JobExecutionContext context) {
        this.workThread = Thread.currentThread();
        // ========================== 获取代理类 ==========================
        TaskInstanceInfoService taskInstanceInfoService = CsSpringBeanHolder.getBean(TaskInstanceInfoService.class);
        TaskInstanceDetailService taskInstanceDetailService = CsSpringBeanHolder.getBean(TaskInstanceDetailService.class);
        // ========================== 获取参数 ==========================
        JobDataMap dataMap = context.getMergedJobDataMap();
        long id = dataMap.getLong(TaskJobKeys.ID);
        // ========================== 获取任务编排模板 ==========================
        TaskInstanceInfoTb taskInstanceInfoVo = taskInstanceInfoService.getById(id);
        String templateInfo = taskInstanceInfoVo.getTemplate();
        List<TaskTemplateNodeVo> taskTemplateNodeVos = JSON.parseArray(templateInfo, TaskTemplateNodeVo.class);
        // ========================== 判断是否重试任务 ==========================
        String retryNodeCode = dataMap.getString(TaskJobKeys.RETRY_NODE_CODE);
        if (StrUtil.isBlank(retryNodeCode)) {
            executeNormalTask(taskInstanceInfoService, taskInstanceDetailService, id, dataMap, taskTemplateNodeVos);
        } else {
            executeRetryTask(taskInstanceInfoService, taskInstanceDetailService, id, dataMap, taskTemplateNodeVos, retryNodeCode);
        }
    }

    /**
     * 执行普通任务（非重试任务）
     *
     * @param taskInstanceInfoService   任务实例信息服务
     * @param taskInstanceDetailService 任务实例明细服务
     * @param id                        任务实例ID
     * @param dataMap                   数据映射
     * @param taskTemplateNodeVos       模板节点列表
     */
    private void executeNormalTask(TaskInstanceInfoService taskInstanceInfoService, TaskInstanceDetailService taskInstanceDetailService, long id,
        JobDataMap dataMap, List<TaskTemplateNodeVo> taskTemplateNodeVos) {
        // ========================== 初始化执行明细 ==========================
        List<TaskInstanceDetailTb> taskInstanceDetails =
            taskTemplateNodeVos.stream().map(taskTemplateNodeVo -> buildTaskInstanceDetail(id, taskTemplateNodeVo)).collect(Collectors.toList());
        taskInstanceDetailService.saveBatch(taskInstanceDetails);
        Map<String, Long> codeIdMap = taskInstanceDetails.stream().collect(Collectors.toMap(TaskInstanceDetailTb::getBizCode, TaskInstanceDetailTb::getId));
        // ========================== 顺序执行 ==========================
        executeTaskSteps(taskInstanceInfoService, taskInstanceDetailService, id, dataMap, taskTemplateNodeVos, codeIdMap);
    }

    /**
     * 执行重试任务
     *
     * @param taskInstanceInfoService   任务实例信息服务
     * @param taskInstanceDetailService 任务实例明细服务
     * @param id                        任务实例ID
     * @param dataMap                   数据映射
     * @param taskTemplateNodeVos       模板节点列表
     * @param retryNodeCode             重试节点编码
     */
    private void executeRetryTask(TaskInstanceInfoService taskInstanceInfoService, TaskInstanceDetailService taskInstanceDetailService, long id,
        JobDataMap dataMap, List<TaskTemplateNodeVo> taskTemplateNodeVos, String retryNodeCode) {
        boolean isFound = false;
        // ========================== 初始化执行明细 ==========================
        List<TaskInstanceDetailTb> taskInstanceDetails = new ArrayList<>();
        List<TaskTemplateNodeVo> taskTemplateNodeRetryList = new ArrayList<>();
        for (TaskTemplateNodeVo taskTemplateNodeVo : taskTemplateNodeVos) {
            if (isFound) {
                taskInstanceDetails.add(buildTaskInstanceDetail(id, taskTemplateNodeVo));
                taskTemplateNodeRetryList.add(taskTemplateNodeVo);
                continue;
            }
            if (taskTemplateNodeVo.getCode().equals(retryNodeCode)) {
                if (!taskTemplateNodeVo.getRetry()) {
                    throw new BadRequestException("节点 '" + taskTemplateNodeVo.getName() + "' 不支持重试");
                }
                taskInstanceDetails.add(buildTaskInstanceDetail(id, taskTemplateNodeVo));
                taskTemplateNodeRetryList.add(taskTemplateNodeVo);
                isFound = true;
            }
        }
        TaskInstanceStepDetailService taskInstanceStepDetailService = CsSpringBeanHolder.getBean(TaskInstanceStepDetailService.class);
        List<String> bizCodeList = taskInstanceDetails.stream().map(TaskInstanceDetailTb::getBizCode).distinct().collect(Collectors.toList());
        List<TaskInstanceDetailTb> taskInstanceDetailTbs = taskInstanceDetailService.queryByInstanceIdAndBizCodeList(id, bizCodeList);
        List<Long> taskInstanceDetailIds = taskInstanceDetailTbs.stream().map(TaskInstanceDetailTb::getId).collect(Collectors.toList());
        // 根据ID删除明细
        taskInstanceDetailService.removeByIds(taskInstanceDetailIds);
        // 根据明细删除步骤
        taskInstanceStepDetailService.removeByInstanceDetailIds(taskInstanceDetailIds);
        taskInstanceDetailService.saveBatch(taskInstanceDetails);
        Map<String, Long> codeIdMap = taskInstanceDetails.stream().collect(Collectors.toMap(TaskInstanceDetailTb::getBizCode, TaskInstanceDetailTb::getId));
        // ========================== 顺序执行 ==========================
        executeTaskSteps(taskInstanceInfoService, taskInstanceDetailService, id, dataMap, taskTemplateNodeRetryList, codeIdMap);
    }

    /**
     * 构建任务实例明细对象
     *
     * @param id                 实例ID
     * @param taskTemplateNodeVo 模板节点
     * @return 任务实例明细对象
     */
    private TaskInstanceDetailTb buildTaskInstanceDetail(long id, TaskTemplateNodeVo taskTemplateNodeVo) {
        TaskInstanceDetailTb taskInstanceDetail = new TaskInstanceDetailTb();
        taskInstanceDetail.setInstanceId(id);
        taskInstanceDetail.setFinishTime(null);
        taskInstanceDetail.setBizCode(taskTemplateNodeVo.getCode());
        taskInstanceDetail.setBizName(taskTemplateNodeVo.getName());
        taskInstanceDetail.setExecuteInfo("未开始");
        taskInstanceDetail.setExecuteStatus("pending");
        return taskInstanceDetail;
    }

    /**
     * 执行任务步骤
     *
     * @param taskInstanceInfoService   任务实例信息服务
     * @param taskInstanceDetailService 任务实例明细服务
     * @param id                        实例ID
     * @param dataMap                   数据映射
     * @param taskTemplateNodes         需要执行的任务节点列表
     * @param codeIdMap                 节点编码与明细ID映射
     */
    private void executeTaskSteps(TaskInstanceInfoService taskInstanceInfoService, TaskInstanceDetailService taskInstanceDetailService, Long id,
        JobDataMap dataMap, List<TaskTemplateNodeVo> taskTemplateNodes, Map<String, Long> codeIdMap) {
        try {
            for (TaskTemplateNodeVo taskTemplateNodeVo : taskTemplateNodes) {
                String code = taskTemplateNodeVo.getCode();
                try {
                    TaskStepExecutor executor = CsSpringBeanHolder.getBean("taskStep" + getBeanAlias(code));
                    taskInstanceDetailService.fastStart(id, code, dataMap);
                    executor.execute(codeIdMap.getOrDefault(code, null), dataMap, taskTemplateNodeVo);
                    taskInstanceDetailService.fastSuccessWithInfo(id, code, null);
                } catch (Exception e) {
                    log.error("任务节点执行失败", e);
                    taskInstanceDetailService.fastFailWithInfo(id, code, e.getMessage());
                    throw new ServerException(e);
                }
            }
            taskInstanceInfoService.fastSuccessWithData(id, dataMap);
        } catch (Exception e) {
            log.error("任务执行失败", e);
            taskInstanceInfoService.fastFailWithMessageData(id, e.getMessage(), dataMap);
        }
    }

    @Override
    public void interrupt() {
        if (workThread != null) {
            workThread.stop();
        }
    }
}
