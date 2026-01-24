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

import cn.odboy.system.application.model.CuteDeptTreeVo;
import cn.odboy.system.application.service.CuteDeptTreeService;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.model.request.SystemQueryDeptArgs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Api(tags = "系统组件：CuteDeptTree")
@RequestMapping("/api/component/CuteDeptTree")
public class CuteDeptTreeApi {

  @Autowired
  private CuteDeptTreeService cuteDeptTreeService;

  @ApiOperation("查询部门")
  @PostMapping(value = "/listMetadata")
  public ResponseEntity<List<CuteDeptTreeVo>> listMetadata(@Validated @RequestBody SystemQueryDeptArgs args) throws Exception {
    List<CuteDeptTreeVo> depts = cuteDeptTreeService.listMetadata(args);
    return ResponseEntity.ok(depts);
  }
}
