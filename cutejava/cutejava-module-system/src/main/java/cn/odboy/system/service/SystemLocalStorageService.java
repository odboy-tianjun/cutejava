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
package cn.odboy.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.server.core.KitFileLocalUploadHelper;
import cn.odboy.system.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.dal.mysql.SystemLocalStorageMapper;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SystemLocalStorageService {

  @Autowired
  private SystemLocalStorageMapper systemLocalStorageMapper;
  @Autowired
  private KitFileLocalUploadHelper fileUploadPathHelper;
  @Autowired
  private AppProperties properties;

  /**
   * 上传
   *
   * @param name          文件名称
   * @param multipartFile 文件
   * @return /
   */
  @Transactional(rollbackFor = Exception.class)
  public SystemLocalStorageTb uploadFile(String name, MultipartFile multipartFile) {
    long size = multipartFile.getSize();
    KitFileUtil.checkSize(properties.getOss().getMaxSize(), size);
    String suffix = KitFileUtil.getSuffix(multipartFile.getOriginalFilename());
    String type = KitFileUtil.getFileType(suffix);
    String uploadDateStr = DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
    File file = KitFileUtil.upload(multipartFile, fileUploadPathHelper.getPath() + uploadDateStr + File.separator);
    if (file == null) {
      throw new BadRequestException("上传失败");
    }
    try {
      String formatSize = KitFileUtil.getSize(size);
      String prefixName =
          StrUtil.isBlank(name) ? KitFileUtil.getPrefix(multipartFile.getOriginalFilename()) : name;
      SystemLocalStorageTb localStorage = new SystemLocalStorageTb();
      localStorage.setRealName(file.getName());
      localStorage.setName(prefixName);
      localStorage.setSuffix(suffix);
      localStorage.setPath(file.getPath());
      localStorage.setType(type);
      localStorage.setSize(formatSize);
      localStorage.setDateGroup(uploadDateStr);
      systemLocalStorageMapper.insert(localStorage);
      return localStorage;
    } catch (Exception e) {
      KitFileUtil.del(file);
      throw e;
    }
  }

  /**
   * 编辑
   *
   * @param args 文件信息
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateLocalStorageById(SystemLocalStorageTb args) {
    SystemLocalStorageTb localStorage = systemLocalStorageMapper.selectById(args.getId());
    localStorage.copy(args);
    systemLocalStorageMapper.insertOrUpdate(localStorage);
  }

  /**
   * 多选删除
   *
   * @param ids /
   */
  @Transactional(rollbackFor = Exception.class)
  public void removeFileByIds(Long[] ids) {
    for (Long id : ids) {
      SystemLocalStorageTb storage = systemLocalStorageMapper.selectById(id);
      try {
        KitFileUtil.del(storage.getPath());
        systemLocalStorageMapper.deleteById(storage);
      } catch (IORuntimeException e) {
        throw new BadRequestException("删除文件 " + storage.getName() + " 失败");
      }
    }
  }

  /**
   * 导出数据
   *
   * @param localStorages 待导出的数据
   * @param response      /
   * @throws IOException /
   */
  public void exportLocalStorageExcel(List<SystemLocalStorageTb> localStorages, HttpServletResponse response)
      throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (SystemLocalStorageTb localStorage : localStorages) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("文件名", localStorage.getRealName());
      map.put("备注名", localStorage.getName());
      map.put("文件类型", localStorage.getType());
      map.put("文件大小", localStorage.getSize());
      map.put("创建者", localStorage.getCreateBy());
      map.put("创建日期", localStorage.getCreateTime());
      list.add(map);
    }
    KitFileUtil.downloadExcel(list, response);
  }

  /**
   * 分页查询
   *
   * @param criteria 条件
   * @param page     分页参数
   * @return /
   */
  public KitPageResult<SystemLocalStorageTb> queryLocalStorage(SystemQueryStorageArgs criteria,
      Page<SystemLocalStorageTb> page) {
    return KitPageUtil.toPage(this.selectLocalStorageByArgs(criteria, page));
  }

  /**
   * 查询全部数据
   *
   * @param criteria 条件
   * @return /
   */
  public List<SystemLocalStorageTb> queryLocalStorage(SystemQueryStorageArgs criteria) {
    return this.selectLocalStorageByArgs(criteria);
  }

  public void injectQueryParams(SystemQueryStorageArgs criteria, LambdaQueryWrapper<SystemLocalStorageTb> wrapper) {
    if (criteria != null) {
      wrapper.and(StrUtil.isNotBlank(criteria.getBlurry()),
          c -> c.like(SystemLocalStorageTb::getName, criteria.getBlurry()).or()
              .like(SystemLocalStorageTb::getSuffix, criteria.getBlurry()).or()
              .like(SystemLocalStorageTb::getType, criteria.getBlurry()).or()
              .like(SystemLocalStorageTb::getCreateBy, criteria.getBlurry()));
      if (CollUtil.isNotEmpty(criteria.getCreateTime()) && criteria.getCreateTime().size() >= 2) {
        wrapper.between(SystemLocalStorageTb::getUpdateTime, criteria.getCreateTime().get(0),
            criteria.getCreateTime().get(1));
      }
    }
    wrapper.orderByDesc(SystemLocalStorageTb::getId);
  }

  public List<SystemLocalStorageTb> selectLocalStorageByArgs(SystemQueryStorageArgs criteria) {
    LambdaQueryWrapper<SystemLocalStorageTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(criteria, wrapper);
    return systemLocalStorageMapper.selectList(wrapper);
  }

  public IPage<SystemLocalStorageTb> selectLocalStorageByArgs(SystemQueryStorageArgs criteria,
      Page<SystemLocalStorageTb> page) {
    LambdaQueryWrapper<SystemLocalStorageTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(criteria, wrapper);
    return systemLocalStorageMapper.selectPage(page, wrapper);
  }
}
