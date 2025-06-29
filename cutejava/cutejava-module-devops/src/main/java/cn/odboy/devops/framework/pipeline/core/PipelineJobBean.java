package cn.odboy.devops.framework.pipeline.core;

import cn.odboy.devops.service.pipeline.PipelineInstanceService;
import cn.odboy.framework.context.SpringBeanHolder;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

@Slf4j
public class PipelineJobBean extends QuartzJobBean {
    private final ThreadPoolTaskExecutor executor = SpringBeanHolder.getBean("pipelineAsync");
    private final PipelineInstanceService pipelineInstanceService = SpringBeanHolder.getBean(PipelineInstanceService.class);

    @Override
    public void executeInternal(JobExecutionContext context) {
        pipelineInstanceService.startPipeline(executor, context);
    }
}
