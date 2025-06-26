package cn.odboy.devops.framework.pipeline;

import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.service.pipeline.PipelineInstanceNodeDetailService;

import java.util.Map;

public interface PipelineNodeJobExecutor {
    PipelineNodeJobExecuteResult execute(Map<String, Object> contextArgs, PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService) throws BadRequestException;
}
