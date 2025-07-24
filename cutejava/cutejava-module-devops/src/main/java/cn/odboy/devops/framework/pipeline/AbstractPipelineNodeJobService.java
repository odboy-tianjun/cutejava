package cn.odboy.devops.framework.pipeline;

import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.service.core.PipelineInstanceNodeDetailService;
import cn.odboy.devops.service.core.PipelineInstanceNodeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public abstract class AbstractPipelineNodeJobService {
    @Autowired
    private PipelineInstanceNodeService pipelineInstanceNodeService;
    @Autowired
    private PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService;

    public PipelineInstanceNodeTb getCurrentNodeInfo(long instanceId, String code) {
        return pipelineInstanceNodeService.getPipelineInstanceNodeByArgs(instanceId, code);
    }

    public void addLog(PipelineInstanceNodeTb pipelineInstanceNode, String stepName, PipelineStatusEnum stepStatus, String stepMsg, Date finishTime) {
        if (pipelineInstanceNode != null) {
            pipelineInstanceNodeDetailService.addLog(pipelineInstanceNode.getId(), pipelineInstanceNode.getCode(), stepName, stepStatus, stepMsg, finishTime);
        }
    }
}
