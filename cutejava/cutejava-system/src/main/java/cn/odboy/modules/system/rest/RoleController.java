package cn.odboy.modules.system.rest;

import cn.hutool.core.lang.Dict;
import cn.odboy.base.PageArgs;
import cn.odboy.exception.BadRequestException;
import cn.odboy.modules.system.domain.Role;
import cn.odboy.modules.system.domain.dto.RoleQueryCriteria;
import cn.odboy.modules.system.service.RoleService;
import cn.odboy.base.PageResult;
import cn.odboy.util.SecurityUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Api(tags = "系统：角色管理")
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    private static final String ENTITY_NAME = "role";

    @ApiOperation("获取单个role")
    @PostMapping(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<Role> findRoleById(@PathVariable Long id) {
        return new ResponseEntity<>(roleService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("导出角色数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void exportRole(HttpServletResponse response, RoleQueryCriteria criteria) throws IOException {
        roleService.download(roleService.queryAll(criteria), response);
    }

    @ApiOperation("返回全部的角色")
    @PostMapping(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResponseEntity<List<Role>> queryAllRole() {
        return new ResponseEntity<>(roleService.queryAll(), HttpStatus.OK);
    }

    @ApiOperation("查询角色")
    @PostMapping(value = "/query")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<PageResult<Role>> queryRole(@Validated @RequestBody PageArgs<RoleQueryCriteria> criteria) {
        Page<Role> page = new Page<>(criteria.getPage(), criteria.getPageSize());
        return new ResponseEntity<>(roleService.queryAll(criteria.getArgs(), page), HttpStatus.OK);
    }

    @ApiOperation("获取用户级别")
    @PostMapping(value = "/level")
    public ResponseEntity<Object> getRoleLevel() {
        return new ResponseEntity<>(Dict.create().set("level", getLevels(null)), HttpStatus.OK);
    }

    @ApiOperation("新增角色")
    @PostMapping(value = "/save")
    @PreAuthorize("@el.check('roles:add')")
    public ResponseEntity<Object> createRole(@Validated @RequestBody Role resources) {
        if (resources.getId() != null) {
            throw new BadRequestException("A new " + ENTITY_NAME + " cannot already have an ID");
        }
        getLevels(resources.getLevel());
        roleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改角色")
    @PostMapping(value = "/modify")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> updateRole(@Validated(Role.Update.class) @RequestBody Role resources) {
        getLevels(resources.getLevel());
        roleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("修改角色菜单")
    @PostMapping(value = "/menu")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> updateRoleMenu(@RequestBody Role resources) {
        Role role = roleService.getById(resources.getId());
        getLevels(role.getLevel());
        roleService.updateMenu(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除角色")
    @PostMapping(value = "/remove")
    @PreAuthorize("@el.check('roles:del')")
    public ResponseEntity<Object> deleteRole(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Role role = roleService.getById(id);
            getLevels(role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verification(ids);
        roleService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 获取用户的角色级别
     *
     * @return /
     */
    private int getLevels(Integer level) {
        List<Integer> levels = roleService.findByUsersId(SecurityUtil.getCurrentUserId()).stream().map(Role::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if (level != null) {
            if (level < min) {
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
