package cn.odboy.rest;

import cn.odboy.base.PageResult;
import cn.odboy.model.tools.domain.QiniuConfig;
import cn.odboy.model.tools.domain.QiniuContent;
import cn.odboy.model.tools.dto.QiniuQueryCriteria;
import cn.odboy.service.QiNiuConfigService;
import cn.odboy.service.QiniuContentService;
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
import java.util.Map;

/**
 * 发送邮件
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qiNiuContent")
@Api(tags = "工具：七牛云存储管理")
public class QiniuController {

    private final QiniuContentService qiniuContentService;
    private final QiNiuConfigService qiNiuConfigService;

    @GetMapping(value = "/config")
    public ResponseEntity<QiniuConfig> queryQiNiuConfig() {
        return new ResponseEntity<>(qiNiuConfigService.getConfig(), HttpStatus.OK);
    }

    @ApiOperation("配置七牛云存储")
    @PutMapping(value = "/config")
    public ResponseEntity<Object> updateQiNiuConfig(@Validated @RequestBody QiniuConfig qiniuConfig) {
        qiNiuConfigService.saveConfig(qiniuConfig);
        qiNiuConfigService.updateType(qiniuConfig.getType());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportQiNiu(HttpServletResponse response, QiniuQueryCriteria criteria) throws IOException {
        qiniuContentService.downloadList(qiniuContentService.queryAll(criteria), response);
    }

    @ApiOperation("查询文件")
    @GetMapping
    public ResponseEntity<PageResult<QiniuContent>> queryQiNiu(QiniuQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(qiniuContentService.queryAll(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("上传文件")
    @PostMapping
    public ResponseEntity<Object> uploadQiNiu(@RequestParam MultipartFile file) {
        QiniuContent qiniuContent = qiniuContentService.upload(file, qiNiuConfigService.getConfig());
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", qiniuContent.getId());
        map.put("errno", 0);
        map.put("data", new String[]{qiniuContent.getUrl()});
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ApiOperation("同步七牛云数据")
    @PostMapping(value = "/synchronize")
    public ResponseEntity<Object> synchronizeQiNiu() {
        qiniuContentService.synchronize(qiNiuConfigService.getConfig());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("下载文件")
    @GetMapping(value = "/download/{id}")
    public ResponseEntity<Object> downloadQiNiu(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("url", qiniuContentService.download(qiniuContentService.getById(id), qiNiuConfigService.getConfig()));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ApiOperation("删除文件")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteQiNiu(@PathVariable Long id) {
        qiniuContentService.delete(qiniuContentService.getById(id), qiNiuConfigService.getConfig());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("删除多张图片")
    @DeleteMapping
    public ResponseEntity<Object> deleteAllQiNiu(@RequestBody Long[] ids) {
        qiniuContentService.deleteAll(ids, qiNiuConfigService.getConfig());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
