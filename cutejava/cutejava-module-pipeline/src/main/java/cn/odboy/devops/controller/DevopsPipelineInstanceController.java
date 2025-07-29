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
package cn.odboy.devops.controller;

import cn.odboy.devops.dal.dataobject.PipelineInstanceTb;
import cn.odboy.devops.dal.dataobject.PipelineTemplateTb;
import cn.odboy.devops.dal.model.QueryLastPipelineDetailArgs;
import cn.odboy.devops.dal.model.RestartPipelineArgs;
import cn.odboy.devops.dal.model.StartPipelineArgs;
import cn.odboy.devops.framework.pipeline.model.PipelineInstanceVo;
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
    public ResponseEntity<?> startPipeline(@Validated @RequestBody StartPipelineArgs args) {
        // 获取模板
        PipelineTemplateTb pipelineTemplateTb = pipelineTemplateService.getPipelineTemplateById(args.getTemplateId());
        // 创建流水线实例
        PipelineInstanceTb pipelineInstanceTb = new PipelineInstanceTb();
        pipelineInstanceTb.setTemplateId(pipelineTemplateTb.getId());
        pipelineInstanceTb.setTemplateType(pipelineTemplateTb.getType());
        pipelineInstanceTb.setTemplateContent(pipelineTemplateTb.getTemplate());
        pipelineInstanceTb.setInstanceName(args.getInstanceName());
        pipelineInstanceTb.setContextName(args.getContextName());
        pipelineInstanceTb.setEnv(args.getEnv());
        return ResponseEntity.ok(pipelineInstanceService.startPipeline(pipelineInstanceTb));
    }

    /**
     * 已验证流程
     */
    @ApiOperation("重启流水线")
    @PostMapping(value = "/restart")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> restartPipeline(@RequestBody RestartPipelineArgs args) {
        return ResponseEntity.ok(pipelineInstanceService.restartPipeline(args));
    }

    /**
     * 已验证流程
     */
    @ApiOperation("停止流水线")
    @PostMapping(value = "/stop")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> stopPipeline(@RequestBody RestartPipelineArgs args) {
        pipelineInstanceService.stopPipeline(args);
        return ResponseEntity.ok("流水线停止成功");
    }

    /**
     * 已验证流程
     */
    @ApiOperation("流水线节点重试")
    @PostMapping(value = "/retry")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> retryPipelineNode(@RequestBody PipelineInstanceVo args) {
        return ResponseEntity.ok(pipelineInstanceService.retryPipelineNode(args));
    }

    @ApiOperation("查询流水线明细")
    @PostMapping(value = "/last")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> queryLastPipelineDetail(@Validated @RequestBody QueryLastPipelineDetailArgs args) {
        return ResponseEntity.ok(pipelineInstanceService.queryLastPipelineDetail(args.getInstanceId()));
    }

    @ApiOperation("查询流水线明细Ws")
    @PostMapping(value = "/lastWs")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> queryLastPipelineDetailWs(@Validated @RequestBody QueryLastPipelineDetailArgs args) {
        pipelineInstanceService.queryLastPipelineDetailWs(args.getInstanceId());
        return ResponseEntity.ok("开始推送流水线明细");
    }
}
