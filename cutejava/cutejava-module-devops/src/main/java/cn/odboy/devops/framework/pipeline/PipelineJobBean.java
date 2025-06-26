package cn.odboy.devops.framework.pipeline;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.framework.context.SpringBeanHolder;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.dal.dataobject.pipeline.PipelineInstanceTb;
import cn.odboy.devops.service.pipeline.PipelineInstanceNodeDetailService;
import cn.odboy.devops.service.pipeline.PipelineInstanceService;
import cn.odboy.util.CsJsonUtil;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Slf4j
public class PipelineJobBean extends QuartzJobBean {
    private final ThreadPoolTaskExecutor executor = SpringBeanHolder.getBean("pipelineAsync");

    @Override
    public void executeInternal(JobExecutionContext context) {
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        // 实例、节点明细
        PipelineInstanceService pipelineInstanceService = SpringBeanHolder.getBean(PipelineInstanceService.class);
        PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService = SpringBeanHolder.getBean(PipelineInstanceNodeDetailService.class);
        // 获取流水线实例
        String pipelineInstanceId = mergedJobDataMap.getString(PipelineConst.INSTANCE_ID_KEY);
        PipelineInstanceTb pipelineInstance = pipelineInstanceService.getPipelineInstance(pipelineInstanceId);
        if (pipelineInstance == null) {
            log.info("流水线实例不存在, instanceId={}", pipelineInstanceId);
            return;
        }
        String pipelineTemplate = pipelineInstance.getPipelineTemplate();
        // 流水线节点模板
        List<PipelineNodeTemplateVo> pipelineNodeTemplateVos = JSON.parseArray(pipelineTemplate, PipelineNodeTemplateVo.class);
        // 流水线上下文参数
        Map<String, Object> contextArgs = CsJsonUtil.toMap(pipelineInstance.getContext(), String.class, Object.class);
        // 放置额外的参数
        contextArgs.put(PipelineConst.INSTANCE_KEY, pipelineInstance);
        // 执行节点任务
        for (int i = 0; i < pipelineNodeTemplateVos.size(); i++) {
            PipelineNodeTemplateVo pipelineNodeTemplateVo = pipelineNodeTemplateVos.get(i);
            String code = pipelineNodeTemplateVo.getCode();
            contextArgs.put(PipelineConst.INSTANCE_NODE_INDEX_KEY, i);
            contextArgs.put(PipelineConst.INSTANCE_NODE_CODE_KEY, code);
            contextArgs.put(PipelineConst.INSTANCE_NODE_NAME_KEY, pipelineNodeTemplateVo.getName());
            contextArgs.put(PipelineConst.INSTANCE_NODE_TEMPLATE_ARGS_KEY, pipelineNodeTemplateVo.getParameters());
            try {
                List<String> allServiceBeanName = SpringBeanHolder.getAllServiceBeanName();
                System.err.println(allServiceBeanName);
                PipelineNodeJobRunnable pipelineNodeJobRunnable = new PipelineNodeJobRunnable(
                        PipelineConst.EXECUTOR_PREFIX + code,
                        contextArgs,
                        pipelineInstanceNodeDetailService
                );
                Future<?> future = executor.submit(pipelineNodeJobRunnable);
                PipelineNodeJobExecuteResult executeResult = (PipelineNodeJobExecuteResult) future.get();
                System.err.println(executeResult);
            } catch (Exception e) {
                log.error("流水线节点任务执行失败", e);
            }
        }
    }
}
