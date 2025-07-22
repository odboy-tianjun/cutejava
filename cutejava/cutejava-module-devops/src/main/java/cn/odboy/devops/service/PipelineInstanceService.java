package cn.odboy.devops.service;

import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeDataVo;

import java.util.List;

/**
 * <p>
 * 流水线实例服务
 * </p>
 *
 * @author odboy
 * @since 2025-06-26
 */
public interface PipelineInstanceService {
    String startPipeline(PipelineInstanceTb pipelineInstanceTb);
    void restartPipeline(PipelineInstanceTb pipelineInstanceTb, String retryNodeCode);
    List<PipelineNodeDataVo> queryLastPipelineDetail(String instanceId);
}
