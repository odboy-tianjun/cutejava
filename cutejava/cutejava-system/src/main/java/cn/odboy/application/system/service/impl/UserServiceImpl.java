package cn.odboy.application.system.service.impl;

import cn.odboy.application.core.service.OnlineUserService;
import cn.odboy.application.core.service.UserCacheService;
import cn.odboy.application.system.mapper.UserJobMapper;
import cn.odboy.application.system.mapper.UserMapper;
import cn.odboy.application.system.mapper.UserRoleMapper;
import cn.odboy.application.system.service.UserService;
import cn.odboy.base.PageResult;
import cn.odboy.constant.SystemRedisKey;
import cn.odboy.exception.BadRequestException;
import cn.odboy.exception.EntityExistException;
import cn.odboy.model.system.domain.Job;
import cn.odboy.model.system.domain.Role;
import cn.odboy.model.system.domain.User;
import cn.odboy.model.system.dto.UserQueryCriteria;
import cn.odboy.properties.FileProperties;
import cn.odboy.util.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserMapper userMapper;
    private final UserJobMapper userJobMapper;
    private final UserRoleMapper userRoleMapper;
    private final FileProperties fileProperties;
    private final RedisUtil redisUtil;
    private final UserCacheService userCacheService;
    private final OnlineUserService onlineUserService;

    @Override
    public PageResult<User> queryUserPage(UserQueryCriteria criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<User> users = userMapper.queryUserPage(criteria, PageUtil.getCount(userMapper)).getRecords();
        Long total = userMapper.getUserCountByCriteria(criteria);
        return PageUtil.toPage(users, total);
    }

    @Override
    public List<User> selectUserByCriteria(UserQueryCriteria criteria) {
        return userMapper.queryUserPage(criteria, PageUtil.getCount(userMapper)).getRecords();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User getUserById(long id) {
        String key = SystemRedisKey.USER_ID + id;
        User user = redisUtil.get(key, User.class);
        if (user == null) {
            user = getById(id);
            redisUtil.set(key, user, 1, TimeUnit.DAYS);
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(User resources) {
        resources.setDeptId(resources.getDept().getId());
        if (userMapper.getUserByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
        if (userMapper.getUserByEmail(resources.getEmail()) != null) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        if (userMapper.getUserByPhone(resources.getPhone()) != null) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        save(resources);
        // 保存用户岗位
        userJobMapper.insertData(resources.getId(), resources.getJobs());
        // 保存用户角色
        userRoleMapper.insertData(resources.getId(), resources.getRoles());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserById(User resources) {
        User user = getById(resources.getId());
        User user1 = userMapper.getUserByUsername(resources.getUsername());
        User user2 = userMapper.getUserByEmail(resources.getEmail());
        User user3 = userMapper.getUserByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "username", resources.getUsername());
        }
        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new EntityExistException(User.class, "email", resources.getEmail());
        }
        if (user3 != null && !user.getId().equals(user3.getId())) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        // 如果用户的角色改变
        if (!resources.getRoles().equals(user.getRoles())) {
            redisUtil.del(SystemRedisKey.DATA_USER + resources.getId());
            redisUtil.del(SystemRedisKey.MENU_USER + resources.getId());
            redisUtil.del(SystemRedisKey.ROLE_AUTH + resources.getId());
            redisUtil.del(SystemRedisKey.ROLE_USER + resources.getId());
        }
        // 修改部门会影响 数据权限
        if (!Objects.equals(resources.getDept(), user.getDept())) {
            redisUtil.del(SystemRedisKey.DATA_USER + resources.getId());
        }
        // 如果用户被禁用，则清除用户登录信息
        if (!resources.getEnabled()) {
            onlineUserService.kickOutByUsername(resources.getUsername());
        }
        user.setDeptId(resources.getDept().getId());
        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setRoles(resources.getRoles());
        user.setDept(resources.getDept());
        user.setJobs(resources.getJobs());
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setGender(resources.getGender());
        saveOrUpdate(user);
        // 清除缓存
        delCaches(user.getId(), user.getUsername());
        // 更新用户岗位
        userJobMapper.deleteByUserId(resources.getId());
        userJobMapper.insertData(resources.getId(), resources.getJobs());
        // 更新用户角色
        userRoleMapper.deleteByUserId(resources.getId());
        userRoleMapper.insertData(resources.getId(), resources.getRoles());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCenterInfoById(User resources) {
        User user = getById(resources.getId());
        User user1 = userMapper.getUserByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(User.class, "phone", resources.getPhone());
        }
        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        saveOrUpdate(user);
        // 清理缓存
        delCaches(user.getId(), user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserByIds(Set<Long> ids) {
        for (Long id : ids) {
            // 清理缓存
            User user = getById(id);
            delCaches(user.getId(), user.getUsername());
        }
        userMapper.deleteByIds(ids);
        // 删除用户岗位
        userJobMapper.deleteByUserIds(ids);
        // 删除用户角色
        userRoleMapper.deleteByUserIds(ids);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePasswordByUsername(String username, String pass) {
        userMapper.updatePasswordByUsername(username, pass, new Date());
        flushCache(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPasswordByIds(Set<Long> ids, String password) {
        List<User> users = userMapper.selectByIds(ids);
        // 清除缓存
        users.forEach(user -> {
            // 清除缓存
            flushCache(user.getUsername());
            // 强制退出
            onlineUserService.kickOutByUsername(user.getUsername());
        });
        // 重置密码
        userMapper.resetPasswordByUserIds(ids, password);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateAvatarFile(MultipartFile multipartFile) {
        // 文件大小验证
        FileUtil.checkSize(fileProperties.getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (fileType != null && !image.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + image + " 格式");
        }
        User user = userMapper.getUserByUsername(SecurityUtil.getCurrentUsername());
        String oldPath = user.getAvatarPath();
        File file = FileUtil.upload(multipartFile, fileProperties.getPath().getAvatar());
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        saveOrUpdate(user);
        if (StringUtil.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
        @NotBlank String username = user.getUsername();
        flushCache(username);
        return new HashMap<>(1) {{
            put("avatar", file.getName());
        }};
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmailByUsername(String username, String email) {
        userMapper.updateEmailByUsername(username, email);
        flushCache(username);
    }

    @Override
    public void downloadExcel(List<User> users, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (User user : users) {
            List<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUsername());
            map.put("角色", roles);
            map.put("部门", user.getDept().getName());
            map.put("岗位", user.getJobs().stream().map(Job::getName).collect(Collectors.toList()));
            map.put("邮箱", user.getEmail());
            map.put("状态", user.getEnabled() ? "启用" : "禁用");
            map.put("手机号码", user.getPhone());
            map.put("修改密码的时间", user.getPwdResetTime());
            map.put("创建日期", user.getCreateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id, String username) {
        redisUtil.del(SystemRedisKey.USER_ID + id);
        flushCache(username);
    }

    /**
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushCache(String username) {
        userCacheService.cleanUserCacheByUsername(username);
    }
}
