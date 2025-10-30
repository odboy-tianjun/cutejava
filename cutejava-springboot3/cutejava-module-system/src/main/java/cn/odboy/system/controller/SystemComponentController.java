/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.odboy.system.controller;

import cn.odboy.system.service.SystemDeptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "系统：组件数据源")
@RequestMapping("/api/component")
public class SystemComponentController {
    @Autowired
    private SystemDeptService systemDeptService;

    @Operation(summary = "查询部门下拉选择数据源")
    @PostMapping(value = "/queryDeptSelectDataSource")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> queryDeptSelectDataSource() {
        return ResponseEntity.ok(systemDeptService.queryDeptSelectDataSource());
    }

    @Operation(summary = "查询部门下拉选择Pro数据源")
    @PostMapping(value = "/queryDeptSelectProDataSource")
    @PreAuthorize("@el.check()")
    public ResponseEntity<?> queryDeptSelectProDataSource() {
        return ResponseEntity.ok(systemDeptService.queryDeptSelectProDataSource());
    }
}
