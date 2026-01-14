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
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.request.SystemQueryStorageArgs;
import cn.odboy.system.dal.model.response.SystemOssStorageVo;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemOssStorageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@Api(tags = "工具：OSS存储管理")
@RequestMapping("/api/ossStorage")
public class SystemOssStorageController {

  @Autowired
  private SystemOssStorageService systemOssStorageService;

  @ApiOperation("查询文件")
  @PostMapping(value = "/searchOssStorage")
  @PreAuthorize("@el.check('storage:list')")
  public ResponseEntity<KitPageResult<SystemOssStorageVo>> queryOssStorage(@Validated @RequestBody KitPageArgs<SystemQueryStorageArgs> pageArgs) {
    Page<SystemOssStorageTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemOssStorageService.searchOssStorage(pageArgs.getArgs(), page));
  }

  @OperationLog
  @ApiOperation("导出OSS存储记录数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('storage:list')")
  public void exportFile(HttpServletResponse response, SystemQueryStorageArgs args) {
    systemOssStorageService.exportOssStorageXlsx(response, args);
  }

  @OperationLog
  @ApiOperation("上传文件到OSS")
  @PostMapping(value = "/uploadFile")
  @PreAuthorize("@el.check('storage:add')")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    String fileUrl = systemOssStorageService.uploadFile(file);
    return ResponseEntity.ok(fileUrl);
  }

  @OperationLog
  @ApiOperation("从OSS删除文件")
  @PostMapping(value = "/deleteFileByIds")
  public ResponseEntity<Void> deleteFileByIds(@RequestBody Long[] ids) {
    systemOssStorageService.deleteFileByIds(ids);
    return ResponseEntity.ok(null);
  }
}
