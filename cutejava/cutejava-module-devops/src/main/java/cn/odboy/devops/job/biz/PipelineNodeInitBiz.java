package cn.odboy.devops.job.biz;

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
public class PipelineNodeInitBiz {
    @PipelineNodeStepLog("初始化开始")
    public void initStart(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("Master分支合并到Release分支")
    public void mergeMasterToRelease(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("新建Release分支")
    public void createReleaseBranch(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("初始化完成")
    public void initFinish(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }
}
