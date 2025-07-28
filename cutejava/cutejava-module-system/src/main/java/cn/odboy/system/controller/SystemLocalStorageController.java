package cn.odboy.system.controller;

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsResultVo;
import cn.odboy.constant.FileTypeEnum;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import cn.odboy.system.service.SystemLocalStorageService;
import cn.odboy.util.CsFileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "工具：本地存储管理")
@RequestMapping("/api/localStorage")
public class SystemLocalStorageController {
    private final SystemLocalStorageService localStorageService;

    @ApiOperation("查询文件")
    @PostMapping
    @PreAuthorize("@el.check('storage:list')")
    public ResponseEntity<CsResultVo<List<SystemLocalStorageTb>>> queryLocalStorage(@Validated @RequestBody CsPageArgs<SystemQueryStorageArgs> args) {
        SystemQueryStorageArgs criteria = args.getArgs();
        Page<SystemLocalStorageTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(localStorageService.queryLocalStorage(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('storage:list')")
    public void exportFile(HttpServletResponse response, SystemQueryStorageArgs criteria) throws IOException {
        localStorageService.exportLocalStorageExcel(localStorageService.queryLocalStorage(criteria), response);
    }

    @ApiOperation("上传文件")
    @PostMapping(value = "/uploadFile")
    @PreAuthorize("@el.check('storage:add')")
    public ResponseEntity<Void> uploadFile(@RequestParam String name, @RequestParam("file") MultipartFile file) {
        localStorageService.uploadFile(name, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("上传图片")
    @PostMapping("/uploadPicture")
    public ResponseEntity<SystemLocalStorageTb> uploadPicture(@RequestParam MultipartFile file) {
        // 判断文件是否为图片
        String suffix = CsFileUtil.getSuffix(file.getOriginalFilename());
        if (!FileTypeEnum.IMAGE.getCode().equals(CsFileUtil.getFileType(suffix))) {
            throw new BadRequestException("只能上传图片");
        }
        SystemLocalStorageTb localStorage = localStorageService.uploadFile(null, file);
        return new ResponseEntity<>(localStorage, HttpStatus.OK);
    }

    @ApiOperation("修改文件")
    @PostMapping(value = "/modifyLocalStorageById")
    @PreAuthorize("@el.check('storage:edit')")
    public ResponseEntity<Void> modifyLocalStorageById(@Validated @RequestBody SystemLocalStorageTb resources) {
        localStorageService.modifyLocalStorageById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("多选删除")
    @PostMapping(value = "/removeFileByIds")
    public ResponseEntity<Void> deleteFileByIds(@RequestBody Long[] ids) {
        localStorageService.removeFileByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
