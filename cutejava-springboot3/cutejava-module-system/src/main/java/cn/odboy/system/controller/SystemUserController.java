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

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResult;
import cn.odboy.base.CsSelectOptionVo;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.system.constant.SystemCaptchaBizEnum;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemQueryUserArgs;
import cn.odboy.system.dal.model.SystemUpdateUserPasswordArgs;
import cn.odboy.system.framework.permission.core.CsSecurityHelper;
import cn.odboy.system.service.*;
import cn.odboy.util.CsPageUtil;
import cn.odboy.util.CsRsaEncryptUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "系统：用户管理")
@RestController
@RequestMapping("/api/user")
public class SystemUserController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private SystemDataService systemDataService;
    @Autowired
    private SystemDeptService systemDeptService;
    @Autowired
    private SystemRoleService systemRoleService;
    @Autowired
    private SystemEmailService systemEmailService;
    @Autowired
    private AppProperties properties;

    @Operation(summary = "导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void exportUserExcel(HttpServletResponse response, SystemQueryUserArgs criteria) throws IOException {
        systemUserService.exportUserExcel(systemUserService.queryUserByArgs(criteria), response);
    }

    @Operation(summary = "查询用户")
    @PostMapping
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<CsPageResult<SystemUserTb>> queryUserByArgs(@Validated @RequestBody CsPageArgs<SystemQueryUserArgs> args) {
        Page<SystemUserTb> page = new Page<>(args.getPage(), args.getSize());
        SystemQueryUserArgs criteria = args.getArgs();
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            criteria.getDeptIds().add(criteria.getDeptId());
            // 先查找是否存在子节点
            List<SystemDeptTb> data = systemDeptService.queryDeptByPid(criteria.getDeptId());
            // 然后把子节点的ID都加入到集合中
            criteria.getDeptIds().addAll(systemDeptService.queryChildDeptIdListByDeptIds(data));
        }
        // 数据权限
        List<Long> dataScopes = systemDataService.queryDeptIdListByArgs(systemUserService.getUserByUsername(CsSecurityHelper.getCurrentUsername()));
        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(criteria.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)) {
            // 取交集
            criteria.getDeptIds().retainAll(dataScopes);
            if (!CollectionUtil.isEmpty(criteria.getDeptIds())) {
                return ResponseEntity.ok(systemUserService.queryUserByArgs(criteria, page));
            }
        } else {
            // 否则取并集
            criteria.getDeptIds().addAll(dataScopes);
            return ResponseEntity.ok(systemUserService.queryUserByArgs(criteria, page));
        }
        return ResponseEntity.ok(CsPageUtil.emptyData());
    }

    @Operation(summary = "新增用户")
    @PostMapping(value = "/saveUser")
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<Object> saveUser(@Validated @RequestBody SystemUserTb args) {
        checkLevel(args);
        // 默认密码 123456
        args.setPassword(passwordEncoder.encode("123456"));
        systemUserService.saveUser(args);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "修改用户")
    @PostMapping(value = "/modifyUserById")
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<Object> modifyUserById(@Validated(SystemUserTb.Update.class) @RequestBody SystemUserTb args) {
        checkLevel(args);
        systemUserService.modifyUserById(args);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "修改用户：个人中心")
    @PostMapping(value = "modifyUserCenterInfoById")
    public ResponseEntity<Object> modifyUserCenterInfoById(@Validated(SystemUserTb.Update.class) @RequestBody SystemUserTb args) {
        if (!args.getId().equals(CsSecurityHelper.getCurrentUserId())) {
            throw new BadRequestException("不能修改他人资料");
        }
        systemUserService.modifyUserCenterInfoById(args);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "删除用户")
    @PostMapping(value = "/removeUserByIds")
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<Object> removeUserByIds(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(
                systemRoleService.queryRoleByUsersId(CsSecurityHelper.getCurrentUserId()).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(systemRoleService.queryRoleByUsersId(id).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足, 不能删除：" + systemUserService.getUserById(id).getUsername());
            }
        }
        systemUserService.removeUserByIds(ids);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "修改密码")
    @PostMapping(value = "/modifyUserPasswordByUsername")
    public ResponseEntity<Object> modifyUserPasswordByUsername(@RequestBody SystemUpdateUserPasswordArgs passVo) throws Exception {
        String oldPass = CsRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), passVo.getOldPass());
        String newPass = CsRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), passVo.getNewPass());
        SystemUserTb user = systemUserService.getUserByUsername(CsSecurityHelper.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        systemUserService.modifyUserPasswordByUsername(user.getUsername(), passwordEncoder.encode(newPass));
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "重置密码")
    @PostMapping(value = "/resetUserPasswordByIds")
    public ResponseEntity<Object> resetUserPasswordByIds(@RequestBody Set<Long> ids) {
        String defaultPwd = passwordEncoder.encode("123456");
        systemUserService.resetUserPasswordByIds(ids, defaultPwd);
        return ResponseEntity.ok(null);
    }

    @Operation(summary = "修改头像")
    @PostMapping(value = "/modifyUserAvatar")
    public ResponseEntity<Object> modifyUserAvatar(@RequestParam MultipartFile avatar) {
        return ResponseEntity.ok(systemUserService.modifyUserAvatar(avatar));
    }

    @Operation(summary = "修改邮箱")
    @PostMapping(value = "/modifyUserEmailByUsername/{code}")
    public ResponseEntity<Object> modifyUserEmailByUsername(@PathVariable String code, @RequestBody SystemUserTb args) throws Exception {
        String password = CsRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), args.getPassword());
        SystemUserTb user = systemUserService.getUserByUsername(CsSecurityHelper.getCurrentUsername());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        systemEmailService.checkEmailCaptcha(SystemCaptchaBizEnum.EMAIL_RESET_EMAIL_CODE, args.getEmail(), code);
        systemUserService.modifyUserEmailByUsername(user.getUsername(), args.getEmail());
        return ResponseEntity.ok(null);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param args /
     */
    private void checkLevel(SystemUserTb args) {
        Integer currentLevel = Collections.min(
            systemRoleService.queryRoleByUsersId(CsSecurityHelper.getCurrentUserId()).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
        Integer optLevel = systemRoleService.getDeptLevelByRoles(args.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

    @Operation(summary = "查询用户基础数据")
    @PostMapping(value = "/queryUserMetadataOptions")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<List<CsSelectOptionVo>> queryUserMetadataOptions(@Validated @RequestBody CsPageArgs<SystemQueryUserArgs> args) {
        int maxPageSize = 50;
        SystemQueryUserArgs criteria = args.getArgs();
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.and(c -> {
            c.eq(SystemUserTb::getPhone, criteria.getBlurry());
            c.or();
            c.eq(SystemUserTb::getEmail, criteria.getBlurry());
            c.or();
            c.like(SystemUserTb::getUsername, criteria.getBlurry());
            c.or();
            c.like(SystemUserTb::getNickName, criteria.getBlurry());
        });
        List<CsSelectOptionVo> collect =
            systemUserService.queryUserByBlurry(wrapper, new Page<>(criteria.getPage(), maxPageSize)).getRecords().stream().map(m -> {
                Map<String, Object> ext = new HashMap<>(1);
                ext.put("id", m.getId());
                ext.put("deptId", m.getDeptId());
                ext.put("email", m.getEmail());
                ext.put("phone", m.getPhone());
                return CsSelectOptionVo.builder().label(m.getNickName()).value(String.valueOf(m.getId())).ext(ext).build();
            }).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }
}
