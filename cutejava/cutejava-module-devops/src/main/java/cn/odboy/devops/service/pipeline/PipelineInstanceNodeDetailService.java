package cn.odboy.devops.service.pipeline;

import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.pipeline.PipelineInstanceNodeDetailTb;
import cn.odboy.devops.dal.mysql.pipeline.PipelineInstanceNodeDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 流水线实例节点明细 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-06-26
 */
@Service
@RequiredArgsConstructor
public class PipelineInstanceNodeDetailService {
    private final PipelineInstanceNodeDetailMapper pipelineInstanceNodeDetailMapper;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void addLog(Long pipelineInstanceId, Map<String, Object> contextArgs, String stepName, PipelineStatusEnum status, String stepMsg) {
        Integer nodeIndex = (Integer) contextArgs.getOrDefault(PipelineConst.INSTANCE_NODE_INDEX_KEY, null);
        String nodeCode = (String) contextArgs.getOrDefault(PipelineConst.INSTANCE_NODE_CODE_KEY, null);
        PipelineInstanceNodeDetailTb record = getPipelineInstanceNodeDetailByArgs(pipelineInstanceId, nodeCode, stepName);
        if (record == null) {
            record = new PipelineInstanceNodeDetailTb();
            record.setPipelineInstanceId(pipelineInstanceId);
            record.setStartTime(new Date());
            record.setNodeIndex(nodeIndex);
            record.setNodeCode(nodeCode);
            record.setStepName(stepName);
            record.setStepStatus(status.getCode());
            pipelineInstanceNodeDetailMapper.insert(record);
        } else {
            PipelineInstanceNodeDetailTb updRecord = new PipelineInstanceNodeDetailTb();
            updRecord.setId(record.getId());
            updRecord.setStepStatus(status.getCode());
            pipelineInstanceNodeDetailMapper.updateById(updRecord);
        }
    }

    private PipelineInstanceNodeDetailTb getPipelineInstanceNodeDetailByArgs(Long pipelineInstanceId, String nodeCode, String stepName) {
        return pipelineInstanceNodeDetailMapper.selectOne(new LambdaQueryWrapper<PipelineInstanceNodeDetailTb>()
                .eq(PipelineInstanceNodeDetailTb::getPipelineInstanceId, pipelineInstanceId)
                .eq(PipelineInstanceNodeDetailTb::getNodeCode, nodeCode)
                .eq(PipelineInstanceNodeDetailTb::getStepName, stepName)
        );
    }

}
