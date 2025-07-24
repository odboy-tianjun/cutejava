package cn.odboy.devops.controller;

import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.dal.dataobject.PipelineTemplateTb;
import cn.odboy.devops.dal.model.DevOpsQueryLastPipelineDetailArgs;
import cn.odboy.devops.service.core.PipelineInstanceService;
import cn.odboy.devops.service.core.PipelineTemplateService;
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
@RequestMapping("/api/devops/pipelineInstance")
@Api(tags = "DevOps：流水线实例管理")
public class DevopsPipelineInstanceController {
    @Autowired
    private PipelineTemplateService pipelineTemplateService;
    @Autowired
    private PipelineInstanceService pipelineInstanceService;

    /**
     * 已验证流程
     */
    @ApiOperation("启动流水线")
    @PostMapping(value = "/start")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> startPipeline(@RequestBody PipelineTemplateTb args) {
        // 输入参数
        String appName = "cuteops";
        String envCode = "daily";
        // 获取模板
        PipelineTemplateTb pipelineTemplateTb = pipelineTemplateService.getPipelineTemplateById(args.getId());
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

    /**
     * TODO 待实现
     */
    @ApiOperation("重启流水线")
    @PostMapping(value = "/restart")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> restartPipeline(@RequestBody PipelineTemplateTb args) {
        return ResponseEntity.ok("功能开发中");
    }

    /**
     * 已验证流程
     */
    @ApiOperation("流水线节点重试")
    @PostMapping(value = "/retry")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> retryPipelineNode(@RequestBody PipelineTemplateTb args) {
        String appName = "cuteops";
        String envCode = "daily";
        PipelineTemplateTb pipelineTemplateTb = pipelineTemplateService.getPipelineTemplateById(args.getId());
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
        return ResponseEntity.ok(pipelineInstanceService.restartPipeline(pipelineInstanceTb, retryNodeCode));
    }

    @ApiOperation("查询流水线明细")
    @PostMapping(value = "/last")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> queryLastPipelineDetail(@Validated @RequestBody DevOpsQueryLastPipelineDetailArgs args) {
        return ResponseEntity.ok(pipelineInstanceService.queryLastPipelineDetail(args.getInstanceId()));
    }

    @ApiOperation("查询流水线明细Ws")
    @PostMapping(value = "/lastWs")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> queryLastPipelineDetailWs(@Validated @RequestBody DevOpsQueryLastPipelineDetailArgs args) {
        pipelineInstanceService.queryLastPipelineDetailWs(args.getInstanceId());
        return ResponseEntity.ok("开始推送流水线明细");
    }
}
