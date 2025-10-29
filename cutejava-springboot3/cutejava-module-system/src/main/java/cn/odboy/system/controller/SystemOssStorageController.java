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
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.SystemOssStorageVo;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.service.SystemOssStorageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@Api(tags = "工具：OSS存储管理")
@RequestMapping("/api/ossStorage")
public class SystemOssStorageController {
    @Autowired
    private SystemOssStorageService systemOssStorageService;

    @ApiOperation("查询文件")
    @PostMapping
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<CsPageResult<SystemOssStorageVo>> queryOssStorage(@Validated @RequestBody CsPageArgs<SystemQueryStorageArgs> args) {
        SystemQueryStorageArgs criteria = args.getArgs();
        Page<SystemOssStorageTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return ResponseEntity.ok(systemOssStorageService.queryOssStorage(criteria, page));
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void exportFile(HttpServletResponse response, SystemQueryStorageArgs criteria) throws IOException {
        systemOssStorageService.exportOssStorageExcel(systemOssStorageService.queryOssStorage(criteria), response);
    }

    @ApiOperation("上传文件")
    @PostMapping(value = "/uploadFile")
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = systemOssStorageService.uploadFile(file);
        return ResponseEntity.ok(fileUrl);
    }

    @ApiOperation("多选删除")
    @PostMapping(value = "/removeFileByIds")
    public ResponseEntity<Void> removeFileByIds(@RequestBody Long[] ids) {
        systemOssStorageService.removeFileByIds(ids);
        return ResponseEntity.ok(null);
    }
}
