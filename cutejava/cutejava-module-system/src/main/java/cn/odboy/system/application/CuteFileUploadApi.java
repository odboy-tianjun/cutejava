package cn.odboy.system.application;

import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.service.SystemLocalStorageService;
import cn.odboy.system.service.SystemOssStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
  private SystemLocalStorageService systemLocalStorageService;
  @Autowired
  private SystemOssStorageService systemOssStorageService;

  @OperationLog
  @ApiOperation("上传文件到服务器本地")
  @PostMapping(value = "/uploadLocal")
  public ResponseEntity<String> uploadLocal(@RequestParam("file") MultipartFile file) {
    String fileUrl = systemLocalStorageService.uploadLocal(file);
    return ResponseEntity.ok(fileUrl);
  }

  @OperationLog
  @ApiOperation("上传文件到OSS")
  @PostMapping(value = "/uploadOSS")
  public ResponseEntity<String> uploadOSS(@RequestParam("file") MultipartFile file) {
    String fileUrl = systemOssStorageService.uploadFile(file);
    return ResponseEntity.ok(fileUrl);
  }
}
