package cn.odboy.devops.service.core.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.devops.constant.pipeline.PipelineConst;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeDetailTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceNodeTb;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.dal.model.StartPipelineResultVo;
import cn.odboy.devops.dal.mysql.PipelineInstanceMapper;
import cn.odboy.devops.dal.redis.PipelineInstanceDAO;
import cn.odboy.devops.framework.pipeline.core.PipelineJobManage;
import cn.odboy.devops.framework.pipeline.model.PipelineInstanceNodeVo;
import cn.odboy.devops.framework.pipeline.model.PipelineInstanceVo;
import cn.odboy.devops.service.core.PipelineInstanceNodeDetailService;
import cn.odboy.devops.service.core.PipelineInstanceNodeService;
import cn.odboy.devops.service.core.PipelineInstanceService;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.websocket.model.WsSidVo;
import cn.odboy.framework.websocket.context.CsWsClientManager;
import cn.odboy.framework.websocket.context.CsWsMessage;
import cn.odboy.framework.websocket.context.CsWsServer;
import cn.odboy.framework.websocket.util.WsMessageUtil;
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
    public StartPipelineResultVo retryPipelineNode(PipelineInstanceVo args) {
        if (args.getInstanceId() == null) {
            throw new BadRequestException("流水线实例Id必填");
        }
        PipelineInstanceTb currentInstance = pipelineInstanceMapper.selectById(args.getInstanceId());
        if (currentInstance == null) {
            throw new BadRequestException("无效流水线，请刷新页面后再试");
        }
        if (StrUtil.isBlank(args.getCode())) {
            throw new BadRequestException("重试节点Code必填");
        }
        if (!PipelineStatusEnum.FAIL.getCode().equals(currentInstance.getCurrentNodeStatus())) {
            throw new BadRequestException("流水线节点无异常，无法重试");
        }
        pipelineInstanceDAO.unLock(currentInstance);
        log.info("解锁流水线, {}", JSON.toJSONString(currentInstance));
        PipelineInstanceTb record = pipelineJobManage.startJobByNodeCode(currentInstance, args.getCode());
        StartPipelineResultVo resultVo = new StartPipelineResultVo();
        resultVo.setInstanceId(record.getInstanceId());
        resultVo.setTemplateContent(record.getTemplateContent());
        return resultVo;
    }

    @Override
    public PipelineInstanceVo queryLastPipelineDetail(String instanceIdStr) {
        String realInstanceIdStr = instanceIdStr.replace(PipelineConst.INSTANCE_ID, "");
        Long instanceId = Long.valueOf(realInstanceIdStr);
        List<PipelineInstanceNodeVo> records = new ArrayList<>();
        PipelineInstanceTb pipelineInstanceTb = pipelineInstanceMapper.selectById(instanceId);
        if (pipelineInstanceTb == null) {
            throw new BadRequestException("流水线实例不存在");
        }
        PipelineInstanceVo pipelineInstanceVo = BeanUtil.copyProperties(pipelineInstanceTb, PipelineInstanceVo.class);
        String status = pipelineInstanceTb.getStatus();
        List<PipelineInstanceNodeTb> instanceNodeTbs = pipelineInstanceNodeService.queryPipelineInstanceNodeListByInstanceId(instanceId);
        for (PipelineInstanceNodeTb instanceNodeTb : instanceNodeTbs) {
            PipelineInstanceNodeVo dataVo = BeanUtil.copyProperties(instanceNodeTb, PipelineInstanceNodeVo.class);
            dataVo.setStatus(status);
            // 取流水线节点最后一步的信息
            PipelineInstanceNodeDetailTb pipelineInstanceNodeDetail = pipelineInstanceNodeDetailService.getLastPipelineInstanceNodeDetailByArgs(instanceNodeTb);
            dataVo.setCurrentNodeMsg(pipelineInstanceNodeDetail.getStepMsg());
            // 取执行历史
            dataVo.setHistorys(pipelineInstanceNodeDetailService.queryPipelineInstanceNodeDetailByArgs(instanceNodeTb));
            records.add(dataVo);
        }
        pipelineInstanceVo.setNodes(records);
        return pipelineInstanceVo;
    }

    @Override
    public void queryLastPipelineDetailWs(String sid) {
        WsSidVo sidVo = WsMessageUtil.parseSid(sid);
        CsWsServer wsServer = CsWsClientManager.getClientBySid(sid);
        wsServer.restartTask(() -> {
            boolean loop = true;
            while (loop) {
                ThreadUtil.safeSleep(1000);
                try {
                    PipelineInstanceVo pipelineInstanceVo = queryLastPipelineDetail(sidVo.getParam());
                    CsWsMessage message = new CsWsMessage(sidVo.getBizCode(), JSON.toJSONString(pipelineInstanceVo));
                    wsServer.sendMessage(message, sid);
                } catch (Exception e) {
                    log.error("推送流水线最新数据失败", e);
                    loop = false;
                }
            }
        });
    }
}
