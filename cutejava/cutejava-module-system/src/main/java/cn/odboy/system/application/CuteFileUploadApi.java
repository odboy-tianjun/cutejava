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

import cn.odboy.system.application.service.CuteFileUploadService;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemLocalStorageService;
import cn.odboy.system.service.SystemOssStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "系统组件：CuteFileUpload")
@RequestMapping("/api/component/CuteFileUpload")
public class CuteFileUploadApi {

  @Autowired
  private CuteFileUploadService cuteFileUploadService;
  @Autowired
  private SystemOssStorageService systemOssStorageService;

  @OperationLog
  @ApiOperation("上传文件到服务器本地")
  @PostMapping(value = "/uploadLocal")
  @PreAuthorize("@el.check()")
  public ResponseEntity<String> uploadLocal(@RequestParam("file") MultipartFile file) {
    String fileUrl = cuteFileUploadService.uploadLocal(file);
    return ResponseEntity.ok(fileUrl);
  }

  @OperationLog
  @ApiOperation("上传文件到OSS")
  @PostMapping(value = "/uploadOSS")
  @PreAuthorize("@el.check()")
  public ResponseEntity<String> uploadOSS(@RequestParam("file") MultipartFile file) {
    String fileUrl = systemOssStorageService.uploadFile(file);
    return ResponseEntity.ok(fileUrl);
  }
}
