package cn.odboy.system.controller;

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsResultVo;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import cn.odboy.system.dal.model.CreateSystemDictArgs;
import cn.odboy.system.dal.model.QuerySystemDictArgs;
import cn.odboy.system.service.SystemDictService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Api(tags = "系统：字典管理")
@RequestMapping("/api/dict")
public class SystemDictController {
    private final SystemDictService systemDictService;

    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void exportDict(HttpServletResponse response, QuerySystemDictArgs criteria) throws IOException {
        systemDictService.exportDictExcel(systemDictService.queryDictByArgs(criteria), response);
    }

    @ApiOperation("查询字典")
    @PostMapping(value = "/queryAllDict")
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<List<SystemDictTb>> queryAllDict() {
        return new ResponseEntity<>(systemDictService.queryDictByArgs(new QuerySystemDictArgs()), HttpStatus.OK);
    }

    @ApiOperation("查询字典")
    @PostMapping
    @PreAuthorize("@el.check('dict:list')")
    public ResponseEntity<CsResultVo<List<SystemDictTb>>> queryDictByArgs(@Validated @RequestBody CsPageArgs<QuerySystemDictArgs> args) {
        QuerySystemDictArgs criteria = args.getArgs();
        Page<SystemDictTb> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemDictService.queryDictByArgs(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("新增字典")
    @PostMapping(value = "/saveDict")
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Void> saveDict(@Validated @RequestBody CreateSystemDictArgs resources) {
        systemDictService.saveDict(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改字典")
    @PostMapping(value = "/modifyDictById")
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Void> modifyDictById(@Validated(SystemDictTb.Update.class) @RequestBody SystemDictTb resources) {
        systemDictService.modifyDictById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除字典")
    @PostMapping(value = "/removeDictByIds")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Void> removeDictByIds(@RequestBody Set<Long> ids) {
        systemDictService.removeDictByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
