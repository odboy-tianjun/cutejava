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

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResult;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import cn.odboy.system.dal.model.SystemCreateDictArgs;
import cn.odboy.system.dal.model.SystemQueryDictArgs;
import cn.odboy.system.service.SystemDictService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@Tag(name = "系统：字典管理")
@RequestMapping("/api/dict")
public class SystemDictController {
    @Autowired
    private SystemDictService systemDictService;

    @Operation(summary = "导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void exportDict(HttpServletResponse response, SystemQueryDictArgs criteria) throws IOException {
        systemDictService.exportDictExcel(systemDictService.queryDictByArgs(criteria), response);
    }

    @Operation(summary = "查询字典")
    @PostMapping(value = "/queryAllDict")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<List<SystemDictTb>> queryAllDict() {
        return ResponseEntity.ok(systemDictService.queryDictByArgs(new SystemQueryDictArgs()));
    }

    @Operation(summary = "查询字典")
    @PostMapping
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<CsPageResult<SystemDictTb>> queryDictByArgs(@Validated @RequestBody CsPageArgs<SystemQueryDictArgs> args) {
        Page<SystemDictTb> page;
        SystemQueryDictArgs criteria = args.getArgs();
        if (args.getSize() != null) {
            page = new Page<>(args.getPage(), args.getSize());
        } else {
            page = new Page<>(criteria.getPage(), criteria.getSize());
        }
        return ResponseEntity.ok(systemDictService.queryDictByArgs(criteria, page));
    }

    @Operation(summary = "新增字典")
    @PostMapping(value = "/saveDict")
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Void> saveDict(@Validated @RequestBody SystemCreateDictArgs args) {
        systemDictService.saveDict(args);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "修改字典")
    @PostMapping(value = "/modifyDictById")
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Void> modifyDictById(@Validated(SystemDictTb.Update.class) @RequestBody SystemDictTb args) {
        systemDictService.modifyDictById(args);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "删除字典")
    @PostMapping(value = "/removeDictByIds")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Void> removeDictByIds(@RequestBody Set<Long> ids) {
        systemDictService.removeDictByIds(ids);
        return ResponseEntity.ok(null);
    }
}
