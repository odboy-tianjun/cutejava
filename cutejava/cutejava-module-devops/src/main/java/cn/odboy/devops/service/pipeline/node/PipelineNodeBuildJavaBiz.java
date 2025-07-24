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
public class PipelineNodeBuildJavaBiz {

    @PipelineNodeStepLog("构建开始")
    public void buildJavaStart(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(1000);
    }

    @PipelineNodeStepLog("构建中")
    public void startGitlabPipeline(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("上传包到OSS")
    public void uploadPackageToOss(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("构建完成")
    public void buildJavaFinish(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(1000);
    }
}
