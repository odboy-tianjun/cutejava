/*
 *  Copyright 2021-2025 Odboy
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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
import cn.odboy.devops.service.core.PipelineInstanceNodeService;
import cn.odboy.framework.context.CsSpringBeanHolder;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;

import java.util.Date;
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
    private volatile Thread executingThread = null;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 保存当前执行线程引用
        this.executingThread = Thread.currentThread();

        // 参数解析
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        PipelineInstanceTb pipelineInstanceTb = (PipelineInstanceTb) jobDataMap.get(PipelineConst.INSTANCE);
        pipelineInstanceTb.setStatus(PipelineStatusEnum.PENDING.getCode());
        // 获取服务
        PipelineJobManage pipelineJobManage = CsSpringBeanHolder.getBean(PipelineJobManage.class);

        try {
            doExecute(jobDataMap, pipelineInstanceTb, pipelineJobManage);
        } finally {
            // 清理所有实例变量
            cleanupInstanceVariables();
            // 删除任务
            Long instanceId = pipelineInstanceTb.getInstanceId();
            if (instanceId != null) {
                pipelineJobManage.deleteJob(instanceId);
            }
        }
    }

    private void cleanupInstanceVariables() {
        this.executingThread = null;
        this.currentInstanceId = null;
        this.currentNodeCode = null;
        this.interrupted = false;
    }

    private void doExecute(JobDataMap jobDataMap, PipelineInstanceTb pipelineInstanceTb, PipelineJobManage pipelineJobManage) {
        // 参数解析
        String retryNodeCode = jobDataMap.getString(PipelineConst.RETRY_NODE_CODE);
        // 获取服务
        PipelineInstanceMapper pipelineInstanceMapper = CsSpringBeanHolder.getBean(PipelineInstanceMapper.class);
        PipelineInstanceNodeService pipelineInstanceNodeService = CsSpringBeanHolder.getBean(PipelineInstanceNodeService.class);
        PipelineInstanceDAO pipelineInstanceDAO = CsSpringBeanHolder.getBean(PipelineInstanceDAO.class);
        PipelineNodeJobManage pipelineNodeJobManage = CsSpringBeanHolder.getBean(PipelineNodeJobManage.class);

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
                    // 流水线正式启动
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
                        // 检查实例节点状态
                        ThreadUtil.safeSleep(3000);
                        PipelineInstanceNodeTb runningInstanceNode = pipelineInstanceNodeService.getPipelineInstanceNodeByArgs(instanceId, pipelineNodeTemplateVo.getCode());
                        if (PipelineStatusEnum.SUCCESS.getCode().equals(runningInstanceNode.getCurrentNodeStatus())) {
                            pipelineStatusEnum = PipelineStatusEnum.SUCCESS;
                            pipelineInstanceTb.setCurrentNodeStatus(pipelineStatusEnum.getCode());
                            pipelineInstanceMapper.updateById(pipelineInstanceTb);
                            break;
                        }
                        if (PipelineStatusEnum.FAIL.getCode().equals(runningInstanceNode.getCurrentNodeStatus())) {
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
        log.info("收到中断信号，准备强制终止流水线任务");
        this.interrupted = true;

        // 直接强制终止线程
        if (this.executingThread != null && this.executingThread.isAlive()) {
            log.warn("正在强制终止线程，instanceId={}", this.currentInstanceId);

            // 更新流水线实例状态
            updateInstanceStatusToFail();

            // 直接停止线程
            try {
                this.executingThread.stop();
                log.info("线程已强制终止，instanceId={}", this.currentInstanceId);
            } catch (ThreadDeath td) {
                // Thread.stop()会抛出ThreadDeath异常，这是正常的
                log.info("线程终止完成，instanceId={}", this.currentInstanceId);
            } catch (Exception e) {
                log.error("强制终止线程时发生异常", e);
            }
        }
    }

    private void updateInstanceStatusToFail() {
        if (this.currentInstanceId != null) {
            PipelineInstanceMapper pipelineInstanceMapper = CsSpringBeanHolder.getBean(PipelineInstanceMapper.class);
            PipelineInstanceDAO pipelineInstanceDAO = CsSpringBeanHolder.getBean(PipelineInstanceDAO.class);
            PipelineInstanceTb pipelineInstanceTb = pipelineInstanceMapper.selectById(this.currentInstanceId);
            if (pipelineInstanceTb != null) {
                // 更新状态 & 解锁
                if (StrUtil.isNotBlank(this.currentNodeCode)) {
                    pipelineInstanceTb.setCurrentNode(this.currentNodeCode);
                    pipelineInstanceTb.setCurrentNodeStatus(PipelineStatusEnum.FAIL.getCode());
                    // 更新节点状态
                    PipelineInstanceNodeService pipelineInstanceNodeService = CsSpringBeanHolder.getBean(PipelineInstanceNodeService.class);
                    pipelineInstanceNodeService.finishPipelineInstanceNodeByArgs(
                            this.currentInstanceId,
                            this.currentNodeCode,
                            PipelineStatusEnum.FAIL,
                            "用户主动停止",
                            new Date()
                    );
                }
                pipelineInstanceTb.setStatus(PipelineStatusEnum.FAIL.getCode());
                // 更新实例状态
                pipelineInstanceMapper.updateById(pipelineInstanceTb);
                pipelineInstanceDAO.unLock(pipelineInstanceTb);
                log.info("流水线实例被解锁, instanceId={}", this.currentInstanceId);
            }
        }
    }
}
