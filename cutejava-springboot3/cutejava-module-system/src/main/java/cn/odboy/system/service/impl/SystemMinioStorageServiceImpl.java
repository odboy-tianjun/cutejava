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
package cn.odboy.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.properties.model.StorageOSSModel;
import cn.odboy.framework.server.core.KitFileLocalUploadHelper;
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.export.SystemOssStorageExportRowVo;
import cn.odboy.system.dal.model.request.SystemQueryStorageArgs;
import cn.odboy.system.dal.model.response.SystemOssStorageVo;
import cn.odboy.system.dal.mysql.SystemOssStorageMapper;
import cn.odboy.system.framework.storage.minio.MinioRepository;
import cn.odboy.system.service.SystemOssStorageService;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitDateUtil;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import cn.odboy.util.KitValidUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.List;

/**
 * <p>
 * OSS存储 Minio服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-07-15
 */
@Service
public class SystemMinioStorageServiceImpl extends ServiceImpl<SystemOssStorageMapper, SystemOssStorageTb>
    implements SystemOssStorageService {

  @Autowired
  private MinioRepository minioRepository;
  @Autowired
  private AppProperties properties;
  @Autowired
  private KitFileLocalUploadHelper fileLocalUploadHelper;
  @Autowired
  private SystemOssStorageMapper systemOssStorageMapper;

  @Override
  public KitPageResult<SystemOssStorageVo> searchOssStorage(
      SystemQueryStorageArgs args,
      Page<SystemOssStorageTb> page
  ) {
    IPage<SystemOssStorageVo> ossStorageTbs = this.selectOssStorageByArgs(args, page);
    for (SystemOssStorageVo storageVo : ossStorageTbs.getRecords()) {
      storageVo.setFileSizeDesc(KitFileUtil.getSize(storageVo.getFileSize()));
    }
    return KitPageUtil.toPage(ossStorageTbs);
  }

  private IPage<SystemOssStorageVo> selectOssStorageByArgs(
      SystemQueryStorageArgs args,
      Page<SystemOssStorageTb> page
  ) {
    KitValidUtil.notNull(args);
    LambdaQueryWrapper<SystemOssStorageTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.and(
        StrUtil.isNotBlank(args.getBlurry()),
        c -> c.like(SystemOssStorageTb::getFileName, args.getBlurry()).or()
            .like(SystemOssStorageTb::getFilePrefix, args.getBlurry()).or()
            .like(SystemOssStorageTb::getFileMime, args.getBlurry()).or()
            .like(SystemOssStorageTb::getFileMd5, args.getBlurry())
    );
    if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
      wrapper.between(SystemOssStorageTb::getUpdateTime, args.getCreateTime().get(0),
          args.getCreateTime().get(1)
      );
    }
    wrapper.orderByAsc(SystemOssStorageTb::getId);
    return systemOssStorageMapper.selectPage(page, wrapper)
        .convert(i -> KitBeanUtil.copyToClass(i, SystemOssStorageVo.class));
  }

  @Override
  public List<SystemOssStorageVo> queryOssStorage(SystemQueryStorageArgs args) {
    // 防止刷数据
    Page<SystemOssStorageTb> page = new Page<>(1, 500);
    return searchOssStorage(args, page).getContent();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String uploadFile(MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
    if (StrUtil.isBlank(originalFilename)) {
      throw new BadRequestException("文件名不能为空");
    }
    StorageOSSModel ossConfig = properties.getOss();
    long fileSize = file.getSize();
    String contentType = file.getContentType();
    KitFileUtil.checkSize(ossConfig.getMaxSize(), fileSize);
    // 按天分组
    String nowDateStr = KitDateUtil.getNowDateStr();
    // 上传到本地临时目录
    File tempFile = KitFileUtil.upload(file, fileLocalUploadHelper.getPath() + nowDateStr + File.separator);
    if (tempFile == null) {
      throw new BadRequestException("上传失败");
    }
    // 校验文件md5, 看是否已存在云端（不确定, 可能云端已经删除, 但是正常来说云端是不允许私自删除的, 所以这里忽略云端不存在的情况）
    String md5 = KitFileUtil.getMd5(tempFile);
    SystemOssStorageTb systemOssStorageTb = this.getOssStorageByMd5(md5);
    if (systemOssStorageTb != null) {
      // 重新生成7天链接
      return minioRepository.generatePreviewUrl(systemOssStorageTb.getObjectName());
    }
    // 上传到OSS
    SystemOssStorageTb storageTb = minioRepository.upload(tempFile, originalFilename, fileSize, contentType, md5);
    if (storageTb == null) {
      throw new BadRequestException("文件上传失败");
    }
    save(storageTb);
    return minioRepository.generatePreviewUrl(storageTb.getObjectName());
  }

  /**
   * 多选删除
   *
   * @param ids /
   */
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteFileByIds(Long[] ids) {
    for (Long id : ids) {
      SystemOssStorageTb storage = getById(id);
      minioRepository.removeBucketFile(storage.getObjectName());
      removeById(storage);
    }
  }

  @Override
  public void exportOssStorageXlsx(HttpServletResponse response, SystemQueryStorageArgs args) {
    List<SystemOssStorageVo> systemOssStorageVos = this.queryOssStorage(args);
    //    KitXlsxExportUtil.exportFile(response, "OSS文件上传记录数据", systemOssStorageVos, SystemOssStorageExportRowVo.class,
    //        (dataObject) -> CollUtil.newArrayList(KitBeanUtil.copyProperties(dataObject, SystemOssStorageExportRowVo.class)));
    List<SystemOssStorageExportRowVo> rowVos =
        KitBeanUtil.copyToList(systemOssStorageVos, SystemOssStorageExportRowVo.class);
    KitExcelExporter.exportSimple(response, "OSS文件上传记录数据", SystemOssStorageExportRowVo.class, rowVos);
  }

  private SystemOssStorageTb getOssStorageByMd5(String md5) {
    return lambdaQuery().eq(SystemOssStorageTb::getFileMd5, md5).one();
  }
}
