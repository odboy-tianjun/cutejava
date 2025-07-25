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
public class PipelineNodeDemoBiz {

    @PipelineNodeStepLog("开始")
    public void start(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }

    @PipelineNodeStepLog("完成")
    public void finish(PipelineInstanceNodeTb pipelineInstanceNode, String contextName, String env, List<PipelineNodeTemplateVo> templateList, PipelineNodeJobExecuteResult lastNodeResult) {
        ThreadUtil.safeSleep(2000);
    }
}
