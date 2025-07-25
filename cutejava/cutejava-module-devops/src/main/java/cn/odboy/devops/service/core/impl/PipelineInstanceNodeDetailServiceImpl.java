package cn.odboy.devops.service.core.impl;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeDetailTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.service.core.PipelineInstanceNodeDetailService;
import com.anwen.mongo.conditions.query.LambdaQueryChainWrapper;
import com.anwen.mongo.conditions.update.LambdaUpdateChainWrapper;
import com.anwen.mongo.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PipelineInstanceNodeDetailServiceImpl extends ServiceImpl<PipelineInstanceNodeDetailTb> implements PipelineInstanceNodeDetailService {

    @Override
    public void addLog(Long nodeId, String nodeCode, String stepName, PipelineStatusEnum status, String stepMsg, Date finishTime) {
        PipelineInstanceNodeDetailTb record = getPipelineInstanceNodeDetailByArgs(nodeId, nodeCode, stepName);
        if (record != null) {
            record.setStepStatus(status.getCode());
            record.setStepMsg(stepMsg);
            record.setFinishTime(finishTime);
            updateById(record);
            return;
        }
        record = new PipelineInstanceNodeDetailTb();
        record.setNodeId(nodeId);
        record.setStartTime(new Date());
        record.setNodeCode(nodeCode);
        record.setStepName(stepName);
        record.setStepStatus(status.getCode());
        record.setStepMsg(stepMsg);
        save(record);
    }

    @Override
    public PipelineInstanceNodeDetailTb getPipelineInstanceNodeDetailByArgs(Long nodeId, String nodeCode, String stepName) {
        return one(new LambdaQueryChainWrapper<>(getBaseMapper(), PipelineInstanceNodeDetailTb.class)
                .eq(PipelineInstanceNodeDetailTb::getNodeId, nodeId).eq(PipelineInstanceNodeDetailTb::getNodeCode, nodeCode)
                .eq(PipelineInstanceNodeDetailTb::getStepName, stepName)
        );
    }

    @Override
    public void removeByNodeIds(List<Long> nodeIds) {
        if (CollUtil.isNotEmpty(nodeIds)) {
            remove(new LambdaUpdateChainWrapper<>(getBaseMapper(), PipelineInstanceNodeDetailTb.class)
                    .in(PipelineInstanceNodeDetailTb::getNodeId, nodeIds)
            );
        }
    }

    @Override
    public PipelineInstanceNodeDetailTb getLastPipelineInstanceNodeDetailByArgs(PipelineInstanceNodeTb instanceNode) {
        List<PipelineInstanceNodeDetailTb> pipelineInstanceNodeDetailList = list(new LambdaQueryChainWrapper<>(getBaseMapper(), PipelineInstanceNodeDetailTb.class)
                .eq(PipelineInstanceNodeDetailTb::getNodeId, instanceNode.getId())
                .eq(PipelineInstanceNodeDetailTb::getNodeCode, instanceNode.getCode())
                .orderByDesc(PipelineInstanceNodeDetailTb::getStartTime)
        );
        // 明细为空，返回节点状态
        if (pipelineInstanceNodeDetailList.isEmpty()) {
            PipelineInstanceNodeDetailTb pipelineInstanceNodeDetailTb = new PipelineInstanceNodeDetailTb();
            pipelineInstanceNodeDetailTb.setStepMsg(instanceNode.getCurrentNodeMsg());
            pipelineInstanceNodeDetailTb.setStepStatus(instanceNode.getCurrentNodeStatus());
            return pipelineInstanceNodeDetailTb;
        }
        boolean existFail = pipelineInstanceNodeDetailList.stream().anyMatch(f -> PipelineStatusEnum.FAIL.getCode().equals(f.getStepStatus()));
        // 节点中存在失败的步骤
        if (existFail) {
            PipelineInstanceNodeDetailTb pipelineInstanceNodeDetailTb = new PipelineInstanceNodeDetailTb();
            pipelineInstanceNodeDetailTb.setStepMsg(instanceNode.getCurrentNodeMsg());
            pipelineInstanceNodeDetailTb.setStepStatus(instanceNode.getCurrentNodeStatus());
            return pipelineInstanceNodeDetailTb;
        }
        // 节点中所有步骤都成功
        long totalStep = pipelineInstanceNodeDetailList.size();
        long totalSuccessStep = pipelineInstanceNodeDetailList.stream().filter(f -> PipelineStatusEnum.SUCCESS.getCode().equals(f.getStepStatus())).count();
        if (totalSuccessStep == totalStep) {
            PipelineInstanceNodeDetailTb pipelineInstanceNodeDetailTb = new PipelineInstanceNodeDetailTb();
            pipelineInstanceNodeDetailTb.setStepMsg(instanceNode.getCurrentNodeMsg());
            pipelineInstanceNodeDetailTb.setStepStatus(instanceNode.getCurrentNodeStatus());
            return pipelineInstanceNodeDetailTb;
        }
        // 返回节点步骤状态
        PipelineInstanceNodeDetailTb pipelineInstanceNodeDetailTb = pipelineInstanceNodeDetailList.get(0);
        // 返回步骤说明
        pipelineInstanceNodeDetailTb.setStepMsg(pipelineInstanceNodeDetailTb.getStepName());
        return pipelineInstanceNodeDetailTb;
    }

    @Override
    public List<PipelineInstanceNodeDetailTb> queryPipelineInstanceNodeDetailByArgs(PipelineInstanceNodeTb instanceNode) {
        return list(new LambdaQueryChainWrapper<>(getBaseMapper(), PipelineInstanceNodeDetailTb.class)
                .eq(PipelineInstanceNodeDetailTb::getNodeId, instanceNode.getId())
                .eq(PipelineInstanceNodeDetailTb::getNodeCode, instanceNode.getCode())
                .orderByAsc(PipelineInstanceNodeDetailTb::getStartTime)
        );
    }
}
