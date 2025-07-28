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

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.core.PipelineInstanceNodeService;
import cn.odboy.framework.context.CsSpringBeanHolder;
import cn.odboy.framework.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * 流水线任务载体
 *
 * @author odboy
 * @date 2025-07-19
 */
@Slf4j
public class PipelineNodeJobBean implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        PipelineInstanceNodeService pipelineInstanceNodeService = CsSpringBeanHolder.getBean(PipelineInstanceNodeService.class);

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        long instanceId = jobDataMap.getLong(PipelineConst.INSTANCE_ID);
        PipelineNodeTemplateVo currentNodeTemplate = (PipelineNodeTemplateVo) jobDataMap.get(PipelineConst.CURRENT_NODE_TEMPLATE);
        String nodeCode = currentNodeTemplate.getCode();

        try {
            String serviceName = PipelineConst.EXECUTOR_PREFIX + nodeCode;
            PipelineNodeJobExecutor pipelineNodeJobExecutor = CsSpringBeanHolder.getBean(serviceName);
            pipelineInstanceNodeService.updatePipelineInstanceNodeByArgs(instanceId, nodeCode, PipelineStatusEnum.RUNNING, PipelineStatusEnum.RUNNING.getDesc());
            PipelineNodeJobExecuteResult executeResult = pipelineNodeJobExecutor.execute(jobDataMap);
            jobExecutionContext.getMergedJobDataMap().put(PipelineConst.LAST_NODE_RESULT, executeResult);
            pipelineInstanceNodeService.finishPipelineInstanceNodeByArgs(instanceId, nodeCode, PipelineStatusEnum.SUCCESS, PipelineStatusEnum.SUCCESS.getDesc(), new Date());
        } catch (BadRequestException e) {
            log.error("流水线节点执行异常", e);
            pipelineInstanceNodeService.finishPipelineInstanceNodeByArgs(instanceId, nodeCode, PipelineStatusEnum.FAIL, e.getMessage(), new Date());
        } catch (Exception e) {
            log.error("流水线节点执行异常", e);
            pipelineInstanceNodeService.finishPipelineInstanceNodeByArgs(instanceId, nodeCode, PipelineStatusEnum.FAIL, PipelineStatusEnum.FAIL.getDesc(), new Date());
        }
    }
}
