/*
 * Copyright 2021-2026 Odboy
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
package cn.odboy.system.application;

import cn.odboy.system.application.service.CuteProductLineSelectProService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统组件：CuteProductLineSelectPro")
@RequestMapping("/api/component/CuteProductLineSelectPro")
public class CuteProductLineSelectProApi {

  @Autowired
  private CuteProductLineSelectProService cuteProductLineSelectProService;

  @ApiOperation("查询部门下拉选择Pro数据源")
  @PostMapping(value = "/listMetadata")
  @PreAuthorize("@el.check()")
  public ResponseEntity<?> listMetadata() {
    return ResponseEntity.ok(cuteProductLineSelectProService.listMetadata());
  }
}
