package cn.odboy.core.controller.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemDictDetailTb;
import cn.odboy.core.service.system.SystemDictDetailService;
import cn.odboy.core.dal.model.system.CreateSystemDictDetailArgs;
import cn.odboy.core.dal.model.system.QuerySystemDictDetailArgs;
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
    @GetMapping
    public ResponseEntity<CsResultVo<List<SystemDictDetailTb>>> queryDictDetailListByArgs(QuerySystemDictDetailArgs criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemDictDetailService.queryDictDetailListByArgs(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/getDictDetailMaps")
    public ResponseEntity<Object> getDictDetailMaps(@RequestParam String dictName) {
        String[] names = dictName.split("[,，]");
        Map<String, List<SystemDictDetailTb>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, systemDictDetailService.queryDictDetailListByName(name));
        }
        return new ResponseEntity<>(dictMap, HttpStatus.OK);
    }

    @ApiOperation("新增字典详情")
    @PostMapping(value = "/saveDictDetail")
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Object> saveDictDetail(@Validated @RequestBody CreateSystemDictDetailArgs resources) {
        systemDictDetailService.saveDictDetail(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改字典详情")
    @PostMapping(value = "/modifyDictDetailById")
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Object> modifyDictDetailById(@Validated(SystemDictDetailTb.Update.class) @RequestBody SystemDictDetailTb resources) {
        systemDictDetailService.modifyDictDetailById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除字典详情")
    @PostMapping(value = "/removeDictDetailById")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Object> removeDictDetailById(@RequestBody SystemDictDetailTb args) {
        systemDictDetailService.removeDictDetailById(args.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
