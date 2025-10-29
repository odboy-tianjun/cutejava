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

package cn.odboy.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.CsPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.properties.model.StorageOSSModel;
import cn.odboy.framework.server.core.CsFileLocalUploadHelper;
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.SystemOssStorageVo;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.dal.mysql.SystemOssStorageMapper;
import cn.odboy.system.framework.storage.minio.MinioRepository;
import cn.odboy.system.service.SystemOssStorageService;
import cn.odboy.util.CsDateUtil;
import cn.odboy.util.CsFileUtil;
import cn.odboy.util.CsPageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * OSS存储 服务实现类
 * </p>
 *
 * @author codegen
 * @since 2025-07-15
 */
@Service
public class SystemOssStorageServiceImpl extends ServiceImpl<SystemOssStorageMapper, SystemOssStorageTb> implements SystemOssStorageService {
    @Autowired
    private MinioRepository minioRepository;
    @Autowired
    private AppProperties properties;
    @Autowired
    private CsFileLocalUploadHelper fileLocalUploadHelper;

    @Override
    public CsPageResult<SystemOssStorageVo> queryOssStorage(SystemQueryStorageArgs criteria, Page<SystemOssStorageTb> page) {
        IPage<SystemOssStorageTb> ossStorageTbs = baseMapper.selectOssStorageByArgs(criteria, page);
        IPage<SystemOssStorageVo> convert = ossStorageTbs.convert(c -> {
            SystemOssStorageVo storageVo = BeanUtil.copyProperties(c, SystemOssStorageVo.class);
            storageVo.setFileSizeDesc(CsFileUtil.getSize(storageVo.getFileSize()));
            return storageVo;
        });
        return CsPageUtil.toPage(convert);
    }

    @Override
    public List<SystemOssStorageVo> queryOssStorage(SystemQueryStorageArgs criteria) {
        // 防止刷数据
        Page<SystemOssStorageTb> page = new Page<>(1, 500);
        return queryOssStorage(criteria, page).getContent();
    }

    @Override
    public void exportOssStorageExcel(List<SystemOssStorageVo> ossStorages, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemOssStorageVo ossStorage : ossStorages) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("服务类型", ossStorage.getServiceType());
            map.put("服务地址", ossStorage.getEndpoint());
            map.put("存储桶名称", ossStorage.getBucketName());
            map.put("文件名称", ossStorage.getFileName());
            map.put("文件大小", ossStorage.getFileSizeDesc());
            map.put("文件类型", ossStorage.getFileMime());
            map.put("创建人", ossStorage.getCreateBy());
            map.put("创建日期", ossStorage.getCreateTime());
            list.add(map);
        }
        CsFileUtil.downloadExcel(list, response);
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
        CsFileUtil.checkSize(ossConfig.getMaxSize(), fileSize);
        // 按天分组
        String nowDateStr = CsDateUtil.getNowDateStr();
        // 上传到本地临时目录
        File tempFile = CsFileUtil.upload(file, fileLocalUploadHelper.getPath() + nowDateStr + File.separator);
        if (tempFile == null) {
            throw new BadRequestException("上传失败");
        }
        // 校验文件md5, 看是否已存在云端（不确定, 可能云端已经删除, 但是正常来说云端是不允许私自删除的, 所以这里忽略云端不存在的情况）
        String md5 = CsFileUtil.getMd5(tempFile);
        SystemOssStorageTb systemOssStorageTb = getByMd5(md5);
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
    public void removeFileByIds(Long[] ids) {
        for (Long id : ids) {
            SystemOssStorageTb storage = getById(id);
            minioRepository.removeBucketFile(storage.getObjectName());
            removeById(storage);
        }
    }

    @Override
    public SystemOssStorageTb getByMd5(String md5) {
        return lambdaQuery().eq(SystemOssStorageTb::getFileMd5, md5).one();
    }
}
