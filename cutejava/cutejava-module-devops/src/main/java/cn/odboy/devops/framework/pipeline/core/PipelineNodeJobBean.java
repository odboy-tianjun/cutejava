package cn.odboy.devops.framework.pipeline.core;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.framework.context.SpringBeanHolder;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

/**
 * 流水线任务载体
 *
 * @author odboy
 * @date 2025-07-19
 */
@Slf4j
public class PipelineNodeJobBean implements InterruptableJob {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        PipelineNodeTemplateVo currentNodeTemplate = (PipelineNodeTemplateVo) jobDataMap.get(PipelineConst.CURRENT_NODE_TEMPLATE);
        String serviceName = PipelineConst.EXECUTOR_PREFIX + currentNodeTemplate.getCode();
        PipelineNodeJobExecutor pipelineNodeJobExecutor = SpringBeanHolder.getBean(serviceName);
        pipelineNodeJobExecutor.execute(jobDataMap);
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {

    }
}
