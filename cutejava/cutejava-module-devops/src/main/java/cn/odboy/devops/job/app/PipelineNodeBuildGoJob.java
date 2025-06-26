package cn.odboy.devops.job.app;

import cn.odboy.devops.constant.pipeline.PipelineNodeBizCodeConst;
import cn.odboy.devops.framework.pipeline.PipelineNodeJobExecutor;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.service.pipeline.PipelineInstanceNodeDetailService;
import cn.odboy.framework.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service(value = PipelineNodeBizCodeConst.AppGo.BUILD)
public class PipelineNodeBuildGoJob implements PipelineNodeJobExecutor {
    @Override
    public PipelineNodeJobExecuteResult execute(Map<String, Object> contextArgs, PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService) throws BadRequestException {
        return PipelineNodeJobExecuteResult.success();
    }
}
