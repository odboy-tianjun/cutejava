package cn.odboy.system.controller;

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsResultVo;
import cn.odboy.system.dal.dataobject.SystemQiniuConfigTb;
import cn.odboy.system.dal.dataobject.SystemQiniuContentTb;
import cn.odboy.system.dal.model.QuerySystemQiniuArgs;
import cn.odboy.system.service.SystemQiniuConfigService;
import cn.odboy.system.service.SystemQiniuContentService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qiNiuContent")
@Api(tags = "工具：七牛云存储管理")
public class SystemQiniuController {
    private final SystemQiniuContentService qiniuContentService;
    private final SystemQiniuConfigService qiNiuConfigService;

    @ApiOperation("查询七牛云存储配置")
    @PostMapping(value = "/queryQiniuConfig")
    public ResponseEntity<SystemQiniuConfigTb> queryQiniuConfig() {
        return new ResponseEntity<>(qiNiuConfigService.getLastQiniuConfig(), HttpStatus.OK);
    }

    @ApiOperation("配置七牛云存储")
    @PostMapping(value = "/modifyQiniuConfig")
    public ResponseEntity<Void> modifyQiniuConfig(@Validated @RequestBody SystemQiniuConfigTb qiniuConfig) {
        qiNiuConfigService.saveQiniuConfig(qiniuConfig);
        qiNiuConfigService.modifyQiniuConfigType(qiniuConfig.getType());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportQiNiu(HttpServletResponse response, QuerySystemQiniuArgs criteria) throws IOException {
        qiniuContentService.exportQiniuContentExcel(qiniuContentService.queryQiniuContentByArgs(criteria), response);
    }

    @ApiOperation("查询文件")
    @PostMapping
    public ResponseEntity<CsResultVo<List<SystemQiniuContentTb>>> queryQiniuContentByArgs(@Validated @RequestBody CsPageArgs<QuerySystemQiniuArgs> args) {
        QuerySystemQiniuArgs criteria = args.getArgs();
        Page<SystemQiniuContentTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(qiniuContentService.queryQiniuContentByArgs(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("上传文件")
    @PostMapping("/uploadFile")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam MultipartFile file) {
        SystemQiniuContentTb qiniuContent = qiniuContentService.uploadFile(file);
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", qiniuContent.getId());
        map.put("errno", 0);
        map.put("data", new String[]{qiniuContent.getUrl()});
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ApiOperation("同步七牛云数据")
    @PostMapping(value = "/synchronize")
    public ResponseEntity<Void> synchronizeQiNiu() {
        qiniuContentService.synchronize();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("下载文件")
    @GetMapping(value = "/createFilePreviewUrl/{id}")
    public ResponseEntity<Map<String, Object>> createFilePreviewUrl(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("url", qiniuContentService.createFilePreviewUrl(qiniuContentService.getQiniuContentById(id)));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ApiOperation("删除文件")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteQiNiu(@PathVariable Long id) {
        qiniuContentService.removeFileById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("删除多张图片")
    @PostMapping(value = "/removeFileByIds")
    public ResponseEntity<Void> removeFileByIds(@RequestBody Long[] ids) {
        qiniuContentService.removeFileByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
