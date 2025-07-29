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
package cn.odboy.devops.service.pipeline;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.framework.pipeline.AbstractPipelineNodeJobService;
import cn.odboy.devops.framework.pipeline.core.PipelineNodeJobExecutor;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.pipeline.node.PipelineNodeInitBiz;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流水线节点任务：初始化准备
 *
 * @author odboy
 * @date 2025-07-24
 */
@Slf4j
@Service(value = PipelineConst.EXECUTOR_PREFIX + "node_init")
@RequiredArgsConstructor
public class PipelineNodeInitService extends AbstractPipelineNodeJobService implements PipelineNodeJobExecutor {
    private final PipelineNodeInitBiz pipelineNodeInitBiz;

    @Override
    public PipelineNodeJobExecuteResult execute(JobDataMap jobDataMap) throws BadRequestException {
        // 参数列表
        long instanceId = jobDataMap.getLong(PipelineConst.INSTANCE_ID);
        PipelineNodeTemplateVo currentNodeTemplate = (PipelineNodeTemplateVo) jobDataMap.get(PipelineConst.CURRENT_NODE_TEMPLATE);
        PipelineInstanceNodeTb pipelineInstanceNode = getCurrentNodeInfo(instanceId, currentNodeTemplate.getCode());
        String contextName = jobDataMap.getString(PipelineConst.CONTEXT_NAME);
        String env = jobDataMap.getString(PipelineConst.ENV);
        List<PipelineNodeTemplateVo> templateList = JSON.parseArray(jobDataMap.getString(PipelineConst.TEMPLATE), PipelineNodeTemplateVo.class);
        PipelineNodeJobExecuteResult lastNodeResult = (PipelineNodeJobExecuteResult) jobDataMap.get(PipelineConst.LAST_NODE_RESULT);
        // 步骤执行
        pipelineNodeInitBiz.initStart(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeInitBiz.createReleaseBranch(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeInitBiz.mergeMasterToRelease(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeInitBiz.initFinish(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        return PipelineNodeJobExecuteResult.success();
    }
}
