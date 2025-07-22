package cn.odboy.devops.controller;

import cn.odboy.annotation.AnonymousGetMapping;
import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.dal.dataobject.PipelineTemplateTb;
import cn.odboy.devops.dal.model.DevOpsQueryLastPipelineDetailArgs;
import cn.odboy.devops.service.PipelineInstanceService;
import cn.odboy.devops.service.PipelineTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devops/pipeline")
@Api(tags = "DevOps：流水线管理")
public class DevopsPipelineController {
    @Autowired
    private PipelineTemplateService pipelineTemplateService;
    @Autowired
    private PipelineInstanceService pipelineInstanceService;

    @ApiOperation("启动流水线")
    @PostMapping(value = "/start")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> startPipeline() {
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
        return ResponseEntity.ok(pipelineInstanceService.startPipeline(pipelineInstanceTb));
    }

    @ApiOperation("重启流水线")
    @PostMapping(value = "/restart")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> restartPipeline() {
        String appName = "cuteops";
        String envCode = "daily";
        PipelineTemplateTb pipelineTemplateTb = pipelineTemplateService.getPipelineTemplateById(4L);
        PipelineInstanceTb pipelineInstanceTb = new PipelineInstanceTb();
        pipelineInstanceTb.setTemplateId(pipelineTemplateTb.getId());
        pipelineInstanceTb.setInstanceName("流水线测试");
        pipelineInstanceTb.setTemplateType(pipelineTemplateTb.getType());
        pipelineInstanceTb.setContextName(appName);
        pipelineInstanceTb.setEnv(envCode);
        pipelineInstanceTb.setTemplateContent(pipelineTemplateTb.getTemplate());
        pipelineInstanceTb.setInstanceId(1947604330727079936L);
        String retryNodeCode = "node_build_java";
        pipelineInstanceService.restartPipeline(pipelineInstanceTb, retryNodeCode);
        return ResponseEntity.ok("流水线重启成功");
    }

    @ApiOperation("查询流水线明细")
    @PostMapping(value = "/last")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> queryLastPipelineDetail(@Validated @RequestBody DevOpsQueryLastPipelineDetailArgs args) {
        return ResponseEntity.ok(pipelineInstanceService.queryLastPipelineDetail(args.getInstanceId()));
    }
}
