package cn.odboy.devops.job;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.framework.pipeline.AbstractPipelineNodeJobService;
import cn.odboy.devops.framework.pipeline.core.PipelineNodeJobExecutor;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.job.biz.PipelineNodeBuildJavaBiz;
import cn.odboy.devops.job.biz.PipelineNodeInitBiz;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service(value = PipelineConst.EXECUTOR_PREFIX + "node_build_java")
public class PipelineNodeBuildJavaJob extends AbstractPipelineNodeJobService implements PipelineNodeJobExecutor {
    private final PipelineNodeBuildJavaBiz pipelineNodeBuildJavaBiz;

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
        pipelineNodeBuildJavaBiz.initStart(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeBuildJavaBiz.createReleaseBranch(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeBuildJavaBiz.mergeMasterToRelease(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeBuildJavaBiz.initFinish(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        return PipelineNodeJobExecuteResult.success();
    }
}
