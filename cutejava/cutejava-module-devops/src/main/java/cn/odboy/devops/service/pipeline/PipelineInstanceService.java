package cn.odboy.devops.service.pipeline;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.pipeline.PipelineInstanceTb;
import cn.odboy.devops.dal.mysql.pipeline.PipelineInstanceMapper;
import cn.odboy.devops.framework.pipeline.core.PipelineNodeJobRunnable;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeJobExecuteResult;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.util.CsJsonUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * <p>
 * 流水线实例 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-06-26
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Lazy)
public class PipelineInstanceService {
    private final PipelineInstanceMapper pipelineInstanceMapper;

    public PipelineInstanceTb getPipelineInstance(long pipelineInstanceId) {
        return pipelineInstanceMapper.selectOne(new LambdaQueryWrapper<PipelineInstanceTb>()
                .eq(PipelineInstanceTb::getPipelineInstanceId, pipelineInstanceId)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void createPipelineInstance(PipelineInstanceTb pipelineInstanceTb) {
        pipelineInstanceMapper.insert(pipelineInstanceTb);
    }

    /**
     * 新事务中执行，就算抛出异常，也不会丢失执行状态
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateStatusByInstanceId(PipelineStatusEnum status, long pipelineInstanceId) {
        pipelineInstanceMapper.updateStatusByInstanceId(status.getCode(), pipelineInstanceId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void startPipeline(ThreadPoolTaskExecutor executor, JobExecutionContext context) {
        JobDataMap mergedJobDataMap = context.getMergedJobDataMap();
        // 获取流水线实例
        long pipelineInstanceId = mergedJobDataMap.getLong(PipelineConst.INSTANCE_ID_KEY);
        PipelineInstanceTb pipelineInstance = getPipelineInstance(pipelineInstanceId);
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
        // 任务重试标记
        Object retryNodeIndexObj = mergedJobDataMap.getOrDefault(PipelineConst.INSTANCE_RETRY_NODE_INDEX_KEY, null);
        Integer retryNodeIndexValue = null;
        if (retryNodeIndexObj != null) {
            retryNodeIndexValue = (Integer) retryNodeIndexObj;
        }
        // 初始化节点明细
        boolean isExecuteSuccess = true;
        updateStatusByInstanceId(PipelineStatusEnum.RUNNING, pipelineInstanceId);
        for (int i = 0; i < pipelineNodeTemplateVos.size(); i++) {
            if (retryNodeIndexValue != null && i < retryNodeIndexValue) {
                // 跳过非重试节点
                continue;
            }
            try {
                PipelineNodeTemplateVo pipelineNodeTemplateVo = pipelineNodeTemplateVos.get(i);
                String code = pipelineNodeTemplateVo.getCode();
                contextArgs.put(PipelineConst.INSTANCE_NODE_INDEX_KEY, i);
                contextArgs.put(PipelineConst.INSTANCE_NODE_CODE_KEY, code);
                contextArgs.put(PipelineConst.INSTANCE_NODE_NAME_KEY, pipelineNodeTemplateVo.getName());
                contextArgs.put(PipelineConst.INSTANCE_NODE_TEMPLATE_ARGS_KEY, pipelineNodeTemplateVo.getParameters());
                // 执行任务
                PipelineNodeJobRunnable pipelineNodeJobRunnable = new PipelineNodeJobRunnable(PipelineConst.EXECUTOR_PREFIX + code, contextArgs);
                Future<?> future = executor.submit(pipelineNodeJobRunnable);
                PipelineNodeJobExecuteResult executeResult = (PipelineNodeJobExecuteResult) future.get();
                updateCurrentNodeByInstanceId(code, executeResult.getStatus(), pipelineInstanceId);
                if (!PipelineStatusEnum.SUCCESS.equals(executeResult.getStatus())) {
                    throw new BadRequestException(executeResult.getMessage());
                }
            } catch (Exception e) {
                log.error("流水线节点任务执行失败", e);
                isExecuteSuccess = false;
                break;
            }
        }
        // 任务执行结果
        updateStatusByInstanceId(isExecuteSuccess ? PipelineStatusEnum.SUCCESS : PipelineStatusEnum.FAIL, pipelineInstanceId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateCurrentNodeByInstanceId(String code, PipelineStatusEnum status, long pipelineInstanceId) {
        pipelineInstanceMapper.updateCurrentNodeByInstanceId(code, status.getCode(), pipelineInstanceId);
    }
}
