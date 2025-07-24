package cn.odboy.devops.service.pipeline.node;

import cn.hutool.core.thread.ThreadUtil;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.framework.pipeline.log.PipelineNodeStepLog;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PipelineNodeMergeBranchBiz {
    @PipelineNodeStepLog("分支合并开始")
    public void mergeBranchStart(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(1000);
    }

    @PipelineNodeStepLog("集成区分支合并到release分支")
    public void integrationAreaBranchMergeRelease(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("分支合并完成")
    public void mergeBranchFinish(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(1000);
    }
}
