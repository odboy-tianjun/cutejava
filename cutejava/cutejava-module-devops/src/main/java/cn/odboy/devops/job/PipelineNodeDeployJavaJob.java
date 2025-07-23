package cn.odboy.devops.job;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineTemplateTb;
import cn.odboy.devops.framework.pipeline.AbstractPipelineNodeJobService;
import cn.odboy.devops.framework.pipeline.core.PipelineNodeJobExecutor;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.job.biz.PipelineNodeBuildJavaBiz;
import cn.odboy.devops.job.biz.PipelineNodeDeployJavaBiz;
import cn.odboy.devops.service.PipelineTemplateService;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service(value = PipelineConst.EXECUTOR_PREFIX + "node_deploy_java")
public class PipelineNodeDeployJavaJob extends AbstractPipelineNodeJobService implements PipelineNodeJobExecutor {
    private final PipelineNodeDeployJavaBiz pipelineNodeDeployJavaBiz;
    private final PipelineTemplateService pipelineTemplateService;

    @Override
    public PipelineNodeJobExecuteResult execute(JobDataMap jobDataMap) throws BadRequestException {
        // 参数列表
        long instanceId = jobDataMap.getLong(PipelineConst.INSTANCE_ID);
        long templateId = jobDataMap.getLong(PipelineConst.TEMPLATE_ID);
        PipelineTemplateTb pipelineTemplate = pipelineTemplateService.getPipelineTemplateById(templateId);
        String templateType = pipelineTemplate.getType();
        PipelineNodeTemplateVo currentNodeTemplate = (PipelineNodeTemplateVo) jobDataMap.get(PipelineConst.CURRENT_NODE_TEMPLATE);
        PipelineInstanceNodeTb pipelineInstanceNode = getCurrentNodeInfo(instanceId, currentNodeTemplate.getCode());
        String contextName = jobDataMap.getString(PipelineConst.CONTEXT_NAME);
        String env = jobDataMap.getString(PipelineConst.ENV);
        List<PipelineNodeTemplateVo> templateList = JSON.parseArray(jobDataMap.getString(PipelineConst.TEMPLATE), PipelineNodeTemplateVo.class);
        PipelineNodeJobExecuteResult lastNodeResult = (PipelineNodeJobExecuteResult) jobDataMap.get(PipelineConst.LAST_NODE_RESULT);
        // 步骤执行
        pipelineNodeDeployJavaBiz.deployStart(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeDeployJavaBiz.deployJavaByWithContextName(pipelineInstanceNode, contextName, env, templateList, lastNodeResult, templateType);
        pipelineNodeDeployJavaBiz.deployFinish(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        return PipelineNodeJobExecuteResult.success();
    }
}
