package cn.odboy.core.controller.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemQiniuConfigTb;
import cn.odboy.core.dal.dataobject.system.SystemQiniuContentTb;
import cn.odboy.core.service.system.SystemQiniuConfigApi;
import cn.odboy.core.service.system.SystemQiniuConfigService;
import cn.odboy.core.service.system.SystemQiniuContentApi;
import cn.odboy.core.service.system.SystemQiniuContentService;
import cn.odboy.core.dal.model.system.QuerySystemQiniuArgs;
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
    private final SystemQiniuConfigApi qiniuConfigApi;
    private final SystemQiniuContentApi qiniuContentApi;

    @ApiOperation("查询七牛云存储配置")
    @PostMapping(value = "/describeQiniuConfig")
    public ResponseEntity<SystemQiniuConfigTb> describeQiniuConfig() {
        return new ResponseEntity<>(qiniuConfigApi.describeQiniuConfig(), HttpStatus.OK);
    }

    @ApiOperation("配置七牛云存储")
    @PostMapping(value = "/modifyQiniuConfig")
    public ResponseEntity<Object> modifyQiniuConfig(@Validated @RequestBody SystemQiniuConfigTb qiniuConfig) {
        qiNiuConfigService.saveQiniuConfig(qiniuConfig);
        qiNiuConfigService.modifyQiniuConfigType(qiniuConfig.getType());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    public void exportQiNiu(HttpServletResponse response, QuerySystemQiniuArgs criteria) throws IOException {
        qiniuContentService.downloadExcel(qiniuContentApi.describeQiniuContentList(criteria), response);
    }

    @ApiOperation("查询文件")
    @GetMapping
    public ResponseEntity<CsResultVo<List<SystemQiniuContentTb>>> queryQiNiu(QuerySystemQiniuArgs criteria) {
        Page<SystemQiniuContentTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(qiniuContentApi.describeQiniuContentPage(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("上传文件")
    @PostMapping
    public ResponseEntity<Object> uploadQiNiu(@RequestParam MultipartFile file) {
        SystemQiniuContentTb qiniuContent = qiniuContentService.uploadFile(file);
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", qiniuContent.getId());
        map.put("errno", 0);
        map.put("data", new String[]{qiniuContent.getUrl()});
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ApiOperation("同步七牛云数据")
    @PostMapping(value = "/synchronize")
    public ResponseEntity<Object> synchronizeQiNiu() {
        qiniuContentService.synchronize();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("下载文件")
    @GetMapping(value = "/createFilePreviewUrl/{id}")
    public ResponseEntity<Object> createFilePreviewUrl(@PathVariable Long id) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("url", qiniuContentService.createFilePreviewUrl(qiniuContentService.getById(id)));
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @ApiOperation("删除文件")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Object> deleteQiNiu(@PathVariable Long id) {
        qiniuContentService.removeFileById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("删除多张图片")
    @DeleteMapping
    public ResponseEntity<Object> deleteAllQiNiu(@RequestBody Long[] ids) {
        qiniuContentService.removeFileByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
