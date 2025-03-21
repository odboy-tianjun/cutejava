package cn.odboy.modules.system.rest;

import cn.odboy.base.PageArgs;
import cn.odboy.exception.BadRequestException;
import cn.odboy.modules.system.domain.DictDetail;
import cn.odboy.modules.system.domain.dto.DictDetailQueryCriteria;
import cn.odboy.modules.system.service.DictDetailService;
import cn.odboy.base.PageResult;
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
public class DictDetailController {

    private final DictDetailService dictDetailService;
    private static final String ENTITY_NAME = "dictDetail";

    @ApiOperation("查询字典详情")
    @PostMapping(value = "/query")
    public ResponseEntity<PageResult<DictDetail>> queryDictDetail(@Validated @RequestBody PageArgs<DictDetailQueryCriteria> criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getPageSize());
        return new ResponseEntity<>(dictDetailService.queryAll(criteria.getArgs(), page), HttpStatus.OK);
    }

    @ApiOperation("查询多个字典详情")
    @PostMapping(value = "/map")
    public ResponseEntity<Object> getDictDetailMaps(@Validated @RequestBody PageArgs<DictDetailQueryCriteria> criteria) {
        String[] names = criteria.getArgs().getDictName().split("[,，]");
        Map<String, List<DictDetail>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, dictDetailService.getDictByName(name));
        }
        return new ResponseEntity<>(dictMap, HttpStatus.OK);
    }

    @ApiOperation("新增字典详情")
    @PostMapping(value = "/save")
    @PreAuthorize("@el.check('dict:add')")
    public ResponseEntity<Object> createDictDetail(@Validated @RequestBody DictDetail resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        dictDetailService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改字典详情")
    @PostMapping(value = "/modify")
    @PreAuthorize("@el.check('dict:edit')")
    public ResponseEntity<Object> updateDictDetail(@Validated(DictDetail.Update.class) @RequestBody DictDetail resources) {
        dictDetailService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除字典详情")
    @PostMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public ResponseEntity<Object> deleteDictDetail(@PathVariable Long id) {
        dictDetailService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}