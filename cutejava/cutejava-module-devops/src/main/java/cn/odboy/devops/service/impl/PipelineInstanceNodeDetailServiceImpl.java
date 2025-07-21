package cn.odboy.devops.service.impl;

import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeDetailTb;
import cn.odboy.devops.service.PipelineInstanceNodeDetailService;
import com.anwen.mongo.conditions.query.LambdaQueryChainWrapper;
import com.anwen.mongo.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class PipelineInstanceNodeDetailServiceImpl extends ServiceImpl<PipelineInstanceNodeDetailTb> implements PipelineInstanceNodeDetailService {

    public void addLog(String nodeId, Integer nodeIndex, String nodeCode, String stepName, PipelineStatusEnum status, String stepMsg) {
        PipelineInstanceNodeDetailTb record = getPipelineInstanceNodeDetailByArgs(nodeId, nodeCode, stepName);
        if (record != null) {
            record.setStepStatus(status.getCode());
            record.setStepMsg(stepMsg);
            updateById(record);
            return;
        }
        record = new PipelineInstanceNodeDetailTb();
        record.setNodeId(nodeId);
        record.setStartTime(new Date());
        record.setNodeIndex(nodeIndex);
        record.setNodeCode(nodeCode);
        record.setStepName(stepName);
        record.setStepStatus(status.getCode());
        record.setStepMsg(stepMsg);
        save(record);
    }

    public PipelineInstanceNodeDetailTb getPipelineInstanceNodeDetailByArgs(String nodeId, String nodeCode, String stepName) {
        return one(new LambdaQueryChainWrapper<>(getBaseMapper(), PipelineInstanceNodeDetailTb.class)
                .eq(PipelineInstanceNodeDetailTb::getNodeId, nodeId).eq(PipelineInstanceNodeDetailTb::getNodeCode, nodeCode)
                .eq(PipelineInstanceNodeDetailTb::getStepName, stepName)
        );
    }
}
