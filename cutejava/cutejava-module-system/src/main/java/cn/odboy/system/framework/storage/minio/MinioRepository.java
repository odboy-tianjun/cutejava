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
package cn.odboy.system.framework.storage.minio;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.properties.model.OSSConfigModel;
import cn.odboy.framework.properties.model.StorageOSSModel;
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.util.KitDateUtil;
import cn.odboy.util.KitFileUtil;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
public class MinioRepository {
    @Autowired private AppProperties properties;
    @Autowired private MinioClient minioClient;

    /**
     * 检查存储bucket是否存在
     *
     * @param bucketName Bucket 名称
     * @return boolean 存在返回 true，否则返回 false
     */
    public boolean existsBucket(String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            log.error("检查 bucket 是否存在失败，bucketName: {}, 错误信息", bucketName, e);
            return false;
        }
    }

    /**
     * 创建存储bucket
     *
     * @param bucketName Bucket 名称
     * @return boolean 创建成功返回 true，否则返回 false
     */
    public boolean createBucket(String bucketName) {
        try {
            if (!existsBucket(bucketName)) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket 创建成功，bucketName: {}", bucketName);
                return true;
            } else {
                log.warn("Bucket 已经存在，bucketName: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("创建 bucket 失败，bucketName: {}, 错误信息", bucketName, e);
        }
        return false;
    }

    /**
     * 删除存储bucket
     *
     * @param bucketName Bucket 名称
     * @return boolean 删除成功返回 true，否则返回 false
     */
    public boolean removeBucket(String bucketName) {
        try {
            if (existsBucket(bucketName)) {
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket 删除成功，bucketName: {}", bucketName);
                return true;
            } else {
                log.warn("Bucket 不存在，bucketName: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("删除 bucket 失败，bucketName: {}, 错误信息", bucketName, e);
        }
        return false;
    }

    /**
     * 获取所有 bucket 列表
     *
     * @return List<Bucket> 所有的 Bucket 列表
     */
    public List<Bucket> queryAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            log.error("获取所有 buckets 失败，错误信息", e);
        }
        return Collections.emptyList();
    }

    /**
     * 文件上传
     *
     * @param tempFile         文件
     * @param originalFilename 源文件名
     * @param fileSize         文件大小
     * @param contentType      文件类型
     * @param md5              文件md5
     * @return String 上传后的文件名，上传失败返回 null
     */
    public SystemOssStorageTb upload(File tempFile, String originalFilename, long fileSize, String contentType,
        String md5) {
        StorageOSSModel ossConfig = properties.getOss();
        OSSConfigModel ossMinioConfig = ossConfig.getMinio();
        String type = FileTypeUtil.getType(tempFile);
        String prefix = KitFileUtil.getPrefix(tempFile);
        String suffix = KitFileUtil.getSuffix(tempFile);
        String fileCode = IdUtil.fastSimpleUUID();
        String fileName = fileCode + FileUtil.getSuffix(originalFilename);
        String objectName = KitDateUtil.getNowDateStr() + "/" + fileName;
        log.info("上传文件，原文件名：{}，上传后文件名：{}", originalFilename, fileName);
        String fileUrl = ossMinioConfig.getEndpoint() + "/" + ossMinioConfig.getBucketName() + "/" + objectName;
        SystemOssStorageTb systemOssStorageTb = new SystemOssStorageTb();
        systemOssStorageTb.setServiceType("minio");
        systemOssStorageTb.setEndpoint(ossMinioConfig.getEndpoint());
        systemOssStorageTb.setBucketName(StrUtil.trim(ossMinioConfig.getBucketName()));
        systemOssStorageTb.setFileName(originalFilename);
        systemOssStorageTb.setFileSize(fileSize);
        systemOssStorageTb.setFileMime(type);
        systemOssStorageTb.setFilePrefix(prefix);
        systemOssStorageTb.setFileSuffix(suffix);
        systemOssStorageTb.setFileMd5(md5);
        systemOssStorageTb.setFileUrl(fileUrl);
        systemOssStorageTb.setFileCode(fileCode);
        systemOssStorageTb.setObjectName(objectName);
        try (InputStream fileInputStream = KitFileUtil.getInputStream(tempFile)) {
            PutObjectArgs objectArgs = PutObjectArgs.builder().bucket(ossMinioConfig.getBucketName()).object(objectName)
                .stream(fileInputStream, fileSize, -1).contentType(contentType).build();
            minioClient.putObject(objectArgs);
            log.info("文件上传成功，并删除临时文件，文件名：{}", originalFilename);
            return systemOssStorageTb;
        } catch (Exception e) {
            log.error("文件上传失败，文件名：{}，错误信息", originalFilename, e);
        } finally {
            try {
                KitFileUtil.del(tempFile);
            } catch (IORuntimeException e) {
                // 删除失败忽略
            }
        }
        return null;
    }

    /**
     * 优化文件上传 文件上传：优化版
     *
     * @param file
     * @return {@link String }
     */
    public String optimizedUpload(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (StrUtil.isBlank(originalFilename)) {
            throw new BadRequestException("文件名不能为空");
        }
        long size = file.getSize();
        KitFileUtil.checkSize(properties.getOss().getMaxSize(), size);
        String fileName = UUID.randomUUID() + FileUtil.getSuffix(originalFilename);
        // yyyy/MM/dd/fileName.xxx
        String objectName = KitDateUtil.getNowDateTimeStr() + "/" + fileName;
        log.info("准备上传文件，原文件名：{}，上传后文件名：{}", originalFilename, fileName);
        try (InputStream fileInputStream = file.getInputStream()) {
            PutObjectArgs objectArgs =
                PutObjectArgs.builder().bucket(properties.getOss().getMinio().getBucketName()).object(objectName)
                    .stream(fileInputStream, file.getSize(), -1).contentType(file.getContentType()).build();
            // 使用重试机制进行上传
            uploadWithRetry(objectArgs, 3);
            log.info("文件上传成功，objectName: {}", objectName);
            return objectName;
        } catch (Exception e) {
            log.error("文件上传失败，文件名：{}，错误信息", originalFilename, e);
        }
        return null;
    }

    /**
     * 上传重试机制
     *
     * @param objectArgs 对象参数
     * @param maxRetries 最大重试次数
     * @throws Exception 例外
     */
    private void uploadWithRetry(PutObjectArgs objectArgs, int maxRetries) throws Exception {
        int attempt = 0;
        while (attempt < maxRetries) {
            try {
                minioClient.putObject(objectArgs);
                return; // 上传成功，返回
            } catch (Exception e) {
                attempt++;
                log.warn("上传失败，第 {} 次尝试，错误信息", attempt, e);
                if (attempt == maxRetries) {
                    throw new BadRequestException("上传失败，已达到最大重试次数");
                }
                // 等待一段时间后重试
                Thread.sleep(2000);
            }
        }
    }

    /**
     * 生成文件的预签名 URL，用于文件预览
     *
     * @param fullFileName 文件完整路径
     * @return String 预签名 URL
     */
    public String generatePreviewUrl(String fullFileName) {
        if (StrUtil.isBlank(fullFileName)) {
            log.warn("文件名为空，无法生成预签名 URL");
            return null;
        }
        try {
            GetPresignedObjectUrlArgs presignedUrlArgs =
                GetPresignedObjectUrlArgs.builder().bucket(properties.getOss().getMinio().getBucketName())
                    .object(fullFileName).method(Method.GET)
                    // 设置 URL 过期时间为 7 天, 最大 7 天, 别挣扎了
                    .expiry(3600 * 24 * 7).build();
            String url = minioClient.getPresignedObjectUrl(presignedUrlArgs);
            log.info("生成预签名 URL 成功，fileName: {}, url: {}", fullFileName, url);
            return url;
        } catch (Exception e) {
            log.error("生成预签名 URL 失败，fileName: {}", fullFileName, e);
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名称
     * @param res      HTTP 响应
     */
    public void download(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs =
            GetObjectArgs.builder().bucket(properties.getOss().getMinio().getBucketName()).object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs);
            FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = response.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
            byte[] bytes = os.toByteArray();
            res.setCharacterEncoding("utf-8");
            res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
            try (ServletOutputStream stream = res.getOutputStream()) {
                stream.write(bytes);
                stream.flush();
            }
        } catch (Exception e) {
            log.error("下载文件失败，fileName: {}, 错误信息", fileName, e);
        }
    }

    /**
     * 文件下载：优化版
     *
     * @param fileName 文件名称
     * @param res      HTTP 响应
     */
    public void downloadOptimizedVersion(String fileName, HttpServletResponse res) {
        GetObjectArgs objectArgs =
            GetObjectArgs.builder().bucket(properties.getOss().getMinio().getBucketName()).object(fileName).build();
        try (GetObjectResponse response = minioClient.getObject(objectArgs)) {
            // 增大缓冲区
            byte[] buf = new byte[1024 * 4];
            int len;
            // 分块下载并输出到响应流
            try (ServletOutputStream stream = res.getOutputStream()) {
                res.setCharacterEncoding("utf-8");
                res.addHeader("Content-Disposition", "attachment;fileName=" + fileName);
                res.setHeader("Cache-Control", "no-store");
                res.setHeader("Pragma", "no-cache");
                res.setHeader("Expires", "0");
                while ((len = response.read(buf)) != -1) {
                    stream.write(buf, 0, len);
                    // 确保实时写入
                    stream.flush();
                }
                log.info("文件下载成功，fileName: {}", fileName);
            }
        } catch (Exception e) {
            log.error("下载文件失败，fileName: {}, 错误信息", fileName, e);
        }
    }

    /**
     * 查看文件对象列表
     *
     * @return List<Item> 文件对象列表
     */
    public List<Item> queryAllObjects() {
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(properties.getOss().getMinio().getBucketName()).build());
            List<Item> items = new ArrayList<>();
            for (Result<Item> result : results) {
                items.add(result.get());
            }
            return items;
        } catch (Exception e) {
            log.error("列出文件对象失败，错误信息", e);
        }
        return Collections.emptyList();
    }

    /**
     * 删除文件
     *
     * @param fileName 文件名
     * @return boolean 删除成功返回 true，否则返回 false
     */
    public boolean removeBucketFile(String fileName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(properties.getOss().getMinio().getBucketName()).object(fileName)
                    .build());
            log.info("删除文件成功，fileName: {}", fileName);
            return true;
        } catch (Exception e) {
            log.error("删除文件失败，fileName: {}, 错误信息", fileName, e);
        }
        return false;
    }
}
