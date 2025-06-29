package cn.odboy.devops.job.app;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.constant.pipeline.PipelineNodeBizCodeConst;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.constant.pipeline.PipelineTypeEnum;
import cn.odboy.devops.dal.dataobject.pipeline.PipelineInstanceTb;
import cn.odboy.devops.dal.dataobject.pipeline.PipelineTemplateTb;
import cn.odboy.devops.framework.pipeline.core.PipelineNodeJobExecutor;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.service.pipeline.PipelineInstanceNodeDetailService;
import cn.odboy.devops.service.pipeline.PipelineInstanceService;
import cn.odboy.devops.service.pipeline.PipelineTemplateService;
import cn.odboy.framework.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service(value = PipelineNodeBizCodeConst.MERGE_CONFIRM)
public class PipelineNodeMergeConfirmJob implements PipelineNodeJobExecutor {
    private final PipelineTemplateService pipelineTemplateService;
    private final PipelineInstanceService pipelineInstanceService;
    private final PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PipelineNodeJobExecuteResult execute(Map<String, Object> contextArgs) throws BadRequestException {
        String nodeName = (String) contextArgs.getOrDefault(PipelineConst.INSTANCE_NODE_NAME_KEY, "");
        PipelineInstanceTb pipelineInstance = (PipelineInstanceTb) contextArgs.get(PipelineConst.INSTANCE_KEY);
        PipelineNodeJobExecuteResult executeResult = new PipelineNodeJobExecuteResult();
        try {
            PipelineTemplateTb pipelineTemplate = pipelineTemplateService.getPipelineTemplateById(pipelineInstance.getPipelineTemplateId());
            PipelineTypeEnum pipelineType = PipelineTypeEnum.getByCode(pipelineInstance.getType());
            pipelineInstanceNodeDetailService.addLog(pipelineInstance.getPipelineInstanceId(), contextArgs, String.format("%s开始", nodeName), PipelineStatusEnum.SUCCESS, "");
            pipelineInstanceNodeDetailService.addLog(pipelineInstance.getPipelineInstanceId(), contextArgs, String.format("%s成功", nodeName), PipelineStatusEnum.SUCCESS, "");
            return executeResult;
        } catch (Exception e) {
            pipelineInstanceNodeDetailService.addLog(pipelineInstance.getPipelineInstanceId(), contextArgs, String.format("%s失败", nodeName), PipelineStatusEnum.FAIL, e.getMessage());
            throw e;
        }
    }
}
