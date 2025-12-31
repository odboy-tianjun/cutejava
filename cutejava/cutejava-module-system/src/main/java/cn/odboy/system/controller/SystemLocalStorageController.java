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
import cn.odboy.system.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.service.SystemLocalStorageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
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

@RestController
@Api(tags = "工具：本地存储管理")
@RequestMapping("/api/localStorage")
public class SystemLocalStorageController {

  @Autowired
  private SystemLocalStorageService localStorageService;

  @ApiOperation("查询文件")
  @PostMapping
  @PreAuthorize("@el.check('storage:list')")
  public ResponseEntity<KitPageResult<SystemLocalStorageTb>> queryLocalStorage(
      @Validated @RequestBody KitPageArgs<SystemQueryStorageArgs> pageArgs) {
    Page<SystemLocalStorageTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(localStorageService.searchLocalStorage(pageArgs.getArgs(), page));
  }

  @ApiOperation("导出数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('storage:list')")
  public void exportFile(HttpServletResponse response, SystemQueryStorageArgs args) throws IOException {
    localStorageService.exportLocalStorageXlsx(response, args);
  }

  @ApiOperation("上传文件")
  @PostMapping(value = "/uploadFile")
  @PreAuthorize("@el.check('storage:add')")
  public ResponseEntity<Void> uploadFile(@RequestParam String name, @RequestParam("file") MultipartFile file) {
    localStorageService.uploadFile(name, file);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("上传图片")
  @PostMapping("/uploadPicture")
  public ResponseEntity<SystemLocalStorageTb> uploadPicture(@RequestParam MultipartFile file) {
    SystemLocalStorageTb localStorage = localStorageService.uploadPictureV1(file);
    return ResponseEntity.ok(localStorage);
  }

  @ApiOperation("修改文件")
  @PostMapping(value = "/modifyLocalStorageById")
  @PreAuthorize("@el.check('storage:edit')")
  public ResponseEntity<Void> modifyLocalStorageById(@Validated @RequestBody SystemLocalStorageTb args) {
    localStorageService.updateLocalStorageById(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("多选删除")
  @PostMapping(value = "/removeFileByIds")
  public ResponseEntity<Void> deleteFileByIds(@RequestBody Long[] ids) {
    localStorageService.deleteFileByIds(ids);
    return ResponseEntity.ok(null);
  }
}
