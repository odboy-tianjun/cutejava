/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.odboy.system.controller;

import cn.hutool.core.lang.Dict;
import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.SystemCreateRoleArgs;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import cn.odboy.system.framework.permission.core.CsSecurityHelper;
import cn.odboy.system.service.SystemRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(tags = "系统：角色管理")
@RequestMapping("/api/role")
public class SystemRoleController {
    @Autowired
    private SystemRoleService systemRoleService;

    @ApiOperation("获取单个role")
    @PostMapping(value = "/queryRoleById")
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<SystemRoleTb> queryRoleById(@RequestBody SystemRoleTb args) {
        return ResponseEntity.ok(systemRoleService.getRoleById(args.getId()));
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
        return ResponseEntity.ok(systemRoleService.queryAllRole());
    }

    @ApiOperation("查询角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:list')")
    public ResponseEntity<CsPageResult<SystemRoleTb>> queryRoleByArgs(@Validated @RequestBody CsPageArgs<SystemQueryRoleArgs> args) {
        SystemQueryRoleArgs criteria = args.getArgs();
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        return ResponseEntity.ok(systemRoleService.queryRoleByArgs(criteria, page));
    }

    @ApiOperation("获取用户级别")
    @PostMapping(value = "/queryRoleLevel")
    public ResponseEntity<Dict> queryRoleLevel() {
        return ResponseEntity.ok(Dict.create().set("level", checkRoleLevels(null)));
    }

    @ApiOperation("新增角色")
    @PostMapping(value = "/saveRole")
    @PreAuthorize("@el.check('roles:add')")
    public ResponseEntity<Void> saveRole(@Validated @RequestBody SystemCreateRoleArgs args) {
        checkRoleLevels(args.getLevel());
        systemRoleService.saveRole(args);
        return ResponseEntity.ok(null);
    }

    @ApiOperation("修改角色")
    @PostMapping(value = "/modifyRoleById")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Void> modifyRoleById(@Validated(SystemRoleTb.Update.class) @RequestBody SystemRoleTb args) {
        checkRoleLevels(args.getLevel());
        systemRoleService.modifyRoleById(args);
        return ResponseEntity.ok(null);
    }

    @ApiOperation("修改角色菜单")
    @PostMapping(value = "/modifyBindMenuById")
    @PreAuthorize("@el.check('roles:edit')")
    public ResponseEntity<Void> modifyBindMenuById(@RequestBody SystemRoleTb args) {
        SystemRoleTb role = systemRoleService.getRoleById(args.getId());
        checkRoleLevels(role.getLevel());
        systemRoleService.modifyBindMenuById(args);
        return ResponseEntity.ok(null);
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
        return ResponseEntity.ok(null);
    }

    /**
     * 检查用户的角色级别
     *
     * @return /
     */
    private int checkRoleLevels(Integer level) {
        List<Integer> levels =
                systemRoleService.queryRoleByUsersId(CsSecurityHelper.getCurrentUserId())
                        .stream()
                        .map(SystemRoleTb::getLevel)
                        .collect(Collectors.toList());
        int min = Collections.min(levels);
        if (level != null) {
            if (level < min) {
                throw new BadRequestException("权限不足, 你的角色级别：" + min + ", 低于操作的角色级别：" + level);
            }
        }
        return min;
    }
}
