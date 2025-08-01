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

import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineTemplateTb;
import cn.odboy.devops.framework.pipeline.AbstractPipelineNodeJobService;
import cn.odboy.devops.framework.pipeline.constant.PipelineConst;
import cn.odboy.devops.framework.pipeline.core.PipelineNodeJobExecutor;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.core.PipelineTemplateService;
import cn.odboy.devops.service.pipeline.node.PipelineNodeDeployJavaBiz;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流水线节点任务：Python部署
 *
 * @author odboy
 * @date 2025-07-24
 */
@Service(value = PipelineConst.EXECUTOR_PREFIX + "node_deploy_python")
public class PipelineNodeDeployPythonService extends AbstractPipelineNodeJobService implements PipelineNodeJobExecutor {
    @Autowired
    private PipelineNodeDeployJavaBiz pipelineNodeDeployJavaBiz;
    @Autowired
    private PipelineTemplateService pipelineTemplateService;

    @Override
    public PipelineNodeJobExecuteResult execute(JobDataMap jobDataMap) throws BadRequestException {
        // 参数列表
        long instanceId = jobDataMap.getLong(PipelineConst.INSTANCE_ID);
        long templateId = jobDataMap.getLong(PipelineConst.TEMPLATE_ID);
        PipelineTemplateTb pipelineTemplate = pipelineTemplateService.getPipelineTemplateById(templateId);
        PipelineNodeTemplateVo currentNodeTemplate = (PipelineNodeTemplateVo)jobDataMap.get(PipelineConst.CURRENT_NODE_TEMPLATE);
        PipelineInstanceNodeTb pipelineInstanceNode = getCurrentNodeInfo(instanceId, currentNodeTemplate.getCode());
        String contextName = jobDataMap.getString(PipelineConst.CONTEXT_NAME);
        String env = jobDataMap.getString(PipelineConst.ENV);
        List<PipelineNodeTemplateVo> templateList = JSON.parseArray(jobDataMap.getString(PipelineConst.TEMPLATE), PipelineNodeTemplateVo.class);
        PipelineNodeJobExecuteResult lastNodeResult = (PipelineNodeJobExecuteResult)jobDataMap.get(PipelineConst.LAST_NODE_RESULT);
        // 步骤执行
        pipelineNodeDeployJavaBiz.deployStart(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeDeployJavaBiz.deployJavaByWithContextName(pipelineInstanceNode, contextName, env, templateList, lastNodeResult, pipelineTemplate);
        pipelineNodeDeployJavaBiz.deployFinish(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        return PipelineNodeJobExecuteResult.success();
    }
}
