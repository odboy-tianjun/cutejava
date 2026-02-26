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
package cn.odboy.system.service;

import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.request.SystemQueryStorageArgs;
import cn.odboy.system.dal.model.response.SystemOssStorageVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * OSS存储接口
 * </p>
 *
 * @author codegen
 * @since 2025-07-15
 */
public interface SystemOssStorageService extends IService<SystemOssStorageTb> {

  /**
   * 分页查询
   *
   * @param args 查询条件
   * @param page 分页参数
   * @return /
   */
  KitPageResult<SystemOssStorageVo> searchOssStorage(SystemQueryStorageArgs args, Page<SystemOssStorageTb> page);

  /**
   * 复杂条件查询
   *
   * @param args 查询条件
   * @return /
   */
  List<SystemOssStorageVo> queryOssStorage(SystemQueryStorageArgs args);

  /**
   * 上传文件
   *
   * @param file /
   * @return /
   */
  String uploadFile(MultipartFile file);

  /**
   * 根据id删除文件
   *
   * @param ids 文件记录id
   */
  void deleteFileByIds(Long[] ids);

  /**
   * 导出文件上传记录
   *
   * @param response /
   * @param args     查询条件
   */
  void exportOssStorageXlsx(HttpServletResponse response, SystemQueryStorageArgs args);
}
