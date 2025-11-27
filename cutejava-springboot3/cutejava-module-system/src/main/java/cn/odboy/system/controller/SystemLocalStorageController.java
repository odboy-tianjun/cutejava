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
import cn.odboy.constant.FileTypeEnum;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.service.SystemLocalStorageService;
import cn.odboy.util.KitFileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Tag(name = "工具：本地存储管理")
@RequestMapping("/api/localStorage")
public class SystemLocalStorageController {
    @Autowired
    private SystemLocalStorageService localStorageService;

    @Operation(summary = "查询文件")
    @PostMapping
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<KitPageResult<SystemLocalStorageTb>> queryLocalStorage(@Validated @RequestBody KitPageArgs<SystemQueryStorageArgs> args) {
        SystemQueryStorageArgs criteria = args.getArgs();
        Page<SystemLocalStorageTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return ResponseEntity.ok(localStorageService.queryLocalStorage(criteria, page));
    }

    @Operation(summary = "导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void exportFile(HttpServletResponse response, SystemQueryStorageArgs criteria) throws IOException {
        localStorageService.exportLocalStorageExcel(localStorageService.queryLocalStorage(criteria), response);
    }

    @Operation(summary = "上传文件")
    @PostMapping(value = "/uploadFile")
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity<Void> uploadFile(@RequestParam String name, @RequestParam("file") MultipartFile file) {
        localStorageService.uploadFile(name, file);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "上传图片")
    @PostMapping("/uploadPicture")
    public ResponseEntity<SystemLocalStorageTb> uploadPicture(@RequestParam MultipartFile file) {
        // 判断文件是否为图片
        String suffix = KitFileUtil.getSuffix(file.getOriginalFilename());
        if (!FileTypeEnum.IMAGE.getCode().equals(KitFileUtil.getFileType(suffix))) {
            throw new BadRequestException("只能上传图片");
        }
        SystemLocalStorageTb localStorage = localStorageService.uploadFile(null, file);
        return ResponseEntity.ok(localStorage);
    }

    @Operation(summary = "修改文件")
    @PostMapping(value = "/modifyLocalStorageById")
    @PreAuthorize("@el.check('storage:edit')")
    public ResponseEntity<Void> modifyLocalStorageById(@Validated @RequestBody SystemLocalStorageTb args) {
        localStorageService.modifyLocalStorageById(args);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "多选删除")
    @PostMapping(value = "/removeFileByIds")
    public ResponseEntity<Void> deleteFileByIds(@RequestBody Long[] ids) {
        localStorageService.removeFileByIds(ids);
        return ResponseEntity.ok(null);
    }
}
