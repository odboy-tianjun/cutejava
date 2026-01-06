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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.constant.FileTypeEnum;
import cn.odboy.framework.context.KitSpringBeanHolder;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.server.core.KitFileLocalUploadHelper;
import cn.odboy.system.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.system.dal.model.SystemLocalStorageExportRowVo;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.dal.mysql.SystemLocalStorageMapper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.File;
import java.util.Date;
import java.util.List;
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
  public void deleteFileByIds(Long[] ids) {
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
   * 分页查询
   *
   * @param args 条件
   * @param page 分页参数
   * @return /
   */
  public KitPageResult<SystemLocalStorageTb> searchLocalStorage(SystemQueryStorageArgs args,
      Page<SystemLocalStorageTb> page) {
    LambdaQueryWrapper<SystemLocalStorageTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(args, wrapper);
    Page<SystemLocalStorageTb> selectPage = systemLocalStorageMapper.selectPage(page, wrapper);
    return KitPageUtil.toPage(selectPage);
  }

  private void injectQueryParams(SystemQueryStorageArgs args, LambdaQueryWrapper<SystemLocalStorageTb> wrapper) {
    if (args != null) {
      wrapper.and(StrUtil.isNotBlank(args.getBlurry()),
          c -> c.like(SystemLocalStorageTb::getName, args.getBlurry()).or()
              .like(SystemLocalStorageTb::getSuffix, args.getBlurry()).or()
              .like(SystemLocalStorageTb::getType, args.getBlurry()).or()
              .like(SystemLocalStorageTb::getCreateBy, args.getBlurry()));
      if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
        wrapper.between(SystemLocalStorageTb::getUpdateTime, args.getCreateTime().get(0),
            args.getCreateTime().get(1));
      }
    }
    wrapper.orderByDesc(SystemLocalStorageTb::getId);
  }

  public List<SystemLocalStorageTb> queryLocalStorageByArgs(SystemQueryStorageArgs args) {
    LambdaQueryWrapper<SystemLocalStorageTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(args, wrapper);
    return systemLocalStorageMapper.selectList(wrapper);
  }


  public SystemLocalStorageTb uploadPictureV1(MultipartFile file) {
    // 判断文件是否为图片
    String suffix = KitFileUtil.getSuffix(file.getOriginalFilename());
    if (!FileTypeEnum.IMAGE.getCode().equals(KitFileUtil.getFileType(suffix))) {
      throw new BadRequestException("只能上传图片");
    }
    return KitSpringBeanHolder.getBean(SystemLocalStorageService.class).uploadFile(null, file);
  }

  public void exportLocalStorageXlsx(HttpServletResponse response, SystemQueryStorageArgs args) {
    List<SystemLocalStorageTb> systemLocalStorageTbs = this.queryLocalStorageByArgs(args);
//    KitXlsxExportUtil.exportFile(response, "文件上传记录数据", systemLocalStorageTbs, SystemLocalStorageExportRowVo.class,
//        (dataObject) -> CollUtil.newArrayList(KitBeanUtil.copyProperties(dataObject, SystemLocalStorageExportRowVo.class)));
    List<SystemLocalStorageExportRowVo> rowVos = KitBeanUtil.copyToList(systemLocalStorageTbs, SystemLocalStorageExportRowVo.class);
    KitExcelExporter.exportSimple(response, "文件上传记录数据", SystemLocalStorageExportRowVo.class, rowVos);
  }
}
