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
package cn.odboy.system.controller;

import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import cn.odboy.system.dal.model.request.SystemCreateDictArgs;
import cn.odboy.system.dal.model.request.SystemQueryDictArgs;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemDictService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统：字典管理")
@RequestMapping("/api/dict")
public class SystemDictController {

  @Autowired
  private SystemDictService systemDictService;

  @OperationLog
  @ApiOperation("导出字典数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('dict:list')")
  public void exportDict(HttpServletResponse response, SystemQueryDictArgs args) {
    systemDictService.exportDictXlsx(response, args);
  }

  @Deprecated
  @ApiOperation("查询所有字典")
  @PostMapping(value = "/queryAllDict")
  @PreAuthorize("@el.check('dict:list')")
  public ResponseEntity<List<SystemDictTb>> queryAllDict() {
    return ResponseEntity.ok(systemDictService.queryDictByArgs(new SystemQueryDictArgs()));
  }

  @ApiOperation("分页查询字典")
  @PostMapping(value = "/searchDict")
  @PreAuthorize("@el.check('dict:list')")
  public ResponseEntity<KitPageResult<SystemDictTb>> queryDictByArgs(@Validated @RequestBody KitPageArgs<SystemQueryDictArgs> pageArgs) {
    Page<SystemDictTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemDictService.searchDict(pageArgs.getArgs(), page, pageArgs.getOrderBy()));
  }

  @OperationLog
  @ApiOperation("新增字典")
  @PostMapping(value = "/saveDict")
  @PreAuthorize("@el.check('dict:add')")
  public ResponseEntity<Void> saveDict(@Validated @RequestBody SystemCreateDictArgs args) {
    systemDictService.saveDict(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改字典")
  @PostMapping(value = "/updateDictById")
  @PreAuthorize("@el.check('dict:edit')")
  public ResponseEntity<Void> updateDictById(@Validated(SystemDictTb.Update.class) @RequestBody SystemDictTb args) {
    systemDictService.updateDictById(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("删除字典")
  @PostMapping(value = "/deleteDictByIds")
  @PreAuthorize("@el.check('dict:del')")
  public ResponseEntity<Void> deleteDictByIds(@RequestBody Set<Long> ids) {
    systemDictService.deleteDictByIds(ids);
    return ResponseEntity.ok(null);
  }
}
