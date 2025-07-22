package cn.odboy.devops.framework.pipeline.core;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.dal.mysql.PipelineInstanceMapper;
import cn.odboy.devops.dal.redis.PipelineInstanceDAO;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.PipelineInstanceNodeService;
import cn.odboy.framework.context.SpringBeanHolder;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.List;

/**
 * 流水线任务载体
 *
 * @author odboy
 * @date 2025-07-19
 */
@Slf4j
public class PipelineJobBean implements InterruptableJob {
    private volatile boolean interrupted = false;
    private volatile Long currentInstanceId = null;
    private volatile String currentNodeCode = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 获取服务
        PipelineInstanceMapper pipelineInstanceMapper = SpringBeanHolder.getBean(PipelineInstanceMapper.class);
        PipelineInstanceNodeService pipelineInstanceNodeService = SpringBeanHolder.getBean(PipelineInstanceNodeService.class);
        PipelineInstanceDAO pipelineInstanceDAO = SpringBeanHolder.getBean(PipelineInstanceDAO.class);
        PipelineJobManage pipelineJobManage = SpringBeanHolder.getBean(PipelineJobManage.class);
        PipelineNodeJobManage pipelineNodeJobManage = SpringBeanHolder.getBean(PipelineNodeJobManage.class);

        // 参数解析
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        String retryNodeCode = jobDataMap.getString(PipelineConst.RETRY_NODE_CODE);
        PipelineInstanceTb pipelineInstanceTb = (PipelineInstanceTb) jobDataMap.get(PipelineConst.INSTANCE);
        pipelineInstanceTb.setStatus(PipelineStatusEnum.PENDING.getCode());

        //        // 模拟终止流水线
//        ThreadUtil.execAsync(() -> {
//            ThreadUtil.safeSleep(6000);
//            try {
//                pipelineJobManage.interruptJob(pipelineInstanceTb.getInstanceId());
//            } catch (SchedulerException e) {
//                log.error("终止流水线异常", e);
//            }
//        });

        if (StrUtil.isBlank(retryNodeCode)) {
            // 创建实例
            pipelineInstanceMapper.insert(pipelineInstanceTb);
            // 创建实例明细
            pipelineInstanceNodeService.createPipelineInstanceDetail(pipelineInstanceTb);
        } else {
            // 重建实例明细
            pipelineInstanceNodeService.remakePipelineInstanceDetail(pipelineInstanceTb, retryNodeCode);
        }

        // 实例节点模板
        String templateContent = pipelineInstanceTb.getTemplateContent();
        List<PipelineNodeTemplateVo> pipelineNodeTemplateVos = JSON.parseArray(templateContent, PipelineNodeTemplateVo.class);
        Long instanceId = pipelineInstanceTb.getInstanceId();

        // 实例状态(默认是成功的)
        PipelineStatusEnum pipelineStatusEnum = PipelineStatusEnum.SUCCESS;

        try {
            // 更新实例状态(running)
            pipelineInstanceTb.setStatus(PipelineStatusEnum.RUNNING.getCode());
            pipelineInstanceMapper.updateById(pipelineInstanceTb);

            // 依次执行流水线节点任务
            boolean skipFlag = true;
            boolean foundRetryNode = false;
            this.currentInstanceId = pipelineInstanceTb.getInstanceId();
            for (PipelineNodeTemplateVo pipelineNodeTemplateVo : pipelineNodeTemplateVos) {
                this.currentNodeCode = pipelineNodeTemplateVo.getCode();

                // 如果 retryNodeCode 不为空，进入跳过逻辑
                if (StrUtil.isNotBlank(retryNodeCode)) {
                    if (skipFlag && !pipelineNodeTemplateVo.getCode().equals(retryNodeCode)) {
                        // 如果当前节点不是重试节点，且 skipFlag 为 true，则跳过
                        continue;
                    } else {
                        // 找到重试节点，开始执行
                        skipFlag = false;
                        foundRetryNode = true;
                    }
                }

                if (StrUtil.isNotBlank(retryNodeCode) && !foundRetryNode) {
                    log.error("未找到重试节点: {}", retryNodeCode);
                    throw new BadRequestException("未找到重试节点: " + retryNodeCode);
                }

                try {
                    pipelineNodeJobManage.startJob(pipelineInstanceTb, pipelineNodeTemplateVo);
                    pipelineInstanceTb.setCurrentNode(pipelineNodeTemplateVo.getCode());
                    pipelineInstanceTb.setCurrentNodeStatus(PipelineStatusEnum.RUNNING.getCode());
                    pipelineInstanceMapper.updateById(pipelineInstanceTb);

                    // 监听节点执行状态
                    while (true) {
                        // 检查中断状态
                        // if (Thread.currentThread().isInterrupted() || interrupted) {
                        if (interrupted) {
                            log.error("检测到中断，停止流水线执行");
                            pipelineStatusEnum = PipelineStatusEnum.FAIL;
                            pipelineInstanceTb.setCurrentNode(pipelineNodeTemplateVo.getCode());
                            pipelineInstanceTb.setCurrentNodeStatus(pipelineStatusEnum.getCode());
                            pipelineInstanceMapper.updateById(pipelineInstanceTb);
                            break;
                        }
                        ThreadUtil.safeSleep(5000);
                        PipelineInstanceNodeTb pipelineInstanceNode = pipelineInstanceNodeService.getPipelineInstanceNodeByArgs(instanceId, pipelineNodeTemplateVo.getCode());
                        if (PipelineStatusEnum.SUCCESS.getCode().equals(pipelineInstanceNode.getCurrentNodeStatus())) {
                            pipelineStatusEnum = PipelineStatusEnum.SUCCESS;
                            pipelineInstanceTb.setCurrentNodeStatus(pipelineStatusEnum.getCode());
                            pipelineInstanceMapper.updateById(pipelineInstanceTb);
                            break;
                        }
                        if (PipelineStatusEnum.FAIL.getCode().equals(pipelineInstanceNode.getCurrentNodeStatus())) {
                            pipelineStatusEnum = PipelineStatusEnum.FAIL;
                            pipelineInstanceTb.setCurrentNodeStatus(pipelineStatusEnum.getCode());
                            pipelineInstanceMapper.updateById(pipelineInstanceTb);
                            break;
                        }
                    }

                    if (pipelineStatusEnum.equals(PipelineStatusEnum.FAIL)) {
                        // 节点失败，全失败
                        break;
                    }
                } catch (Exception e) {
                    log.error("流水线节点运行失败", e);
                    pipelineInstanceTb.setStatus(PipelineStatusEnum.FAIL.getCode());
                    pipelineInstanceMapper.updateById(pipelineInstanceTb);
                }
            }

            // 更新实例状态
            pipelineInstanceTb.setStatus(pipelineStatusEnum.getCode());
            pipelineInstanceMapper.updateById(pipelineInstanceTb);
            pipelineInstanceDAO.unLock(pipelineInstanceTb);
        } catch (Exception e) {
            log.error("流水线运行失败", e);
            pipelineInstanceTb.setStatus(PipelineStatusEnum.FAIL.getCode());
            pipelineInstanceMapper.updateById(pipelineInstanceTb);
            pipelineInstanceDAO.unLock(pipelineInstanceTb);
        } finally {
            pipelineJobManage.deleteJob(instanceId);
        }
    }

    /**
     * 流水线任务已中断
     */
    @Override
    public void interrupt() throws UnableToInterruptJobException {
        this.interrupted = true;
        if (this.currentInstanceId != null && StrUtil.isNotBlank(this.currentNodeCode)) {
            PipelineInstanceMapper pipelineInstanceMapper = SpringBeanHolder.getBean(PipelineInstanceMapper.class);
            PipelineInstanceDAO pipelineInstanceDAO = SpringBeanHolder.getBean(PipelineInstanceDAO.class);

            int seconds = 30_000;
            log.info("中断信号已发出，等待最多 {} 秒，若未自行结束则强制终止线程", seconds);
            long waitStart = System.currentTimeMillis();
            while ((System.currentTimeMillis() - waitStart) < seconds) {
                if (Thread.currentThread().isInterrupted()) {
                    log.info("线程已自行中断");
                    return;
                }
                ThreadUtil.safeSleep(1000);
            }

            log.info("等待超时，强制终止线程");
            PipelineInstanceTb pipelineInstanceTb = pipelineInstanceMapper.selectById(this.currentInstanceId);
            if (pipelineInstanceTb != null) {
                // 更新状态 & 解锁
                pipelineInstanceTb.setCurrentNode(this.currentNodeCode);
                pipelineInstanceTb.setCurrentNodeStatus(PipelineStatusEnum.FAIL.getCode());
                pipelineInstanceTb.setStatus(PipelineStatusEnum.FAIL.getCode());
                pipelineInstanceMapper.updateById(pipelineInstanceTb);
                pipelineInstanceDAO.unLock(pipelineInstanceTb);

                // 杀死
                try {
                    log.info("流水线实例线程被杀死, instanceId={}", this.currentInstanceId);
                    Thread.currentThread().stop();
                } catch (Exception e) {
                    log.error("流水线实例线程杀死失败", e);
                }
            }
        }
    }
}
