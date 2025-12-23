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
import cn.odboy.system.dal.model.SystemCreateDictDetailArgs;
import cn.odboy.system.dal.model.SystemQueryDictDetailArgs;
import cn.odboy.system.service.SystemDictDetailService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "系统：字典详情管理")
@RequestMapping("/api/dictDetail")
public class SystemDictDetailController {
    @Autowired
    private SystemDictDetailService systemDictDetailService;

    @Operation(summary = "查询字典详情")
    @PostMapping
    public ResponseEntity<KitPageResult<SystemDictDetailTb>> queryDictDetailByCrud(@Validated @RequestBody KitPageArgs<SystemQueryDictDetailArgs> args) {
        return queryDictDetailByArgs(args);
    }

    @Operation(summary = "查询字典详情")
    @PostMapping(value = "/queryDictDetailByArgs")
    public ResponseEntity<KitPageResult<SystemDictDetailTb>> queryDictDetailByArgs(@Validated @RequestBody KitPageArgs<SystemQueryDictDetailArgs> args) {
        SystemQueryDictDetailArgs criteria = args.getArgs();
        Page<SystemDictDetailTb> page = new Page<>(args.getPage(), args.getSize());
        return ResponseEntity.ok(systemDictDetailService.queryDictDetailByArgs(criteria, page));
    }

    @Operation(summary = "查询多个字典详情")
    @GetMapping(value = "/getDictDetailMaps")
    public ResponseEntity<Map<String, List<SystemDictDetailTb>>> getDictDetailMaps(@RequestParam String dictName) {
        String[] names = dictName.split("[,, ]");
        Map<String, List<SystemDictDetailTb>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, systemDictDetailService.queryDictDetailByName(name));
        }
        return ResponseEntity.ok(dictMap);
    }

    @Operation(summary = "新增字典详情")
    @PostMapping(value = "/saveDictDetail")
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Void> saveDictDetail(@Validated @RequestBody SystemCreateDictDetailArgs args) {
        systemDictDetailService.saveDictDetail(args);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "修改字典详情")
    @PostMapping(value = "/modifyDictDetailById")
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Void> modifyDictDetailById(@Validated(SystemDictDetailTb.Update.class) @RequestBody SystemDictDetailTb args) {
        systemDictDetailService.modifyDictDetailById(args);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "删除字典详情")
    @PostMapping(value = "/removeDictDetailById")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Void> removeDictDetailById(@RequestBody SystemDictDetailTb args) {
        systemDictDetailService.removeDictDetailById(args.getId());
        return ResponseEntity.ok(null);
    }
}
