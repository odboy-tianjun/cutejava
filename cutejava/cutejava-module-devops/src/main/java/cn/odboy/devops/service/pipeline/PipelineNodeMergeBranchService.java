package cn.odboy.devops.service.pipeline;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.framework.pipeline.AbstractPipelineNodeJobService;
import cn.odboy.devops.framework.pipeline.core.PipelineNodeJobExecutor;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.pipeline.node.PipelineNodeMergeBranchBiz;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDataMap;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流水线节点任务：分支合并
 *
 * @author odboy
 * @date 2025-07-24
 */
@RequiredArgsConstructor
@Service(value = PipelineConst.EXECUTOR_PREFIX + "node_merge_branch")
public class PipelineNodeMergeBranchService extends AbstractPipelineNodeJobService implements PipelineNodeJobExecutor {
    private final PipelineNodeMergeBranchBiz pipelineNodeMergeBranchBiz;

    @Override
    public PipelineNodeJobExecuteResult execute(JobDataMap jobDataMap) throws Exception {
        // 参数列表
        long instanceId = jobDataMap.getLong(PipelineConst.INSTANCE_ID);
        PipelineNodeTemplateVo currentNodeTemplate = (PipelineNodeTemplateVo) jobDataMap.get(PipelineConst.CURRENT_NODE_TEMPLATE);
        PipelineInstanceNodeTb pipelineInstanceNode = getCurrentNodeInfo(instanceId, currentNodeTemplate.getCode());
        String contextName = jobDataMap.getString(PipelineConst.CONTEXT_NAME);
        String env = jobDataMap.getString(PipelineConst.ENV);
        List<PipelineNodeTemplateVo> templateList = JSON.parseArray(jobDataMap.getString(PipelineConst.TEMPLATE), PipelineNodeTemplateVo.class);
        PipelineNodeJobExecuteResult lastNodeResult = (PipelineNodeJobExecuteResult) jobDataMap.get(PipelineConst.LAST_NODE_RESULT);
        // 步骤执行
        pipelineNodeMergeBranchBiz.mergeBranchStart(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeMergeBranchBiz.integrationAreaBranchMergeRelease(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        pipelineNodeMergeBranchBiz.mergeBranchFinish(pipelineInstanceNode, contextName, env, templateList, lastNodeResult);
        return PipelineNodeJobExecuteResult.success();
    }
}
