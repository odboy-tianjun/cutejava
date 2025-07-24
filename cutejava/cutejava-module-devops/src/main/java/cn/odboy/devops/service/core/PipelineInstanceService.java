package cn.odboy.devops.service.core;

import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.dal.model.StartPipelineResultVo;
import cn.odboy.devops.framework.pipeline.model.PipelineInstanceVo;

/**
 * <p>
 * 流水线实例服务
 * </p>
 *
 * @author odboy
 * @since 2025-06-26
 */
public interface PipelineInstanceService {
    StartPipelineResultVo startPipeline(PipelineInstanceTb pipelineInstanceTb);

    StartPipelineResultVo restartPipeline(PipelineInstanceTb pipelineInstanceTb, String retryNodeCode);

    PipelineInstanceVo queryLastPipelineDetail(String instanceId);

    void queryLastPipelineDetailWs(String sid);
}
