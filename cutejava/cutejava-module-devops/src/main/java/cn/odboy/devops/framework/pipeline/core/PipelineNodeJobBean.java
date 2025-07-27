package cn.odboy.devops.framework.pipeline.core;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.core.PipelineInstanceNodeService;
import cn.odboy.framework.context.CsSpringBeanHolder;
import cn.odboy.framework.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * 流水线任务载体
 *
 * @author odboy
 * @date 2025-07-19
 */
@Slf4j
public class PipelineNodeJobBean implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        PipelineInstanceNodeService pipelineInstanceNodeService = CsSpringBeanHolder.getBean(PipelineInstanceNodeService.class);

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        long instanceId = jobDataMap.getLong(PipelineConst.INSTANCE_ID);
        PipelineNodeTemplateVo currentNodeTemplate = (PipelineNodeTemplateVo) jobDataMap.get(PipelineConst.CURRENT_NODE_TEMPLATE);
        String nodeCode = currentNodeTemplate.getCode();

        try {
            String serviceName = PipelineConst.EXECUTOR_PREFIX + nodeCode;
            PipelineNodeJobExecutor pipelineNodeJobExecutor = CsSpringBeanHolder.getBean(serviceName);
            pipelineInstanceNodeService.updatePipelineInstanceNodeByArgs(instanceId, nodeCode, PipelineStatusEnum.RUNNING, PipelineStatusEnum.RUNNING.getDesc());
            PipelineNodeJobExecuteResult executeResult = pipelineNodeJobExecutor.execute(jobDataMap);
            jobExecutionContext.getMergedJobDataMap().put(PipelineConst.LAST_NODE_RESULT, executeResult);
            pipelineInstanceNodeService.finishPipelineInstanceNodeByArgs(instanceId, nodeCode, PipelineStatusEnum.SUCCESS, PipelineStatusEnum.SUCCESS.getDesc(), new Date());
        } catch (BadRequestException e) {
            log.error("流水线节点执行异常", e);
            pipelineInstanceNodeService.finishPipelineInstanceNodeByArgs(instanceId, nodeCode, PipelineStatusEnum.FAIL, e.getMessage(), new Date());
        } catch (Exception e) {
            log.error("流水线节点执行异常", e);
            pipelineInstanceNodeService.finishPipelineInstanceNodeByArgs(instanceId, nodeCode, PipelineStatusEnum.FAIL, PipelineStatusEnum.FAIL.getDesc(), new Date());
        }
    }
}
