package cn.odboy.context;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.odboy.exception.BadRequestException;
import cn.odboy.handler.SerialPipelineNodeHandler;
import cn.odboy.model.pipeline.model.PipelineNodeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * 流水线管理工具
 *
 * @author odboy
 */
@Slf4j
@Component
public class PipelineManage {
    private static final PipelineTaskPool pipelineTaskPool = new PipelineTaskPool("DefaultPipelineTaskPool");

    public void execute() {
        String pipelineId = IdUtil.objectId();
        pipelineTaskPool.submitTask(pipelineId, () -> {
            List<PipelineNodeVo> pipelineNodeVos = initialize();
            for (PipelineNodeVo pipelineNodeVo : pipelineNodeVos) {
                executePipeline(pipelineId, pipelineNodeVo);
                while ("running".equals(pipelineNodeVo.getExecuteStatus())) {
                    log.info("流水线执行中...");
                    ThreadUtil.safeSleep(2000);
                }
            }
        });
        ThreadUtil.safeSleep(1000000);
    }

    private void executePipeline(String pipelineId, PipelineNodeVo pipelineNodeVo) {
        String pipelineServiceName = String.format("pipeline:%s", pipelineNodeVo.getBizCode());
        SerialPipelineNodeHandler<Object, Object> pipelineNodeHandler = SpringBeanHolder.getBean(pipelineServiceName);
        try {
            Object o = pipelineNodeHandler.doProcess(pipelineId, null);
            int i = 1 / 0;
            log.info("运行结果, {}", o);
        } catch (BadRequestException e) {
            log.error("运行流水线异常", e);
            if (pipelineTaskPool.isTaskRunning(pipelineId)) {
                pipelineTaskPool.stopTaskForcibly(pipelineId);
            }
        } catch (Exception e) {
            log.error("其他异常", e);
            if (pipelineTaskPool.isTaskRunning(pipelineId)) {
                pipelineTaskPool.stopTaskForcibly(pipelineId);
            }
        }
    }

    public List<PipelineNodeVo> initialize() {
        List<PipelineNodeVo> pipelineNodeVos = new ArrayList<>();
        PipelineNodeVo createReleaseBranchNodeVo = new PipelineNodeVo();
        createReleaseBranchNodeVo.setName("初始化");
        createReleaseBranchNodeVo.setClick(true);
        createReleaseBranchNodeVo.setButtonList(null);
        createReleaseBranchNodeVo.setBizCode("create_release_branch");
        createReleaseBranchNodeVo.setStartTimeMillis(DateTime.now().getTime());
        createReleaseBranchNodeVo.setDurationMillis(0L);
        createReleaseBranchNodeVo.setExecuteStatus("running");
        createReleaseBranchNodeVo.setResultDesc(null);
        pipelineNodeVos.add(createReleaseBranchNodeVo);

        PipelineNodeVo createReleaseBranchNodeVo1 = new PipelineNodeVo();
        createReleaseBranchNodeVo1.setName("初始化1");
        createReleaseBranchNodeVo1.setClick(true);
        createReleaseBranchNodeVo1.setButtonList(null);
        createReleaseBranchNodeVo1.setBizCode("create_release_branch");
        createReleaseBranchNodeVo1.setStartTimeMillis(DateTime.now().getTime());
        createReleaseBranchNodeVo1.setDurationMillis(0L);
        createReleaseBranchNodeVo1.setExecuteStatus("running");
        createReleaseBranchNodeVo1.setResultDesc(null);
        pipelineNodeVos.add(createReleaseBranchNodeVo1);

        return pipelineNodeVos;
    }
}