package cn.odboy.devops.framework.pipeline.log;

import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.framework.pipeline.AbstractPipelineNodeJobService;
import cn.odboy.framework.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;


@Slf4j
@Aspect
@Component
public class PipelineNodeStepLogAspect extends AbstractPipelineNodeJobService {
    /**
     * 配置切入点
     */
    @Pointcut("@annotation(cn.odboy.devops.framework.pipeline.log.PipelineNodeStepLog)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param joinPoint join point for advice
     */
    @Around("logPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        PipelineInstanceNodeTb currentNodeInfo = null;
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof PipelineInstanceNodeTb) {
                currentNodeInfo = (PipelineInstanceNodeTb) arg;
                break;
            }
        }
        PipelineNodeStepLog pipelineNodeStepLog = method.getAnnotation(PipelineNodeStepLog.class);
        try {
            addPipelineInstanceNodeDetailLog(currentNodeInfo, pipelineNodeStepLog.value(), PipelineStatusEnum.RUNNING, PipelineStatusEnum.RUNNING.getDesc(), null);
            Object result = joinPoint.proceed();
            addPipelineInstanceNodeDetailLog(currentNodeInfo, pipelineNodeStepLog.value(), PipelineStatusEnum.SUCCESS, PipelineStatusEnum.SUCCESS.getDesc(), new Date());
            return result;
        } catch (BadRequestException e) {
            log.error("执行失败", e);
            addPipelineInstanceNodeDetailLog(currentNodeInfo, pipelineNodeStepLog.value(), PipelineStatusEnum.FAIL, e.getMessage(), new Date());
            throw e;
        } catch (Throwable e) {
            log.error("执行失败", e);
            addPipelineInstanceNodeDetailLog(currentNodeInfo, pipelineNodeStepLog.value(), PipelineStatusEnum.FAIL, PipelineStatusEnum.FAIL.getDesc(), new Date());
            throw e;
        }
    }
}
