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
import cn.odboy.task.dal.dataobject.TaskInstanceDetailTb;
import cn.odboy.task.dal.model.TaskTemplateInfoVo;
import cn.odboy.task.dal.model.TaskTemplateNodeVo;
import cn.odboy.task.service.TaskInstanceDetailService;
import cn.odboy.task.service.TaskInstanceInfoService;
import cn.odboy.task.service.TaskTemplateInfoService;
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
    private Thread subMainThread = null;

    private String getBeanAlias(String code) {
        // node_init
        String[] s = code.split("_");
        return Arrays.stream(s).map(StrUtil::upperFirst).collect(Collectors.joining());
    }

    @Override
    public void executeInternal(JobExecutionContext context) {
        this.subMainThread = Thread.currentThread();
        // ========================== 获取代理类 ==========================
        TaskInstanceInfoService taskInstanceInfoService = CsSpringBeanHolder.getBean(TaskInstanceInfoService.class);
        TaskInstanceDetailService taskInstanceDetailService = CsSpringBeanHolder.getBean(TaskInstanceDetailService.class);
        TaskTemplateInfoService taskTemplateInfoService = CsSpringBeanHolder.getBean(TaskTemplateInfoService.class);
        // ========================== 获取参数 ==========================
        JobDataMap dataMap = context.getMergedJobDataMap();
        long id = dataMap.getLong("id");
        // 应用名、资源类型
        String contextName = dataMap.getString("contextName");
        // 开发语言、资源版本
        String language = dataMap.getString("language");
        String envAlias = dataMap.getString("envAlias");
        // ========================== 获取任务编排模板 ==========================
        TaskTemplateInfoVo taskInstanceInfoVo = taskTemplateInfoService.getTemplateInfoByECL(envAlias, contextName, language);
        if (taskInstanceInfoVo == null) {
            taskInstanceInfoService.fastFailWithMessage(id, "没有查询到任务编排模板");
            return;
        }
        String templateInfo = taskInstanceInfoVo.getTemplateInfo();
        List<TaskTemplateNodeVo> taskTemplateNodeVos = JSON.parseArray(templateInfo, TaskTemplateNodeVo.class);
        if (taskTemplateNodeVos == null || taskTemplateNodeVos.isEmpty()) {
            taskInstanceInfoService.fastFailWithMessage(id, "没有查询到任务编排明细");
            return;
        }
        // ========================== 初始化执行明细 ==========================
        List<TaskInstanceDetailTb> taskInstanceDetails = new ArrayList<>();
        for (TaskTemplateNodeVo taskTemplateNodeVo : taskTemplateNodeVos) {
            TaskInstanceDetailTb taskInstanceDetail = new TaskInstanceDetailTb();
            taskInstanceDetail.setInstanceId(id);
            taskInstanceDetail.setFinishTime(null);
            taskInstanceDetail.setBizCode(taskTemplateNodeVo.getCode());
            taskInstanceDetail.setBizName(taskTemplateNodeVo.getName());
            taskInstanceDetail.setExecuteInfo("未开始");
            taskInstanceDetail.setExecuteStatus("pending");
            taskInstanceDetails.add(taskInstanceDetail);
        }
        taskInstanceDetailService.saveBatch(taskInstanceDetails);
        Map<String, Long> codeIdMap = taskInstanceDetails.stream().collect(Collectors.toMap(TaskInstanceDetailTb::getBizCode, TaskInstanceDetailTb::getId));
        // ========================== 顺序执行 ==========================
        try {
            for (TaskTemplateNodeVo taskTemplateNodeVo : taskTemplateNodeVos) {
                String code = taskTemplateNodeVo.getCode();
                try {
                    TaskStepExecutor executor = CsSpringBeanHolder.getBean("taskStep" + getBeanAlias(code));
                    executor.execute(codeIdMap.getOrDefault(code, null), dataMap, taskTemplateNodeVo, new TaskStepCallback() {
                        @Override
                        public void onStart() {
                            taskInstanceDetailService.fastStart(id, code, dataMap);
                        }

                        @Override
                        public void onFinish(String executeInfo) {
                            taskInstanceDetailService.fastSuccessWithInfo(id, code, executeInfo);
                        }
                    });
                } catch (Exception e) {
                    taskInstanceDetailService.fastFailWithInfo(id, code, e.getMessage());
                    throw new RuntimeException(e);
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
        if (subMainThread != null) {
            subMainThread.stop();
        }
    }
}
