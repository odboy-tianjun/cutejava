package cn.odboy.system.controller;

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResult;
import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.model.SystemCreateDictDetailArgs;
import cn.odboy.system.dal.model.SystemQueryDictDetailArgs;
import cn.odboy.system.service.SystemDictDetailService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "系统：字典详情管理")
@RequestMapping("/api/dictDetail")
public class SystemDictDetailController {
    @Autowired
    private SystemDictDetailService systemDictDetailService;

    @ApiOperation("查询字典详情")
    @PostMapping
    public ResponseEntity<CsPageResult<SystemDictDetailTb>> queryDictDetailByCrud(@Validated @RequestBody CsPageArgs<SystemQueryDictDetailArgs> args) {
        return queryDictDetailByArgs(args);
    }

    @ApiOperation("查询字典详情")
    @PostMapping(value = "/queryDictDetailByArgs")
    public ResponseEntity<CsPageResult<SystemDictDetailTb>> queryDictDetailByArgs(@Validated @RequestBody CsPageArgs<SystemQueryDictDetailArgs> args) {
        SystemQueryDictDetailArgs criteria = args.getArgs();
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemDictDetailService.queryDictDetailByArgs(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/getDictDetailMaps")
    public ResponseEntity<Map<String, List<SystemDictDetailTb>>> getDictDetailMaps(@RequestParam String dictName) {
        String[] names = dictName.split("[,, ]");
        Map<String, List<SystemDictDetailTb>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, systemDictDetailService.queryDictDetailByName(name));
        }
        return new ResponseEntity<>(dictMap, HttpStatus.OK);
    }

    @ApiOperation("新增字典详情")
    @PostMapping(value = "/saveDictDetail")
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Void> saveDictDetail(@Validated @RequestBody SystemCreateDictDetailArgs resources) {
        systemDictDetailService.saveDictDetail(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改字典详情")
    @PostMapping(value = "/modifyDictDetailById")
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Void> modifyDictDetailById(@Validated(SystemDictDetailTb.Update.class) @RequestBody SystemDictDetailTb resources) {
        systemDictDetailService.modifyDictDetailById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除字典详情")
    @PostMapping(value = "/removeDictDetailById")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Void> removeDictDetailById(@RequestBody SystemDictDetailTb args) {
        systemDictDetailService.removeDictDetailById(args.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
