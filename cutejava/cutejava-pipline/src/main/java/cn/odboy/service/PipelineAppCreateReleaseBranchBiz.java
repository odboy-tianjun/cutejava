package cn.odboy.service;

import cn.odboy.handler.SerialPipelineNodeHandler;
import org.springframework.stereotype.Service;

@Service("pipeline:create_release_branch")
public class PipelineAppCreateReleaseBranchBiz extends SerialPipelineNodeHandler<Object, Object> {
    @Override
    public void preProcess(String pipelineId, Object inputModel) {

    }

    @Override
    public void preProcessException(String pipelineId, Object inputModel, Exception e) {

    }

    @Override
    public Object process(String pipelineId, Object inputModel) {
        return "Hello World";
    }

    @Override
    public void processException(String pipelineId, Object inputModel, Exception e) {

    }

    @Override
    public void postProcess(String pipelineId, Object inputModel, Object processResult) {

    }

    @Override
    public void postProcessException(String pipelineId, Object inputModel, Object processResult, Exception e) {

    }

    @Override
    public boolean isLocked(String pipelineId, Object inputModel) {
        return false;
    }
}
