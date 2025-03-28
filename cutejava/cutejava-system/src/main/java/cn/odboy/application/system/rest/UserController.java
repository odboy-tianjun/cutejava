package cn.odboy.application.system.rest;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.application.system.service.DataService;
import cn.odboy.application.system.service.DeptService;
import cn.odboy.application.system.service.RoleService;
import cn.odboy.application.system.service.UserService;
import cn.odboy.application.tools.service.CaptchaService;
import cn.odboy.base.MyMetaOption;
import cn.odboy.base.PageResult;
import cn.odboy.constant.CodeEnum;
import cn.odboy.exception.BadRequestException;
import cn.odboy.model.system.domain.Dept;
import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.response.UserPassVo;
import cn.odboy.model.system.request.UserQueryCriteria;
import cn.odboy.properties.RsaProperties;
import cn.odboy.util.PageUtil;
import cn.odboy.util.RsaEncryptUtil;
import cn.odboy.util.SecurityUtil;
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
public class UserController {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final DataService dataService;
    private final DeptService deptService;
    private final RoleService roleService;
    private final CaptchaService verificationCodeService;

    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void exportUser(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        userService.downloadExcel(userService.selectUserByCriteria(criteria), response);
    }

    @ApiOperation("查询用户")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<PageResult<User>> queryUser(UserQueryCriteria criteria) {
        Page<Object> page = new Page<>(criteria.getPage(), criteria.getSize());
        if (!ObjectUtils.isEmpty(criteria.getDeptId())) {
            criteria.getDeptIds().add(criteria.getDeptId());
            // 先查找是否存在子节点
            List<Dept> data = deptService.selectDeptByPid(criteria.getDeptId());
            // 然后把子节点的ID都加入到集合中
            criteria.getDeptIds().addAll(deptService.selectChildDeptIdByDeptIds(data));
        }
        // 数据权限
        List<Long> dataScopes = dataService.selectDeptIdByUserIdWithDeptId(userService.getUserByUsername(SecurityUtil.getCurrentUsername()));
        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(criteria.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)) {
            // 取交集
            criteria.getDeptIds().retainAll(dataScopes);
            if (!CollectionUtil.isEmpty(criteria.getDeptIds())) {
                return new ResponseEntity<>(userService.queryUserPage(criteria, page), HttpStatus.OK);
            }
        } else {
            // 否则取并集
            criteria.getDeptIds().addAll(dataScopes);
            return new ResponseEntity<>(userService.queryUserPage(criteria, page), HttpStatus.OK);
        }
        return new ResponseEntity<>(PageUtil.noData(), HttpStatus.OK);
    }

    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<Object> createUser(@Validated @RequestBody User resources) {
        checkLevel(resources);
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        userService.saveUser(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改用户")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<Object> updateUser(@Validated(User.Update.class) @RequestBody User resources) throws Exception {
        checkLevel(resources);
        userService.updateUserById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("修改用户：个人中心")
    @PutMapping(value = "center")
    public ResponseEntity<Object> centerUser(@Validated(User.Update.class) @RequestBody User resources) {
        if (!resources.getId().equals(SecurityUtil.getCurrentUserId())) {
            throw new BadRequestException("不能修改他人资料");
        }
        userService.updateCenterInfoById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除用户")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<Object> deleteUser(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(roleService.selectRoleByUsersId(SecurityUtil.getCurrentUserId()).stream().map(Role::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(roleService.selectRoleByUsersId(id).stream().map(Role::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + userService.getUserById(id).getUsername());
            }
        }
        userService.deleteUserByIds(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResponseEntity<Object> updateUserPassword(@RequestBody UserPassVo passVo) throws Exception {
        String oldPass = RsaEncryptUtil.decryptByPrivateKey(RsaProperties.privateKey, passVo.getOldPass());
        String newPass = RsaEncryptUtil.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
        User user = userService.getUserByUsername(SecurityUtil.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePasswordByUsername(user.getUsername(), passwordEncoder.encode(newPass));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("重置密码")
    @PutMapping(value = "/resetPwd")
    public ResponseEntity<Object> resetPwd(@RequestBody Set<Long> ids) {
        String defaultPwd = passwordEncoder.encode("123456");
        userService.resetPasswordByIds(ids, defaultPwd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改头像")
    @PostMapping(value = "/updateAvatar")
    public ResponseEntity<Object> updateUserAvatar(@RequestParam MultipartFile avatar) {
        return new ResponseEntity<>(userService.updateAvatarFile(avatar), HttpStatus.OK);
    }

    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResponseEntity<Object> updateUserEmail(@PathVariable String code, @RequestBody User resources) throws Exception {
        String password = RsaEncryptUtil.decryptByPrivateKey(RsaProperties.privateKey, resources.getPassword());
        User user = userService.getUserByUsername(SecurityUtil.getCurrentUsername());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.checkCode(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey(), resources.getEmail(), code);
        userService.updateEmailByUsername(user.getUsername(), resources.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources /
     */
    private void checkLevel(User resources) {
        Integer currentLevel = Collections.min(roleService.selectRoleByUsersId(SecurityUtil.getCurrentUserId()).stream().map(Role::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.getDeptLevelByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }

    @ApiOperation("查询用户基础数据")
    @PostMapping(value = "/queryUserMetaPage")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<List<MyMetaOption>> queryUserMetaPage(@Validated @RequestBody UserQueryCriteria criteria) {
        int maxPageSize = 50;
        return new ResponseEntity<>(userService.page(new Page<>(criteria.getPage(), maxPageSize), new LambdaQueryWrapper<User>()
                .and(c -> {
                    c.eq(User::getPhone, criteria.getBlurry());
                    c.or();
                    c.eq(User::getEmail, criteria.getBlurry());
                    c.or();
                    c.like(User::getUsername, criteria.getBlurry());
                    c.or();
                    c.like(User::getNickName, criteria.getBlurry());
                })
        ).getRecords().stream().map(m -> {
            Map<String, Object> ext = new HashMap<>(1);
            ext.put("id", m.getId());
            ext.put("deptId", m.getDeptId());
            ext.put("email", m.getEmail());
            ext.put("phone", m.getPhone());
            return MyMetaOption.builder()
                    .label(m.getNickName())
                    .value(m.getUsername())
                    .ext(ext)
                    .build();
        }).collect(Collectors.toList()), HttpStatus.OK);
    }
}
