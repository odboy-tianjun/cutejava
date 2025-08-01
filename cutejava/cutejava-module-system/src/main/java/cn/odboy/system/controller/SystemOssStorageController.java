package cn.odboy.system.controller;

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResult;
import cn.odboy.system.dal.dataobject.SystemOssStorageTb;
import cn.odboy.system.dal.model.SystemOssStorageVo;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.service.SystemOssStorageService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public ResponseEntity<CsPageResult<SystemOssStorageVo>> queryOssStorage(@Validated @RequestBody CsPageArgs<SystemQueryStorageArgs> args) {
        SystemQueryStorageArgs criteria = args.getArgs();
        Page<SystemOssStorageTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemOssStorageService.queryOssStorage(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void exportFile(HttpServletResponse response, SystemQueryStorageArgs criteria) throws IOException {
        systemOssStorageService.exportOssStorageExcel(systemOssStorageService.queryOssStorage(criteria), response);
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
        systemOssStorageService.removeFileByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    @ApiOperation(value = "图片/视频预览")
    //    @GetMapping("/preview-file")
    //    public ResponseEntity<Object> previewFile(@RequestParam("fileName") String fileName) {
    //        // 校验文件名是否有效
    //        if (StrUtil.isBlank(fileName)) {
    //            log.warn("文件名为空，无法生成预签名 URL");
    //            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文件名为空，无法生成预签名 URL");
    //        }
    //
    //        // 确保文件名包含完整的路径（例如：2025/04/28/78bda88f-ff2d-4c4f-9756-397c62c3771f.jpg）
    //        if (!fileName.contains("/")) {
    //            log.warn("请检查文件名是否正确，无法生成预签名 URL");
    //            return null;
    //        }
    //        // 创建返回 map
    //        Map<String, Object> res = new HashMap<>();
    //        // 生成 1 小时有效的预签名 URL
    //        try {
    //            GetPresignedObjectUrlArgs presignedUrlArgs = GetPresignedObjectUrlArgs.builder()
    //                    // 获取 bucket 名称
    //                    .bucket(properties.getOss().getMinio().getBucketName())
    //                    // 使用完整路径
    //                    .object(fileName)
    //                    // 设置方法为 GET（读取文件）
    //                    .method(Method.GET)
    //                    // 设置 URL 过期时间为 1 小时 1*60*60
    //                    .expiry(3600).build();
    //
    //            // 获取预签名 URL
    //            String url = minioClient.getPresignedObjectUrl(presignedUrlArgs);
    //            res.put("data", url);
    //            // 记录生成的 URL
    //            log.info("生成预签名 URL 成功，fileName: {}, url: {}", fileName, url);
    //        } catch (Exception e) {
    //            log.error("生成预签名 URL 失败，fileName: {}", fileName, e);
    //        }
    //        res.put("code", HttpStatus.OK.value());
    //        res.put("msg", "上传成功");
    //        return ResponseEntity.status(HttpStatus.OK).body(res);
    //    }
    //
    //    /**
    //     * 存储桶存在
    //     *
    //     * @param bucketName 存储桶名称
    //     * @return {@link ResponseEntity }<{@link Object }>
    //     */
    //    @ApiOperation(value = "查看存储bucket是否存在")
    //    @GetMapping("/bucketExists")
    //    public ResponseEntity<Object> bucketExists(@RequestParam("bucketName") String bucketName) {
    //        return ResponseEntity.status(HttpStatus.OK).body("查看存储bucket是否存在 bucketName : " + minioRepository.bucketExists(bucketName));
    //    }
    //
    //    /**
    //     * 制作桶
    //     *
    //     * @param bucketName 存储桶名称
    //     * @return {@link ResponseEntity }<{@link Object }>
    //     */
    //    @ApiOperation(value = "创建存储bucket")
    //    @GetMapping("/makeBucket")
    //    public ResponseEntity<Object> makeBucket(String bucketName) {
    //        return ResponseEntity.status(HttpStatus.OK).body("创建存储bucket bucketName : " + minioRepository.makeBucket(bucketName));
    //    }
    //
    //    /**
    //     * 删除存储桶
    //     *
    //     * @param bucketName 存储桶名称
    //     * @return {@link ResponseEntity }<{@link Object }>
    //     */
    //    @ApiOperation(value = "删除存储bucket")
    //    @GetMapping("/removeBucket")
    //    public ResponseEntity<Object> removeBucket(String bucketName) {
    //        return ResponseEntity.status(HttpStatus.OK).body("删除存储bucket bucketName : " + minioRepository.removeBucket(bucketName));
    //    }
    //
    //    /**
    //     * 获取所有存储桶
    //     *
    //     * @return {@link ResponseEntity }<{@link Object }>
    //     */
    //    @ApiOperation(value = "获取全部bucket")
    //    @GetMapping("/queryAllBuckets")
    //    public ResponseEntity<Object> queryAllBuckets() {
    //        List<Bucket> allBuckets = minioRepository.queryAllBuckets();
    //        // 获取名称 allBuckets.forEach(bucket -> log.info(bucket.name()));
    //        List<String> bucketNames = allBuckets.stream().map(Bucket::name).collect(Collectors.toList());
    //        HashMap<String, Object> response = new HashMap<>();
    //        response.put("data", bucketNames);
    //        response.put("code", HttpStatus.OK.value());
    //        response.put("msg", "获取全部bucket成功");
    //        return ResponseEntity.status(HttpStatus.OK).body(response);
    //    }
    //
    //    /**
    //     * 上传
    //     *
    //     * @param file 文件
    //     * @return {@link ResponseEntity }<{@link Object }>
    //     */
    //    @ApiOperation(value = "文件上传返回url")
    //    @PostMapping("/upload")
    //    public ResponseEntity<Object> upload(@RequestParam("file") MultipartFile file) {
    //        if (file.isEmpty()) {
    //            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("文件不能为空");
    //        }
    //        String objectName = minioRepository.upload(file);
    //        if (null != objectName) {
    //            HashMap<String, Object> response = new HashMap<>();
    //            response.put("data", (properties.getOss().getMinio().getEndpoint() + "/" + properties.getOss().getMinio().getBucketName() + "/" + objectName));
    //            response.put("code", HttpStatus.OK.value());
    //            response.put("msg", "上传成功");
    //            return ResponseEntity.status(HttpStatus.OK).body(response);
    //        }
    //        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败");
    //    }
    //
    //    /**
    //     * 文件上传
    //     *
    //     * @param file 文件
    //     * @return 文件在Minio中的路径
    //     */
    //    @ApiOperation(value = "文件上传返回url")
    //    @PostMapping("/upload")
    //    public ResponseEntity<Object> upload(MultipartFile file) {
    //        String originalFilename = file.getOriginalFilename();
    //        if (StrUtil.isBlank(originalFilename)) {
    //            throw new RuntimeException("文件名为空");
    //        }
    //        String fileExtension = FileUtil.getSuffix(originalFilename);
    //        // 使用UUID确保文件名唯一
    //        String fileName = UUID.randomUUID() + fileExtension;
    //        // 使用日期加上文件名，方便分类
    //        String objectName = CsDateUtil.getNowDateTimeMsStr() + "/" + fileName;
    //
    //        log.info("准备上传文件，objectName: {}", objectName);
    //
    //        // 通过更大缓冲区和流优化上传文件
    //        try (InputStream fileInputStream = file.getInputStream()) {
    //            // 上传前记录文件大小和类型
    //            long fileSize = file.getSize();
    //            log.info("上传文件大小：{} 字节, 文件类型：{}", fileSize, file.getContentType());
    //            PutObjectArgs objectArgs = PutObjectArgs.builder()
    //                    .bucket(properties.getOss().getMinio().getBucketName())
    //                    .object(objectName)
    //                    .stream(fileInputStream, fileSize, -1)
    //                    .contentType(file.getContentType())
    //                    .build();
    //            minioClient.putObject(objectArgs);
    //            log.info("文件上传成功，objectName: {}", objectName);
    //            HashMap<String, Object> response = new HashMap<>();
    //            response.put("data", (properties.getOss().getMinio().getEndpoint() + "/" + properties.getOss().getMinio().getBucketName() + "/" + objectName));
    //            response.put("code", HttpStatus.OK.value());
    //            response.put("msg", "上传成功");
    //            return ResponseEntity.status(HttpStatus.OK).body(response);
    //        } catch (IOException e) {
    //            log.error("上传文件失败，objectName: {}", objectName, e);
    //            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败");
    //        } catch (Exception e) {
    //            throw new RuntimeException(e);
    //        }
    //    }
}
