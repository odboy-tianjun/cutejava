package cn.odboy.devops.service.pipeline;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineTemplateTb;
import cn.odboy.devops.framework.pipeline.AbstractPipelineNodeJobService;
import cn.odboy.devops.framework.pipeline.core.PipelineNodeJobExecutor;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.core.PipelineTemplateService;
import cn.odboy.devops.service.pipeline.node.PipelineNodeDeployJavaBiz;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流水线节点任务：Python部署
 *
 * @author odboy
 * @date 2025-07-24
 */
@RequiredArgsConstructor
@Service(value = PipelineConst.EXECUTOR_PREFIX + "node_deploy_python")
public class PipelineNodeDeployPythonService extends AbstractPipelineNodeJobService implements PipelineNodeJobExecutor {
    private final PipelineNodeDeployJavaBiz pipelineNodeDeployJavaBiz;
    private final PipelineTemplateService pipelineTemplateService;

    @Override
    public PipelineNodeJobExecuteResult execute(JobDataMap jobDataMap) throws BadRequestException {
        // 参数列表
        long instanceId = jobDataMap.getLong(PipelineConst.INSTANCE_ID);
        long templateId = jobDataMap.getLong(PipelineConst.TEMPLATE_ID);
        PipelineTemplateTb pipelineTemplate = pipelineTemplateService.getPipelineTemplateById(templateId);
        PipelineNodeTemplateVo currentNodeTemplate = (PipelineNodeTemplateVo) jobDataMap.get(PipelineConst.CURRENT_NODE_TEMPLATE);
        PipelineInstanceNodeTb pipelineInstanceNode = getCurrentNodeInfo(instanceId, currentNodeTemplate.getCode());
        String contextName = jobDataMap.getString(PipelineConst.CONTEXT_NAME);
        String env = jobDataMap.getString(PipelineConst.ENV);
        List<PipelineNodeTemplateVo> templateList = JSON.parseArray(jobDataMap.getString(PipelineConst.TEMPLATE), PipelineNodeTemplateVo.class);
        PipelineNodeJobExecuteResult lastNodeResult = (PipelineNodeJobExecuteResult) jobDataMap.get(PipelineConst.LAST_NODE_RESULT);
        // 步骤执行
        pipelineNodeDeployJavaBiz.deployStart(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeDeployJavaBiz.deployJavaByWithContextName(pipelineInstanceNode, contextName, env, templateList, lastNodeResult, pipelineTemplate);
        pipelineNodeDeployJavaBiz.deployFinish(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        return PipelineNodeJobExecuteResult.success();
    }
}
