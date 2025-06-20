package cn.odboy.core.controller.system;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.base.CsSelectOptionItemVo;
import cn.odboy.core.service.system.*;
import cn.odboy.core.service.system.ookkoko.SystemDataService;
import cn.odboy.core.constant.system.SystemCaptchaBizEnum;
import cn.odboy.core.dal.dataobject.system.SystemDeptTb;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.model.system.UpdateSystemUserPasswordArgs;
import cn.odboy.core.framework.permission.core.SecurityHelper;
import cn.odboy.core.framework.properties.AppProperties;
import cn.odboy.core.dal.model.system.QuerySystemUserArgs;
import cn.odboy.core.service.system.ookkoko.SystemDeptService;
import cn.odboy.exception.BadRequestException;
import cn.odboy.util.PageUtil;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class SystemUserController {
    private final PasswordEncoder passwordEncoder;
    private final SystemUserService systemUserService;
    private final SystemDataService systemDataService;
    private final SystemDeptService systemDeptService;
    private final SystemRoleApi systemRoleApi;
    private final SystemUserApi systemUserApi;
    private final SystemCaptchaService verificationCodeService;
    private final AppProperties properties;

    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void downloadUserExcel(HttpServletResponse response, QuerySystemUserArgs criteria) throws IOException {
        systemUserService.downloadUserExcel(systemUserApi.describeUserList(criteria), response);
    }

    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<CsResultVo<List<SystemUserTb>>> queryUser(QuerySystemUserArgs criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            criteria.getDeptIds().add(criteria.getDeptId());
            // 先查找是否存在子节点
            List<SystemDeptTb> data = systemDeptService.describeDeptListByPid(criteria.getDeptId());
            // 然后把子节点的ID都加入到集合中
            criteria.getDeptIds().addAll(systemDeptService.describeChildDeptIdListByDeptIds(data));
        }
        // 数据权限
        List<Long> dataScopes = systemDataService.describeDeptIdListByUserIdWithDeptId(systemUserApi.describeUserByUsername(SecurityHelper.getCurrentUsername()));
        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(criteria.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)) {
            // 取交集
            criteria.getDeptIds().retainAll(dataScopes);
            if (!CollectionUtil.isEmpty(criteria.getDeptIds())) {
                return new ResponseEntity<>(systemUserApi.describeUserPage(criteria, page), HttpStatus.OK);
            }
        } else {
            // 否则取并集
            criteria.getDeptIds().addAll(dataScopes);
            return new ResponseEntity<>(systemUserApi.describeUserPage(criteria, page), HttpStatus.OK);
        }
        return new ResponseEntity<>(PageUtil.emptyData(), HttpStatus.OK);
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
        if (!resources.getId().equals(SecurityHelper.getCurrentUserId())) {
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
            Integer currentLevel = Collections.min(systemRoleApi.describeRoleListByUsersId(SecurityHelper.getCurrentUserId()).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(systemRoleApi.describeRoleListByUsersId(id).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + systemUserApi.describeUserById(id).getUsername());
            }
        }
        systemUserService.removeUserByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/modifyUserPasswordByUsername")
    public ResponseEntity<Object> modifyUserPasswordByUsername(@RequestBody UpdateSystemUserPasswordArgs passVo) throws Exception {
        String oldPass = RsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), passVo.getOldPass());
        String newPass = RsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), passVo.getNewPass());
        SystemUserTb user = systemUserApi.describeUserByUsername(SecurityHelper.getCurrentUsername());
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
        SystemUserTb user = systemUserApi.describeUserByUsername(SecurityHelper.getCurrentUsername());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.checkCodeAvailable(SystemCaptchaBizEnum.EMAIL_RESET_EMAIL_CODE.getBizCode(), resources.getEmail(), code);
        systemUserService.modifyUserEmailByUsername(user.getUsername(), resources.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources /
     */
    private void checkLevel(SystemUserTb resources) {
        Integer currentLevel = Collections.min(systemRoleApi.describeRoleListByUsersId(SecurityHelper.getCurrentUserId()).stream().map(SystemRoleTb::getLevel).collect(Collectors.toList()));
        Integer optLevel = systemRoleApi.describeDeptLevelByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

    @ApiOperation("查询用户基础数据")
    @PostMapping(value = "/describeUserMetadataOptions")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<List<CsSelectOptionItemVo>> queryUserMetadataOptions(@Validated @RequestBody QuerySystemUserArgs criteria) {
        int maxPageSize = 50;
        return new ResponseEntity<>(systemUserService.page(new Page<>(criteria.getPage(), maxPageSize), new LambdaQueryWrapper<SystemUserTb>()
                .and(c -> {
                    c.eq(SystemUserTb::getPhone, criteria.getBlurry());
                    c.or();
                    c.eq(SystemUserTb::getEmail, criteria.getBlurry());
                    c.or();
                    c.like(SystemUserTb::getUsername, criteria.getBlurry());
                    c.or();
                    c.like(SystemUserTb::getNickName, criteria.getBlurry());
                })
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
