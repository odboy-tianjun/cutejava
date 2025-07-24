package cn.odboy.devops.controller;

import cn.odboy.base.CsArgs;
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
@RequestMapping("/api/devops/pipelineTemplate")
@Api(tags = "DevOps：流水线模板管理")
public class DevopsPipelineTemplateController {
    @Autowired
    private PipelineTemplateService pipelineTemplateService;

    @ApiOperation("根据id查询模板内容")
    @PostMapping(value = "/getPipelineTemplate")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> getPipelineTemplate(@Validated @RequestBody CsArgs.FindByLongId args) {
        return ResponseEntity.ok(pipelineTemplateService.getPipelineTemplateById(args.getId()));
    }
}

