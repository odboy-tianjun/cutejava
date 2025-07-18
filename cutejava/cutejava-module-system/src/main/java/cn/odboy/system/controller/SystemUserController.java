package cn.odboy.system.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsResultVo;
import cn.odboy.base.CsSelectOptionItemVo;
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
import cn.odboy.util.RsaEncryptUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class SystemUserController {
    private final PasswordEncoder passwordEncoder;
    private final SystemUserService systemUserService;
    private final SystemDataService systemDataService;
    private final SystemDeptService systemDeptService;
    private final SystemRoleService systemRoleService;
    private final SystemEmailService systemEmailService;
    private final AppProperties properties;

    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void exportUserExcel(HttpServletResponse response, SystemQueryUserArgs criteria) throws IOException {
        systemUserService.exportUserExcel(systemUserService.queryUserByArgs(criteria), response);
    }

    @ApiOperation("查询用户")
    @PostMapping
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<CsResultVo<List<SystemUserTb>>> queryUserByArgs(@Validated @RequestBody CsPageArgs<SystemQueryUserArgs> args) {
        Page<Object> page = new Page<>(args.getPage(), args.getSize());
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
                return new ResponseEntity<>(systemUserService.queryUserByArgs(criteria, page), HttpStatus.OK);
            }
        } else {
            // 否则取并集
            criteria.getDeptIds().addAll(dataScopes);
            return new ResponseEntity<>(systemUserService.queryUserByArgs(criteria, page), HttpStatus.OK);
        }
        return new ResponseEntity<>(CsPageUtil.emptyData(), HttpStatus.OK);
    }

    @ApiOperation("新增用户")
    @PostMapping(value = "/saveUser")
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<Object> saveUser(@Validated @RequestBody SystemUserTb resources) {
        checkLevel(resources);
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        systemUserService.saveUser(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改用户")
    @PostMapping(value = "/modifyUserById")
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<Object> modifyUserById(@Validated(SystemUserTb.Update.class) @RequestBody SystemUserTb resources) throws Exception {
        checkLevel(resources);
        systemUserService.modifyUserById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("修改用户：个人中心")
    @PostMapping(value = "modifyUserCenterInfoById")
    public ResponseEntity<Object> modifyUserCenterInfoById(@Validated(SystemUserTb.Update.class) @RequestBody SystemUserTb resources) {
        if (!resources.getId().equals(CsSecurityHelper.getCurrentUserId())) {
            throw new BadRequestException("不能修改他人资料");
        }
        systemUserService.modifyUserCenterInfoById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除用户")
    @PostMapping(value = "/removeUserByIds")
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<Object> removeUserByIds(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(systemRoleService.queryRoleByUsersId(CsSecurityHelper.getCurrentUserId()).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(systemRoleService.queryRoleByUsersId(id).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足, 不能删除：" + systemUserService.getUserById(id).getUsername());
            }
        }
        systemUserService.removeUserByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/modifyUserPasswordByUsername")
    public ResponseEntity<Object> modifyUserPasswordByUsername(@RequestBody SystemUpdateUserPasswordArgs passVo) throws Exception {
        String oldPass = RsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), passVo.getOldPass());
        String newPass = RsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), passVo.getNewPass());
        SystemUserTb user = systemUserService.getUserByUsername(CsSecurityHelper.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        systemUserService.modifyUserPasswordByUsername(user.getUsername(), passwordEncoder.encode(newPass));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("重置密码")
    @PostMapping(value = "/resetUserPasswordByIds")
    public ResponseEntity<Object> resetUserPasswordByIds(@RequestBody Set<Long> ids) {
        String defaultPwd = passwordEncoder.encode("123456");
        systemUserService.resetUserPasswordByIds(ids, defaultPwd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改头像")
    @PostMapping(value = "/modifyUserAvatar")
    public ResponseEntity<Object> modifyUserAvatar(@RequestParam MultipartFile avatar) {
        return new ResponseEntity<>(systemUserService.modifyUserAvatar(avatar), HttpStatus.OK);
    }

    @ApiOperation("修改邮箱")
    @PostMapping(value = "/modifyUserEmailByUsername/{code}")
    public ResponseEntity<Object> modifyUserEmailByUsername(@PathVariable String code, @RequestBody SystemUserTb resources) throws Exception {
        String password = RsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), resources.getPassword());
        SystemUserTb user = systemUserService.getUserByUsername(CsSecurityHelper.getCurrentUsername());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        systemEmailService.checkEmailCaptcha(SystemCaptchaBizEnum.EMAIL_RESET_EMAIL_CODE, resources.getEmail(), code);
        systemUserService.modifyUserEmailByUsername(user.getUsername(), resources.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources /
     */
    private void checkLevel(SystemUserTb resources) {
        Integer currentLevel = Collections.min(systemRoleService.queryRoleByUsersId(CsSecurityHelper.getCurrentUserId()).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
        Integer optLevel = systemRoleService.getDeptLevelByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

    @ApiOperation("查询用户基础数据")
    @PostMapping(value = "/queryUserMetadataOptions")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<List<CsSelectOptionItemVo>> queryUserMetadataOptions(@Validated @RequestBody CsPageArgs<SystemQueryUserArgs> args) {
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
        return new ResponseEntity<>(systemUserService.queryUserByBlurry(wrapper, new Page<>(criteria.getPage(), maxPageSize)
        ).getRecords().stream().map(m -> {
            Map<String, Object> ext = new HashMap<>(1);
            ext.put("id", m.getId());
            ext.put("deptId", m.getDeptId());
            ext.put("email", m.getEmail());
            ext.put("phone", m.getPhone());
            return CsSelectOptionItemVo.builder()
                    .label(m.getNickName())
                    .value(m.getUsername())
                    .ext(ext)
                    .build();
        }).collect(Collectors.toList()), HttpStatus.OK);
    }
}
