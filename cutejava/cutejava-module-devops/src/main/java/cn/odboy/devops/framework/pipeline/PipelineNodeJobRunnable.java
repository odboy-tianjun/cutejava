package cn.odboy.devops.framework.pipeline;

import cn.odboy.framework.context.SpringBeanHolder;
import cn.odboy.devops.service.pipeline.PipelineInstanceNodeDetailService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class PipelineNodeJobRunnable implements Callable<Object> {
    private final PipelineNodeJobExecutor target;
    private final Map<String, Object> params;
    private final PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService;

    public PipelineNodeJobRunnable(String beanName, Map<String, Object> params, PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService) {
        this.target = SpringBeanHolder.getBean(beanName);
        this.params = params;
        this.pipelineInstanceNodeDetailService = pipelineInstanceNodeDetailService;
    }

    @Override
    @SuppressWarnings({"unchecked", "all"})
    public Object call() throws Exception {
        if (this.params == null) {
            return this.target.execute(new HashMap<>(1), this.pipelineInstanceNodeDetailService);
        }
        return this.target.execute(this.params, this.pipelineInstanceNodeDetailService);
    }
}
