package cn.odboy.application.system.rest;

import cn.hutool.core.lang.Dict;
import cn.odboy.application.system.service.RoleService;
import cn.odboy.base.PageResult;
import cn.odboy.context.SecurityHelper;
import cn.odboy.exception.BadRequestException;
import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.request.CreateRoleRequest;
import cn.odboy.model.system.request.QueryRoleRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @ApiOperation("获取单个role")
    @PostMapping(value = "/describeRoleById")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<Role> describeRoleById(@RequestBody Role args) {
        return new ResponseEntity<>(roleService.describeRoleById(args.getId()), HttpStatus.OK);
    }

    @ApiOperation("导出角色数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void exportRole(HttpServletResponse response, QueryRoleRequest criteria) throws IOException {
        roleService.downloadRoleExcel(roleService.describeRoleList(criteria), response);
    }

    @ApiOperation("返回全部的角色")
    @PostMapping(value = "/describeRoleList")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResponseEntity<List<Role>> describeRoleList() {
        return new ResponseEntity<>(roleService.describeRoleList(), HttpStatus.OK);
    }

    @ApiOperation("查询角色")
    @GetMapping
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<PageResult<Role>> describeRolePage(QueryRoleRequest criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(roleService.describeRolePage(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("获取用户级别")
    @PostMapping(value = "/describeRoleLevel")
    public ResponseEntity<Object> describeRoleLevel() {
        return new ResponseEntity<>(Dict.create().set("level", checkRoleLevels(null)), HttpStatus.OK);
    }

    @ApiOperation("新增角色")
    @PostMapping(value = "/saveRole")
    @PreAuthorize("@el.check('roles:add')")
    public ResponseEntity<Object> saveRole(@Validated @RequestBody CreateRoleRequest resources) {
        checkRoleLevels(resources.getLevel());
        roleService.saveRole(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改角色")
    @PostMapping(value = "/modifyRoleById")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> modifyRoleById(@Validated(Role.Update.class) @RequestBody Role resources) {
        checkRoleLevels(resources.getLevel());
        roleService.modifyRoleById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("修改角色菜单")
    @PostMapping(value = "/modifyBindMenuById")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Object> modifyBindMenuById(@RequestBody Role resources) {
        Role role = roleService.getById(resources.getId());
        checkRoleLevels(role.getLevel());
        roleService.modifyBindMenuById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除角色")
    @PostMapping(value = "/removeRoleByIds")
    @PreAuthorize("@el.check('roles:del')")
    public ResponseEntity<Object> removeRoleByIds(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Role role = roleService.getById(id);
            checkRoleLevels(role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verifyBindRelationByIds(ids);
        roleService.removeRoleByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 检查用户的角色级别
     *
     * @return /
     */
    private int checkRoleLevels(Integer level) {
        List<Integer> levels = roleService.describeRoleListByUsersId(SecurityHelper.getCurrentUserId()).stream().map(Role::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if (level != null) {
            if (level < min) {
                throw new BadRequestException("权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
