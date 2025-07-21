package cn.odboy.devops.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.PipelineInstanceNodeService;
import com.alibaba.fastjson2.JSON;
import com.anwen.mongo.conditions.query.LambdaQueryChainWrapper;
import com.anwen.mongo.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class PipelineInstanceNodeServiceImpl extends ServiceImpl<PipelineInstanceNodeTb> implements PipelineInstanceNodeService {
    @Override
    public void createPipelineInstanceDetail(PipelineInstanceTb pipelineInstanceTb) {
        String templateContent = pipelineInstanceTb.getTemplateContent();
        List<PipelineNodeTemplateVo> pipelineNodeTemplateVos = JSON.parseArray(templateContent, PipelineNodeTemplateVo.class);
        if (CollUtil.isNotEmpty(pipelineNodeTemplateVos)) {
            List<PipelineInstanceNodeTb> records = new ArrayList<>();
            for (PipelineNodeTemplateVo pipelineNodeTemplateVo : pipelineNodeTemplateVos) {
                PipelineInstanceNodeTb record = new PipelineInstanceNodeTb();
                record.setInstanceId(pipelineInstanceTb.getInstanceId());
                record.setName(pipelineNodeTemplateVo.getName());
                record.setCode(pipelineNodeTemplateVo.getCode());
                record.setType(pipelineNodeTemplateVo.getType());
                record.setDetailType(pipelineNodeTemplateVo.getDetailType());
                record.setParameters(pipelineNodeTemplateVo.getParameters());
                record.setClick(pipelineNodeTemplateVo.getClick());
                record.setRetry(pipelineNodeTemplateVo.getRetry());
                record.setButtons(pipelineNodeTemplateVo.getButtons());
                records.add(record);
            }
            saveBatch(records);
        }
    }

    @Override
    public void updatePipelineInstanceNodeByArgs(Long instanceId, String code, PipelineStatusEnum pipelineStatusEnum, String msg) {
        PipelineInstanceNodeTb pipelineInstanceNode = getPipelineInstanceNodeByArgs(instanceId, code);
        pipelineInstanceNode.setStartTime(new Date());
        pipelineInstanceNode.setCurrentNodeStatus(pipelineStatusEnum.getCode());
        pipelineInstanceNode.setCurrentNodeMsg(msg);
        updateById(pipelineInstanceNode);
    }

    public PipelineInstanceNodeTb getPipelineInstanceNodeByArgs(Long instanceId, String code) {
        return one(new LambdaQueryChainWrapper<>(getBaseMapper(), PipelineInstanceNodeTb.class)
                .eq(PipelineInstanceNodeTb::getInstanceId, instanceId)
                .eq(PipelineInstanceNodeTb::getCode, code)
        );
    }
}
