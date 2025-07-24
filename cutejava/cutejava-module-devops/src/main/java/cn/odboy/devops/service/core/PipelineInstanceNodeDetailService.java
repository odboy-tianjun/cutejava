package cn.odboy.devops.service.core;

import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeDetailTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import com.anwen.mongo.service.IService;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 流水线实例节点明细
 * </p>
 *
 * @author codegen
 * @since 2025-06-26
 */
public interface PipelineInstanceNodeDetailService extends IService<PipelineInstanceNodeDetailTb> {
    void addLog(Long nodeId, String nodeCode, String stepName, PipelineStatusEnum status, String stepMsg, Date finishTime);

    PipelineInstanceNodeDetailTb getPipelineInstanceNodeDetailByArgs(Long nodeId, String nodeCode, String stepName);

    void removeByNodeIds(List<Long> nodeIds);

    PipelineInstanceNodeDetailTb getLastPipelineInstanceNodeDetailByArgs(PipelineInstanceNodeTb instanceNode, Long nodeId, String nodeCode);
}
