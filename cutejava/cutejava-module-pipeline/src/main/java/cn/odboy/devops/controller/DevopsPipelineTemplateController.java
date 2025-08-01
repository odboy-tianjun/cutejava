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

import cn.odboy.base.CsArgs;
import cn.odboy.devops.service.core.PipelineTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

