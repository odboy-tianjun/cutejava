package cn.odboy.devops.service;

import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import com.anwen.mongo.service.IService;

/**
 * 流水线实例节点明细
 *
 * @author odboy
 * @date 2025-07-20
 */
public interface PipelineInstanceNodeService extends IService<PipelineInstanceNodeTb> {
    void createPipelineInstanceDetail(PipelineInstanceTb pipelineInstanceTb);
    void updatePipelineInstanceNodeByArgs(Long instanceId, String code, PipelineStatusEnum pipelineStatusEnum, String msg);
    PipelineInstanceNodeTb getPipelineInstanceNodeByArgs(Long instanceId, String code);
}
