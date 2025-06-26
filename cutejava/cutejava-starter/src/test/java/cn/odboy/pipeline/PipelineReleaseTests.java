package cn.odboy.pipeline;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.odboy.devops.constant.pipeline.PipelineStatusEnum;
import cn.odboy.devops.dal.dataobject.pipeline.PipelineInstanceTb;
import cn.odboy.devops.dal.dataobject.pipeline.PipelineTemplateTb;
import cn.odboy.devops.framework.pipeline.PipelineJobManage;
import cn.odboy.devops.service.pipeline.PipelineInstanceService;
import cn.odboy.devops.service.pipeline.PipelineTemplateService;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 流水线正式测试
 *
 * @author odboy
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PipelineReleaseTests {
    @Autowired
    private PipelineJobManage pipelineJobManage;
    @Autowired
    private PipelineTemplateService pipelineTemplateService;
    @Autowired
    private PipelineInstanceService pipelineInstanceService;

    /**
     * 默认模板由人工维护，百分百不会空<br/>
     * 介意又不想配置的自行面壁
     */
    @Test
    public void contextLoads() {
        // 输入参数
        Date nowTime = new Date();
        String createBy = "admin";
        String appName = "test";
        String envCode = "daily";
        Map<String, Object> context = new HashMap<>();
        context.put("createBy", createBy);
        context.put("appName", appName);
        context.put("envCode", envCode);
        // 获取模板
        PipelineTemplateTb pipelineTemplateTb = pipelineTemplateService.getPipelineTemplateById(1L);
        // 创建流水线实例
        PipelineInstanceTb pipelineInstanceTb = new PipelineInstanceTb();
        pipelineInstanceTb.setPipelineTemplateId(pipelineTemplateTb.getId());
        pipelineInstanceTb.setPipelineInstanceName("流水线测试");
        pipelineInstanceTb.setPipelineInstanceId(IdUtil.nanoId());
        pipelineInstanceTb.setCreateTime(nowTime);
        pipelineInstanceTb.setCreateBy(createBy);
        pipelineInstanceTb.setUpdateTime(nowTime);
        pipelineInstanceTb.setType(pipelineTemplateTb.getType());
        pipelineInstanceTb.setAppName(appName);
        pipelineInstanceTb.setEnv(envCode);
        pipelineInstanceTb.setStatus(PipelineStatusEnum.WAIT.getCode());
        pipelineInstanceTb.setCurrentNode(null);
        pipelineInstanceTb.setCurrentNodeStatus(null);
        pipelineInstanceTb.setContext(JSON.toJSONString(context));
        pipelineInstanceTb.setPipelineTemplate(pipelineTemplateTb.getTemplate());
        pipelineInstanceService.createPipelineInstance(pipelineInstanceTb);
        pipelineJobManage.startJob(pipelineInstanceTb);
        ThreadUtil.safeSleep(1000 * 60 * 60 * 24);
    }
}

