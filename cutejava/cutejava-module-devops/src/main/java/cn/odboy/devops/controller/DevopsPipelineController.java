package cn.odboy.devops.controller;

import cn.odboy.annotation.AnonymousGetMapping;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.dal.dataobject.PipelineTemplateTb;
import cn.odboy.devops.service.PipelineInstanceService;
import cn.odboy.devops.service.PipelineTemplateService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devops/pipeline")
@Api(tags = "系统：验证码管理")
public class DevopsPipelineController {
    @Autowired
    private PipelineTemplateService pipelineTemplateService;
    @Autowired
    private PipelineInstanceService pipelineInstanceService;

    @AnonymousGetMapping(value = "/start")
    public ResponseEntity<?> start() {
        // 输入参数
        String appName = "cuteops";
        String envCode = "daily";
        // 获取模板
        PipelineTemplateTb pipelineTemplateTb = pipelineTemplateService.getPipelineTemplateById(4L);
        // 创建流水线实例
        PipelineInstanceTb pipelineInstanceTb = new PipelineInstanceTb();
        pipelineInstanceTb.setTemplateId(pipelineTemplateTb.getId());
        pipelineInstanceTb.setInstanceName("流水线测试");
        pipelineInstanceTb.setTemplateType(pipelineTemplateTb.getType());
        pipelineInstanceTb.setContextName(appName);
        pipelineInstanceTb.setEnv(envCode);
        pipelineInstanceTb.setTemplateContent(pipelineTemplateTb.getTemplate());
        pipelineInstanceService.start(pipelineInstanceTb);
        return ResponseEntity.ok("流水线启动成功");
    }
}
