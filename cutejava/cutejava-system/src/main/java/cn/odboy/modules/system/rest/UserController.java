package cn.odboy.modules.system.rest;

import cn.hutool.core.collection.CollectionUtil;
import cn.odboy.base.PageArgs;
import cn.odboy.config.properties.RsaProperties;
import cn.odboy.constant.CodeEnum;
import cn.odboy.exception.BadRequestException;
import cn.odboy.modules.system.domain.Dept;
import cn.odboy.modules.system.domain.Role;
import cn.odboy.modules.system.domain.User;
import cn.odboy.modules.system.domain.dto.UserPassVo;
import cn.odboy.modules.system.domain.dto.UserQueryCriteria;
import cn.odboy.modules.system.service.*;
import cn.odboy.base.PageResult;
import cn.odboy.util.PageUtil;
import cn.odboy.util.RsaUtil;
import cn.odboy.util.SecurityUtil;
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
import java.util.Collections;
import java.util.List;
import java.util.Set;
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
    private final VerifyService verificationCodeService;

    @ApiOperation("导出用户数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    public void exportUser(HttpServletResponse response, UserQueryCriteria criteria) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    @ApiOperation("查询用户")
    @PostMapping(value = "/query")
    @PreAuthorize("@el.check('user:list')")
    public ResponseEntity<PageResult<User>> queryUser(@Validated @RequestBody PageArgs<UserQueryCriteria> criteria) {
        Page<User> page = new Page<>(criteria.getPage(), criteria.getPageSize());
        UserQueryCriteria args = criteria.getArgs();

        if (!ObjectUtils.isEmpty(args.getDeptId())) {
            args.getDeptIds().add(args.getDeptId());
            // 先查找是否存在子节点
            List<Dept> data = deptService.findByPid(args.getDeptId());
            // 然后把子节点的ID都加入到集合中
            args.getDeptIds().addAll(deptService.getDeptChildren(data));
        }
        // 数据权限
        List<Long> dataScopes = dataService.getDeptIds(userService.findByName(SecurityUtil.getCurrentUsername()));
        // criteria.getDeptIds() 不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(args.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)) {
            // 取交集
            args.getDeptIds().retainAll(dataScopes);
            if (!CollectionUtil.isEmpty(args.getDeptIds())) {
                return new ResponseEntity<>(userService.queryAll(criteria.getArgs(), page), HttpStatus.OK);
            }
        } else {
            // 否则取并集
            args.getDeptIds().addAll(dataScopes);
            return new ResponseEntity<>(userService.queryAll(criteria.getArgs(), page), HttpStatus.OK);
        }
        return new ResponseEntity<>(PageUtil.noData(), HttpStatus.OK);
    }

    @ApiOperation("新增用户")
    @PostMapping(value = "/save")
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<Object> createUser(@Validated @RequestBody User resources) {
        checkLevel(resources);
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        userService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改用户")
    @PostMapping(value = "/modify")
    @PreAuthorize("@el.check('user:edit')")
    public ResponseEntity<Object> updateUser(@Validated(User.Update.class) @RequestBody User resources) throws Exception {
        checkLevel(resources);
        userService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("修改用户：个人中心")
    @PostMapping(value = "center")
    public ResponseEntity<Object> centerUser(@Validated(User.Update.class) @RequestBody User resources) {
        if (!resources.getId().equals(SecurityUtil.getCurrentUserId())) {
            throw new BadRequestException("不能修改他人资料");
        }
        userService.updateCenter(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除用户")
    @PostMapping(value = "/remove")
    @PreAuthorize("@el.check('user:del')")
    public ResponseEntity<Object> deleteUser(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(roleService.findByUsersId(SecurityUtil.getCurrentUserId()).stream().map(Role::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(roleService.findByUsersId(id).stream().map(Role::getLevel).collect(Collectors.toList()));
            if (currentLevel > optLevel) {
                throw new BadRequestException("角色权限不足，不能删除：" + userService.findById(id).getUsername());
            }
        }
        userService.delete(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/updatePass")
    public ResponseEntity<Object> updateUserPass(@RequestBody UserPassVo passVo) throws Exception {
        String oldPass = RsaUtil.decryptByPrivateKey(RsaProperties.privateKey, passVo.getOldPass());
        String newPass = RsaUtil.decryptByPrivateKey(RsaProperties.privateKey, passVo.getNewPass());
        User user = userService.findByName(SecurityUtil.getCurrentUsername());
        if (!passwordEncoder.matches(oldPass, user.getPassword())) {
            throw new BadRequestException("修改失败，旧密码错误");
        }
        if (passwordEncoder.matches(newPass, user.getPassword())) {
            throw new BadRequestException("新密码不能与旧密码相同");
        }
        userService.updatePass(user.getUsername(), passwordEncoder.encode(newPass));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("重置密码")
    @PostMapping(value = "/resetPwd")
    public ResponseEntity<Object> resetPwd(@RequestBody Set<Long> ids) {
        String pwd = passwordEncoder.encode("123456");
        userService.resetPwd(ids, pwd);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("修改头像")
    @PostMapping(value = "/updateAvatar")
    public ResponseEntity<Object> updateUserAvatar(@RequestParam MultipartFile avatar) {
        return new ResponseEntity<>(userService.updateAvatar(avatar), HttpStatus.OK);
    }

    @ApiOperation("修改邮箱")
    @PostMapping(value = "/updateEmail/{code}")
    public ResponseEntity<Object> updateUserEmail(@PathVariable String code, @RequestBody User resources) throws Exception {
        String password = RsaUtil.decryptByPrivateKey(RsaProperties.privateKey, resources.getPassword());
        User user = userService.findByName(SecurityUtil.getCurrentUsername());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadRequestException("密码错误");
        }
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + resources.getEmail(), code);
        userService.updateEmail(user.getUsername(), resources.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 如果当前用户的角色级别低于创建用户的角色级别，则抛出权限不足的错误
     *
     * @param resources /
     */
    private void checkLevel(User resources) {
        Integer currentLevel = Collections.min(roleService.findByUsersId(SecurityUtil.getCurrentUserId()).stream().map(Role::getLevel).collect(Collectors.toList()));
        Integer optLevel = roleService.findByRoles(resources.getRoles());
        if (currentLevel > optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }
}
