package cn.odboy.devops.framework.pipeline.core;

import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.framework.exception.BadRequestException;

import java.util.Map;

public interface PipelineNodeJobExecutor {
    PipelineNodeJobExecuteResult execute(Map<String, Object> contextArgs) throws BadRequestException;
}
