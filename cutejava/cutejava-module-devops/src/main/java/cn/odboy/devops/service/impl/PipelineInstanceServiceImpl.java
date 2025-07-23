package cn.odboy.devops.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeDetailTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.dal.model.StartPipelineResultVo;
import cn.odboy.devops.dal.mysql.PipelineInstanceMapper;
import cn.odboy.devops.dal.redis.PipelineInstanceDAO;
import cn.odboy.devops.framework.pipeline.core.PipelineJobManage;
import cn.odboy.devops.framework.pipeline.model.PipelineInstanceVo;
import cn.odboy.devops.framework.pipeline.model.PipelineNodeDataVo;
import cn.odboy.devops.service.PipelineInstanceNodeDetailService;
import cn.odboy.devops.service.PipelineInstanceNodeService;
import cn.odboy.devops.service.PipelineInstanceService;
import cn.odboy.framework.exception.BadRequestException;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineInstanceServiceImpl implements PipelineInstanceService {
    private final PipelineJobManage pipelineJobManage;
    private final PipelineInstanceMapper pipelineInstanceMapper;
    private final PipelineInstanceDAO pipelineInstanceDAO;
    private final PipelineInstanceNodeService pipelineInstanceNodeService;
    private final PipelineInstanceNodeDetailService pipelineInstanceNodeDetailService;

    @Override
    public StartPipelineResultVo startPipeline(PipelineInstanceTb pipelineInstanceTb) {
        if (pipelineInstanceDAO.lock(pipelineInstanceTb)) {
            throw new BadRequestException("流水线运行中，无法重复执行");
        }
        PipelineInstanceTb record = pipelineJobManage.startJob(pipelineInstanceTb);
        StartPipelineResultVo resultVo = new StartPipelineResultVo();
        resultVo.setInstanceId(record.getInstanceId());
        resultVo.setTemplateContent(record.getTemplateContent());
        return resultVo;
    }

    @Override
    public StartPipelineResultVo restartPipeline(PipelineInstanceTb pipelineInstanceTb, String retryNodeCode) {
        if (pipelineInstanceTb.getInstanceId() == null) {
            throw new BadRequestException("流水线实例Id必填");
        }
        PipelineInstanceTb currentInstance = pipelineInstanceMapper.selectById(pipelineInstanceTb.getInstanceId());
        if (currentInstance == null) {
            throw new BadRequestException("无效流水线，请刷新页面后再试");
        }
        if (pipelineInstanceDAO.lock(currentInstance)) {
            throw new BadRequestException("流水线运行中，无法重复执行");
        }
        // pending、running、success、fail
        List<String> canRestartStatus = new ArrayList<>() {{
            add(PipelineStatusEnum.SUCCESS.getCode());
            add(PipelineStatusEnum.FAIL.getCode());
        }};
        if (!canRestartStatus.contains(currentInstance.getStatus())) {
            throw new BadRequestException("流水线运行中，无法重复执行");
        }
        log.info("解锁流水线, {}", JSON.toJSONString(currentInstance));
        pipelineInstanceDAO.unLock(currentInstance);
        PipelineInstanceTb record = pipelineJobManage.startJobByNodeCode(currentInstance, retryNodeCode);
        StartPipelineResultVo resultVo = new StartPipelineResultVo();
        resultVo.setInstanceId(record.getInstanceId());
        resultVo.setTemplateContent(record.getTemplateContent());
        return resultVo;
    }

    @Override
    public PipelineInstanceVo queryLastPipelineDetail(String instanceIdStr) {
        String realInstanceIdStr = instanceIdStr.replace(PipelineConst.INSTANCE_ID, "");
        Long instanceId = Long.valueOf(realInstanceIdStr);
        List<PipelineNodeDataVo> records = new ArrayList<>();
        PipelineInstanceTb pipelineInstanceTb = pipelineInstanceMapper.selectById(instanceId);
        if (pipelineInstanceTb == null) {
            throw new BadRequestException("流水线实例不存在");
        }
        String status = pipelineInstanceTb.getStatus();
        List<PipelineInstanceNodeTb> instanceNodeTbs = pipelineInstanceNodeService.queryPipelineInstanceNodeListByInstanceId(instanceId);
        for (PipelineInstanceNodeTb instanceNodeTb : instanceNodeTbs) {
            PipelineNodeDataVo dataVo = BeanUtil.copyProperties(instanceNodeTb, PipelineNodeDataVo.class);
            dataVo.setStatus(status);
            // 取流水线节点最后一步的信息
            PipelineInstanceNodeDetailTb pipelineInstanceNodeDetail = pipelineInstanceNodeDetailService.getLastPipelineInstanceNodeDetailByArgs(instanceNodeTb, instanceNodeTb.getId(), instanceNodeTb.getCode());
            dataVo.setCurrentNodeMsg(pipelineInstanceNodeDetail.getStepMsg());
            dataVo.setCurrentNodeStatus(pipelineInstanceNodeDetail.getStepStatus());
            records.add(dataVo);
        }
        PipelineInstanceVo pipelineInstanceVo = BeanUtil.copyProperties(pipelineInstanceTb, PipelineInstanceVo.class);
        pipelineInstanceVo.setNodes(records);
        return pipelineInstanceVo;
    }
}
