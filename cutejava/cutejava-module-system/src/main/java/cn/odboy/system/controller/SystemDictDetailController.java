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

import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.model.request.SystemCreateDictDetailArgs;
import cn.odboy.system.dal.model.request.SystemQueryDictDetailArgs;
import cn.odboy.system.dal.model.response.SystemDictDetailVo;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemDictDetailService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统：字典详情管理")
@RequestMapping("/api/dictDetail")
public class SystemDictDetailController {

  @Autowired
  private SystemDictDetailService systemDictDetailService;

  @ApiOperation("查询字典详情")
  @PostMapping(value = "/searchDictDetail")
  public ResponseEntity<KitPageResult<SystemDictDetailVo>> queryDictDetailByArgs(@Validated @RequestBody KitPageArgs<SystemQueryDictDetailArgs> pageArgs) {
    Page<SystemDictDetailTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemDictDetailService.searchDictDetail(pageArgs.getArgs(), page));
  }

  @ApiOperation("查询多个字典详情")
  @GetMapping(value = "/getDictDetailMaps")
  public ResponseEntity<Map<String, List<SystemDictDetailVo>>> getDictDetailMaps(@RequestParam String dictName) {
    Map<String, List<SystemDictDetailVo>> dictMap = systemDictDetailService.getDictDetailMap(dictName);
    return ResponseEntity.ok(dictMap);
  }

  @OperationLog
  @ApiOperation("新增字典详情")
  @PostMapping(value = "/saveDictDetail")
  @PreAuthorize("@el.check('dict:add')")
  public ResponseEntity<Void> saveDictDetail(@Validated @RequestBody SystemCreateDictDetailArgs args) {
    systemDictDetailService.saveDictDetail(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改字典详情")
  @PostMapping(value = "/updateDictDetailById")
  @PreAuthorize("@el.check('dict:edit')")
  public ResponseEntity<Void> updateDictDetailById(@Validated(SystemDictDetailTb.Update.class) @RequestBody SystemDictDetailTb args) {
    systemDictDetailService.updateDictDetailById(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("删除字典详情")
  @PostMapping(value = "/deleteDictDetailById")
  @PreAuthorize("@el.check('dict:del')")
  public ResponseEntity<Void> deleteDictDetailById(@RequestBody SystemDictDetailTb args) {
    systemDictDetailService.deleteDictDetailById(args.getId());
    return ResponseEntity.ok(null);
  }
}
