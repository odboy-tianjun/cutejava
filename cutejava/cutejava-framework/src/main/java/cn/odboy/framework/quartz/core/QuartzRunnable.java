package cn.odboy.framework.quartz.core;

import cn.odboy.framework.context.CsSpringBeanHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 执行定时任务
 */
@Slf4j
public class QuartzRunnable implements Callable<Object> {
    private final Object target;
    private final Method method;
    private final String params;

    public QuartzRunnable(String beanName, String methodName, String params) throws NoSuchMethodException, SecurityException {
        this.target = CsSpringBeanHolder.getBean(beanName);
        this.params = params;
        if (StringUtils.isNotBlank(params)) {
            this.method = target.getClass().getDeclaredMethod(methodName, String.class);
        } else {
            this.method = target.getClass().getDeclaredMethod(methodName);
        }
    }

    @Override
    @SuppressWarnings({"unchecked", "all"})
    public Object call() throws Exception {
        ReflectionUtils.makeAccessible(method);
        if (StringUtils.isNotBlank(params)) {
            return method.invoke(target, params);
        } else {
            return method.invoke(target);
        }
    }
}
