package cn.odboy.devops.job;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.framework.pipeline.core.PipelineNodeJobExecutor;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.PipelineInstanceNodeDetailService;
import cn.odboy.devops.service.PipelineInstanceNodeService;
import cn.odboy.devops.service.PipelineInstanceService;
import cn.odboy.devops.service.PipelineTemplateService;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service(value = PipelineConst.EXECUTOR_PREFIX + "node_build_java")
public class PipelineNodeBuildJava implements PipelineNodeJobExecutor {
    private final PipelineTemplateService pipelineTemplateService;
    private final PipelineInstanceService pipelineInstanceService;
    private final PipelineInstanceNodeService pipelineInstanceNodeService;
    private final PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService;

    @Override
    public PipelineNodeJobExecuteResult execute(JobDataMap jobDataMap) {
        long instanceId = jobDataMap.getLong(PipelineConst.INSTANCE_ID);
        String contextName = jobDataMap.getString(PipelineConst.CONTEXT_NAME);
        String env = jobDataMap.getString(PipelineConst.ENV);
        long templateId = jobDataMap.getLong(PipelineConst.TEMPLATE_ID);
        PipelineNodeTemplateVo currentNodeTemplate = (PipelineNodeTemplateVo) jobDataMap.get(PipelineConst.CURRENT_NODE_TEMPLATE);
        pipelineInstanceNodeService.updatePipelineInstanceNodeByArgs(instanceId, currentNodeTemplate.getCode(), PipelineStatusEnum.SUCCESS, PipelineStatusEnum.SUCCESS.getDesc());
        return PipelineNodeJobExecuteResult.success();
    }
}
