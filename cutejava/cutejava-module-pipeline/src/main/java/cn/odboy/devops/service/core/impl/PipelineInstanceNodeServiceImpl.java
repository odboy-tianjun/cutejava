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
package cn.odboy.devops.service.core.impl;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.devops.framework.pipeline.constant.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeTemplateVo;
import cn.odboy.devops.service.core.PipelineInstanceNodeDetailService;
import cn.odboy.devops.service.core.PipelineInstanceNodeService;
import com.alibaba.fastjson2.JSON;
import com.anwen.mongo.conditions.query.LambdaQueryChainWrapper;
import com.anwen.mongo.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineInstanceNodeServiceImpl extends ServiceImpl<PipelineInstanceNodeTb> implements PipelineInstanceNodeService {
    private final PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService;

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
                record.setCurrentNodeStatus(PipelineStatusEnum.PENDING.getCode());
                record.setCurrentNodeMsg(PipelineStatusEnum.PENDING.getDesc());
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

    @Override
    public PipelineInstanceNodeTb getPipelineInstanceNodeByArgs(Long instanceId, String code) {
        return one(new LambdaQueryChainWrapper<>(getBaseMapper(), PipelineInstanceNodeTb.class).eq(PipelineInstanceNodeTb::getInstanceId, instanceId)
            .eq(PipelineInstanceNodeTb::getCode, code));
    }

    @Override
    public void remakePipelineInstanceDetail(PipelineInstanceTb pipelineInstanceTb, String retryNodeCode) {
        List<String> penddingNodeList = new ArrayList<>();
        List<PipelineNodeTemplateVo> pipelineNodeTemplateVos = JSON.parseArray(pipelineInstanceTb.getTemplateContent(), PipelineNodeTemplateVo.class);
        boolean findFlag = false;
        for (PipelineNodeTemplateVo pipelineNodeTemplateVo : pipelineNodeTemplateVos) {
            if (pipelineNodeTemplateVo.getCode().equals(retryNodeCode) || findFlag) {
                findFlag = true;
                penddingNodeList.add(pipelineNodeTemplateVo.getCode());
            }
        }
        // 更新节点状态
        List<PipelineInstanceNodeTb> instanceNodeList = list(
            new LambdaQueryChainWrapper<>(getBaseMapper(), PipelineInstanceNodeTb.class).eq(PipelineInstanceNodeTb::getInstanceId,
                pipelineInstanceTb.getInstanceId()).eq(PipelineInstanceNodeTb::getCode, penddingNodeList));
        if (CollUtil.isNotEmpty(instanceNodeList)) {
            Date nowTime = new Date();
            for (PipelineInstanceNodeTb pipelineInstanceNodeTb : instanceNodeList) {
                pipelineInstanceNodeTb.setUpdateTime(nowTime);
                pipelineInstanceNodeTb.setStartTime(null);
                pipelineInstanceNodeTb.setCurrentNodeMsg(PipelineStatusEnum.PENDING.getDesc());
                pipelineInstanceNodeTb.setCurrentNodeStatus(PipelineStatusEnum.PENDING.getCode());
            }
            updateBatchByIds(instanceNodeList);
            // 删除节点明细数据
            List<Long> nodeIds = instanceNodeList.stream().map(PipelineInstanceNodeTb::getId).distinct().collect(Collectors.toList());
            pipelineInstanceNodeDetailService.removeByNodeIds(nodeIds);
        }
    }

    @Override
    public List<PipelineInstanceNodeTb> queryPipelineInstanceNodeListByInstanceId(Long instanceId) {
        return list(new LambdaQueryChainWrapper<>(getBaseMapper(), PipelineInstanceNodeTb.class).eq(PipelineInstanceNodeTb::getInstanceId, instanceId)
            .orderByAsc(PipelineInstanceNodeTb::getId));
    }

    @Override
    public void finishPipelineInstanceNodeByArgs(Long instanceId, String nodeCode, PipelineStatusEnum pipelineStatusEnum, String desc, Date date) {
        PipelineInstanceNodeTb pipelineInstanceNode = getPipelineInstanceNodeByArgs(instanceId, nodeCode);
        pipelineInstanceNode.setCurrentNodeStatus(pipelineStatusEnum.getCode());
        pipelineInstanceNode.setCurrentNodeMsg(desc);
        pipelineInstanceNode.setFinishTime(date);
        updateById(pipelineInstanceNode);
    }
}
