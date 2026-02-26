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

import cn.odboy.system.application.model.CuteUserSelectVo;
import cn.odboy.system.application.service.CuteUserSelectService;
import cn.odboy.system.dal.model.request.SystemQueryUserArgs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Api(tags = "系统组件：CuteUserSelect")
@RequestMapping("/api/component/CuteUserSelect")
public class CuteUserSelectApi {

  @Autowired
  private CuteUserSelectService cuteUserSelectService;

  @ApiOperation("查询用户基础数据")
  @PostMapping(value = "/listMetadata")
  @PreAuthorize("@el.check()")
  public ResponseEntity<List<CuteUserSelectVo>> listMetadata(@Validated @RequestBody SystemQueryUserArgs args) {
    List<CuteUserSelectVo> collect = cuteUserSelectService.listMetadata(args);
    return ResponseEntity.ok(collect);
  }

  @ApiOperation("根据用户名称集合查询用户基础数据")
  @PostMapping(value = "/listMetadataByUsernames")
  @PreAuthorize("@el.check()")
  public ResponseEntity<List<CuteUserSelectVo>> listMetadataByUsernames(@RequestBody List<String> usernameList) {
    List<CuteUserSelectVo> collect = cuteUserSelectService.listMetadataByUsernames(usernameList);
    return ResponseEntity.ok(collect);
  }
}
