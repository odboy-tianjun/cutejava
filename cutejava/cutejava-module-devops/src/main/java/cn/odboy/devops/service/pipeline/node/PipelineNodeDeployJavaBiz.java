package cn.odboy.devops.service.pipeline.node;

import cn.hutool.core.thread.ThreadUtil;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineTemplateTb;
import cn.odboy.devops.framework.pipeline.log.PipelineNodeStepLog;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PipelineNodeDeployJavaBiz {
    @PipelineNodeStepLog("部署开始")
    public void deployStart(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(1000);
    }

    @PipelineNodeStepLog("部署中")
    public void deployJavaByWithContextName(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult, PipelineTemplateTb templateType) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("部署完成")
    public void deployFinish(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(1000);
    }
}
