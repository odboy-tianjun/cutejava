package cn.odboy.devops.service;

import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;

/**
 * <p>
 * 流水线实例服务
 * </p>
 *
 * @author odboy
 * @since 2025-06-26
 */
public interface PipelineInstanceService {
    void start(PipelineInstanceTb pipelineInstanceTb);
}
