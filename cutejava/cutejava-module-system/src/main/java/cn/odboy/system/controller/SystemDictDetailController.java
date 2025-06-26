package cn.odboy.system.controller;

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsResultVo;
import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.model.CreateSystemDictDetailArgs;
import cn.odboy.system.dal.model.QuerySystemDictDetailArgs;
import cn.odboy.system.service.SystemDictDetailService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：字典详情管理")
@RequestMapping("/api/dictDetail")
public class SystemDictDetailController {
    private final SystemDictDetailService systemDictDetailService;

    @ApiOperation("查询字典详情")
    @PostMapping
    public ResponseEntity<CsResultVo<List<SystemDictDetailTb>>> queryDictDetailByCrud(@Validated @RequestBody CsPageArgs<QuerySystemDictDetailArgs> args) {
        return queryDictDetailByArgs(args);
    }

    @ApiOperation("查询字典详情")
    @PostMapping(value = "/queryDictDetailByArgs")
    public ResponseEntity<CsResultVo<List<SystemDictDetailTb>>> queryDictDetailByArgs(@Validated @RequestBody CsPageArgs<QuerySystemDictDetailArgs> args) {
        QuerySystemDictDetailArgs criteria = args.getArgs();
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemDictDetailService.queryDictDetailByArgs(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/getDictDetailMaps")
    public ResponseEntity<Map<String, List<SystemDictDetailTb>>> getDictDetailMaps(@RequestParam String dictName) {
        String[] names = dictName.split("[,，]");
        Map<String, List<SystemDictDetailTb>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, systemDictDetailService.queryDictDetailByName(name));
        }
        return new ResponseEntity<>(dictMap, HttpStatus.OK);
    }

    @ApiOperation("新增字典详情")
    @PostMapping(value = "/saveDictDetail")
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Void> saveDictDetail(@Validated @RequestBody CreateSystemDictDetailArgs resources) {
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
