package cn.odboy.devops.service.impl;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.dal.mysql.PipelineInstanceMapper;
import cn.odboy.devops.dal.redis.PipelineInstanceDAO;
import cn.odboy.devops.framework.pipeline.core.PipelineJobManage;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.PipelineInstanceNodeDetailService;
import cn.odboy.devops.service.PipelineInstanceNodeService;
import cn.odboy.devops.service.PipelineInstanceService;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineInstanceServiceImpl implements PipelineInstanceService {
    private final PipelineJobManage pipelineJobManage;
    private final PipelineInstanceMapper pipelineInstanceMapper;
    private final PipelineInstanceNodeService pipelineInstanceNodeService;
    private final PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService;
    private final PipelineInstanceDAO pipelineInstanceDAO;

    @Override
    public void start(PipelineInstanceTb pipelineInstanceTb) {
        if (pipelineInstanceDAO.lock(pipelineInstanceTb)) {
            throw new BadRequestException("流水线运行中，无法重复执行");
        }
        ThreadUtil.execAsync(() -> innerStart(pipelineInstanceTb));
    }

    public void innerStart(PipelineInstanceTb pipelineInstanceTb) {
        // 雪花id
        pipelineInstanceTb.setInstanceId(IdUtil.getSnowflakeNextId());
        pipelineInstanceTb.setStatus(PipelineStatusEnum.PENDING.getCode());
        // 创建实例
        pipelineInstanceMapper.insert(pipelineInstanceTb);
        // 创建实例明细
        pipelineInstanceNodeService.createPipelineInstanceDetail(pipelineInstanceTb);
        // 流水线节点模板
        String templateContent = pipelineInstanceTb.getTemplateContent();
        List<PipelineNodeTemplateVo> pipelineNodeTemplateVos = JSON.parseArray(templateContent, PipelineNodeTemplateVo.class);
        Long instanceId = pipelineInstanceTb.getInstanceId();
        // 流水线实例状态(默认是成功的)
        PipelineStatusEnum pipelineStatusEnum = PipelineStatusEnum.SUCCESS;
        try {
            pipelineInstanceTb.setStatus(PipelineStatusEnum.RUNNING.getCode());
            pipelineInstanceMapper.updateById(pipelineInstanceTb);
            // 依次执行流水线节点任务
            for (PipelineNodeTemplateVo pipelineNodeTemplateVo : pipelineNodeTemplateVos) {
                try {
                    pipelineJobManage.startJob(pipelineInstanceTb, pipelineNodeTemplateVo);
                    pipelineInstanceTb.setCurrentNode(pipelineNodeTemplateVo.getCode());
                    pipelineInstanceTb.setCurrentNodeStatus(PipelineStatusEnum.RUNNING.getCode());
                    pipelineInstanceMapper.updateById(pipelineInstanceTb);
                    // 监听节点执行状态
                    while (true) {
                        ThreadUtil.safeSleep(2000);
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
                    ThreadUtil.safeSleep(2000);
                } catch (Exception e) {
                    log.error("流水线节点运行失败", e);
                    pipelineInstanceTb.setStatus(PipelineStatusEnum.FAIL.getCode());
                    pipelineInstanceMapper.updateById(pipelineInstanceTb);
                }
            }
            pipelineInstanceTb.setStatus(pipelineStatusEnum.getCode());
            pipelineInstanceMapper.updateById(pipelineInstanceTb);
            pipelineInstanceDAO.unLock(pipelineInstanceTb);
        } catch (Exception e) {
            log.error("流水线运行失败", e);
            pipelineInstanceTb.setStatus(PipelineStatusEnum.FAIL.getCode());
            pipelineInstanceMapper.updateById(pipelineInstanceTb);
            pipelineInstanceDAO.unLock(pipelineInstanceTb);
        }
    }

    public void restart(PipelineInstanceTb pipelineInstanceTb) {
        boolean exist = pipelineInstanceDAO.lock(pipelineInstanceTb);
        if (exist) {
            if (!PipelineStatusEnum.FAIL.getCode().equals(pipelineInstanceTb.getCurrentNodeStatus())) {
                throw new BadRequestException("流水线执行中，无法重启流水线");
            }
            log.info("解锁流水线, {}", JSON.toJSONString(pipelineInstanceTb));
            pipelineInstanceDAO.unLock(pipelineInstanceTb);
        }
    }
}
