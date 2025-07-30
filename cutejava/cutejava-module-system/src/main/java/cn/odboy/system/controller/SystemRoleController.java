package cn.odboy.system.controller;

import cn.hutool.core.lang.Dict;
import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResultVo;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.SystemCreateRoleArgs;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import cn.odboy.system.framework.permission.core.CsSecurityHelper;
import cn.odboy.system.service.SystemRoleService;
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
@RequestMapping("/api/role")
public class SystemRoleController {
    private final SystemRoleService systemRoleService;

    @ApiOperation("获取单个role")
    @PostMapping(value = "/queryRoleById")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<SystemRoleTb> queryRoleById(@RequestBody SystemRoleTb args) {
        return new ResponseEntity<>(systemRoleService.getRoleById(args.getId()), HttpStatus.OK);
    }

    @ApiOperation("导出角色数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void exportRole(HttpServletResponse response, SystemQueryRoleArgs criteria) throws IOException {
        systemRoleService.exportRoleExcel(systemRoleService.queryRoleByArgs(criteria), response);
    }

    @ApiOperation("返回全部的角色")
    @PostMapping(value = "/queryRoleList")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResponseEntity<List<SystemRoleTb>> queryRoleList() {
        return new ResponseEntity<>(systemRoleService.queryAllRole(), HttpStatus.OK);
    }

    @ApiOperation("查询角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<CsPageResultVo<List<SystemRoleTb>>> queryRoleByArgs(@Validated @RequestBody CsPageArgs<SystemQueryRoleArgs> args) {
        SystemQueryRoleArgs criteria = args.getArgs();
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return new ResponseEntity<>(systemRoleService.queryRoleByArgs(criteria, page), HttpStatus.OK);
    }

    @ApiOperation("获取用户级别")
    @PostMapping(value = "/queryRoleLevel")
    public ResponseEntity<Dict> queryRoleLevel() {
        return new ResponseEntity<>(Dict.create().set("level", checkRoleLevels(null)), HttpStatus.OK);
    }

    @ApiOperation("新增角色")
    @PostMapping(value = "/saveRole")
    @PreAuthorize("@el.check('roles:add')")
    public ResponseEntity<Void> saveRole(@Validated @RequestBody SystemCreateRoleArgs resources) {
        checkRoleLevels(resources.getLevel());
        systemRoleService.saveRole(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改角色")
    @PostMapping(value = "/modifyRoleById")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Void> modifyRoleById(@Validated(SystemRoleTb.Update.class) @RequestBody SystemRoleTb resources) {
        checkRoleLevels(resources.getLevel());
        systemRoleService.modifyRoleById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("修改角色菜单")
    @PostMapping(value = "/modifyBindMenuById")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Void> modifyBindMenuById(@RequestBody SystemRoleTb resources) {
        SystemRoleTb role = systemRoleService.getRoleById(resources.getId());
        checkRoleLevels(role.getLevel());
        systemRoleService.modifyBindMenuById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除角色")
    @PostMapping(value = "/removeRoleByIds")
    @PreAuthorize("@el.check('roles:del')")
    public ResponseEntity<Void> removeRoleByIds(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            SystemRoleTb role = systemRoleService.getRoleById(id);
            checkRoleLevels(role.getLevel());
        }
        // 验证是否被用户关联
        systemRoleService.verifyBindRelationByIds(ids);
        systemRoleService.removeRoleByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 检查用户的角色级别
     *
     * @return /
     */
    private int checkRoleLevels(Integer level) {
        List<Integer> levels =
            systemRoleService.queryRoleByUsersId(CsSecurityHelper.getCurrentUserId()).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if (level != null) {
            if (level < min) {
                throw new BadRequestException("权限不足, 你的角色级别：" + min + ", 低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
