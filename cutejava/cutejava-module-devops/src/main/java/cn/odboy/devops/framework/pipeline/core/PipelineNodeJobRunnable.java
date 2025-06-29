package cn.odboy.devops.framework.pipeline.core;

import cn.odboy.framework.context.SpringBeanHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class PipelineNodeJobRunnable implements Callable<Object> {
    private final PipelineNodeJobExecutor target;
    private final Map<String, Object> params;

    public PipelineNodeJobRunnable(String beanName, Map<String, Object> params) {
        this.target = SpringBeanHolder.getBean(beanName);
        this.params = params;
    }

    @Override
    @SuppressWarnings({"unchecked", "all"})
    public Object call() throws Exception {
        if (this.params == null) {
            return this.target.execute(new HashMap<>(1));
        }
        return this.target.execute(this.params);
    }
}
