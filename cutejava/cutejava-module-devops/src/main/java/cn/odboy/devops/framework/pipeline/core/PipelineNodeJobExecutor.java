package cn.odboy.devops.framework.pipeline.core;

import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.framework.exception.BadRequestException;
import org.quartz.JobDataMap;

/**
 * 流水线节点任务 接口
 *
 * @author odboy
 * @date 2025-07-21
 */
public interface PipelineNodeJobExecutor {
    PipelineNodeJobExecuteResult execute(JobDataMap contextArgs) throws Exception;
}
