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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.SystemOssStorageExportRowVo;
import cn.odboy.system.dal.model.SystemOssStorageVo;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.dal.model.SystemUserOnlineVo;
import cn.odboy.system.service.SystemOssStorageService;
import cn.odboy.util.xlsx.KitExcelExporter;
import cn.odboy.util.xlsx.KitXlsxExportUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
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
  @PostMapping
  @PreAuthorize("@el.check('storage:list')")
  public ResponseEntity<KitPageResult<SystemOssStorageVo>> queryOssStorage(
      @Validated @RequestBody KitPageArgs<SystemQueryStorageArgs> pageArgs) {
    Page<SystemOssStorageTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemOssStorageService.searchOssStorage(pageArgs.getArgs(), page));
  }

  @ApiOperation("导出数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('storage:list')")
  public void exportFile(HttpServletResponse response, SystemQueryStorageArgs args) throws IOException {
    List<SystemOssStorageVo> systemOssStorageVos = systemOssStorageService.queryOssStorage(args);
//    KitXlsxExportUtil.exportFile(response, "OSS文件上传记录数据", systemOssStorageVos, SystemOssStorageExportRowVo.class,
//        (dataObject) -> CollUtil.newArrayList(BeanUtil.copyProperties(dataObject, SystemOssStorageExportRowVo.class)));
    List<SystemOssStorageExportRowVo> rowVos = BeanUtil.copyToList(systemOssStorageVos, SystemOssStorageExportRowVo.class);
    KitExcelExporter.exportSimple(response, "OSS文件上传记录数据", SystemOssStorageExportRowVo.class, rowVos);
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
    systemOssStorageService.deleteFileByIds(ids);
    return ResponseEntity.ok(null);
  }
}
