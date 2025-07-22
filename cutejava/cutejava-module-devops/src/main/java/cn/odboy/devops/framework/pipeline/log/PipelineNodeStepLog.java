package cn.odboy.devops.framework.pipeline.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 流水线节点明细日志 切面
 *
 * @author odboy
 * @date 2025-07-22
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PipelineNodeStepLog {
    String value() default "默认步骤名";
}