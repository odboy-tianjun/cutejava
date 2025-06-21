package cn.odboy.core.service.system;

import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.system.SystemJobTb;
import cn.odboy.core.dal.dataobject.system.SystemRoleTb;
import cn.odboy.core.dal.dataobject.system.SystemUserTb;
import cn.odboy.core.dal.model.system.QuerySystemUserArgs;
import cn.odboy.core.dal.mysql.system.SystemUserJobMapper;
import cn.odboy.core.dal.mysql.system.SystemUserMapper;
import cn.odboy.core.dal.mysql.system.SystemUserRoleMapper;
import cn.odboy.core.dal.redis.system.SystemRedisKey;
import cn.odboy.core.dal.redis.system.SystemUserJwtService;
import cn.odboy.core.dal.redis.system.SystemUserOnlineService;
import cn.odboy.core.framework.permission.core.SecurityHelper;
import cn.odboy.core.framework.properties.AppProperties;
import cn.odboy.exception.BadRequestException;
import cn.odboy.exception.EntityExistException;
import cn.odboy.util.FileUtil;
import cn.odboy.util.PageUtil;
import cn.odboy.util.StringUtil;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemUserServiceImpl extends ServiceImpl<SystemUserMapper, SystemUserTb> implements SystemUserService {
    private final SystemUserMapper systemUserMapper;
    private final SystemUserJobMapper systemUserJobMapper;
    private final SystemUserRoleMapper systemUserRoleMapper;
    private final AppProperties properties;
    private final SystemUserJwtService systemUserJwtService;
    private final SystemUserOnlineService systemUserOnlineService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SystemUserTb resources) {
        resources.setDeptId(resources.getDept().getId());
        if (systemUserMapper.getUserByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(SystemUserTb.class, "username", resources.getUsername());
        }
        if (systemUserMapper.getUserByEmail(resources.getEmail()) != null) {
            throw new EntityExistException(SystemUserTb.class, "email", resources.getEmail());
        }
        if (systemUserMapper.getUserByPhone(resources.getPhone()) != null) {
            throw new EntityExistException(SystemUserTb.class, "phone", resources.getPhone());
        }
        save(resources);
        // 保存用户岗位
        systemUserJobMapper.insertBatchWithUserId(resources.getJobs(), resources.getId());
        // 保存用户角色
        systemUserRoleMapper.insertBatchWithUserId(resources.getRoles(), resources.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUserById(SystemUserTb resources) {
        SystemUserTb user = getById(resources.getId());
        SystemUserTb user1 = systemUserMapper.getUserByUsername(resources.getUsername());
        SystemUserTb user2 = systemUserMapper.getUserByEmail(resources.getEmail());
        SystemUserTb user3 = systemUserMapper.getUserByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(SystemUserTb.class, "username", resources.getUsername());
        }
        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new EntityExistException(SystemUserTb.class, "email", resources.getEmail());
        }
        if (user3 != null && !user.getId().equals(user3.getId())) {
            throw new EntityExistException(SystemUserTb.class, "phone", resources.getPhone());
        }
        // 如果用户被禁用，则清除用户登录信息
        if (!resources.getEnabled()) {
            systemUserOnlineService.kickOutByUsername(resources.getUsername());
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
        // 清除用户登录缓存
        flushLoginCacheInfo(user.getUsername());
        // 更新用户岗位
        systemUserJobMapper.deleteByUserId(resources.getId());
        systemUserJobMapper.insertBatchWithUserId(resources.getJobs(), resources.getId());
        // 更新用户角色
        systemUserRoleMapper.deleteByUserId(resources.getId());
        systemUserRoleMapper.insertBatchWithUserId(resources.getRoles(), resources.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUserCenterInfoById(SystemUserTb resources) {
        SystemUserTb user = getById(resources.getId());
        SystemUserTb user1 = systemUserMapper.getUserByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new EntityExistException(SystemUserTb.class, "phone", resources.getPhone());
        }
        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        saveOrUpdate(user);
        flushLoginCacheInfo(user.getUsername());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeUserByIds(Set<Long> ids) {
        for (Long id : ids) {
            // 清理缓存
            SystemUserTb user = getById(id);
            flushLoginCacheInfo(user.getUsername());
        }
        systemUserMapper.deleteByIds(ids);
        // 删除用户岗位
        systemUserJobMapper.deleteByUserIds(ids);
        // 删除用户角色
        systemUserRoleMapper.deleteByUserIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUserPasswordByUsername(String username, String pass) {
        systemUserMapper.updatePasswordByUsername(username, pass, new Date());
        flushLoginCacheInfo(username);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetUserPasswordByIds(Set<Long> ids, String password) {
        List<SystemUserTb> users = systemUserMapper.selectByIds(ids);
        // 清除缓存
        users.forEach(user -> {
            // 清除缓存
            flushLoginCacheInfo(user.getUsername());
            // 强制退出
            systemUserOnlineService.kickOutByUsername(user.getUsername());
        });
        // 重置密码
        systemUserMapper.updatePasswordByUserIds(password, ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> modifyUserAvatar(MultipartFile multipartFile) {
        // 文件大小验证
        FileUtil.checkSize(properties.getFile().getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (fileType != null && !image.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + image + " 格式");
        }
        SystemUserTb user = systemUserMapper.getUserByUsername(SecurityHelper.getCurrentUsername());
        String oldPath = user.getAvatarPath();
        File file = FileUtil.upload(multipartFile, properties.getFile().getPath().getAvatar());
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        saveOrUpdate(user);
        if (StringUtil.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
        @NotBlank String username = user.getUsername();
        flushLoginCacheInfo(username);
        return new HashMap<>(1) {{
            put("avatar", file.getName());
        }};
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyUserEmailByUsername(String username, String email) {
        systemUserMapper.updateEmailByUsername(username, email);
        flushLoginCacheInfo(username);
    }

    @Override
    public void downloadUserExcel(List<SystemUserTb> users, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemUserTb user : users) {
            List<String> roles = user.getRoles().stream().map(SystemRoleTb::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUsername());
            map.put("角色", roles);
            map.put("部门", user.getDept().getName());
            map.put("岗位", user.getJobs().stream().map(SystemJobTb::getName).collect(Collectors.toList()));
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
     * 清理 登陆时 用户缓存信息
     *
     * @param username /
     */
    private void flushLoginCacheInfo(String username) {
        systemUserJwtService.cleanUserJwtModelCacheByUsername(username);
    }

    @Override
    public CsResultVo<List<SystemUserTb>> describeUserPage(QuerySystemUserArgs criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<SystemUserTb> users = systemUserMapper.queryUserPageByArgs(criteria, PageUtil.getCount(systemUserMapper)).getRecords();
        Long total = systemUserMapper.getUserCountByArgs(criteria);
        return PageUtil.toPage(users, total);
    }

    @Override
    public List<SystemUserTb> describeUserList(QuerySystemUserArgs criteria) {
        return systemUserMapper.queryUserPageByArgs(criteria, PageUtil.getCount(systemUserMapper)).getRecords();
    }

    @Override
    public SystemUserTb describeUserById(long id) {
        return systemUserMapper.selectById(id);
    }

    @Override
    public SystemUserTb describeUserByUsername(String username) {
        return systemUserMapper.getUserByUsername(username);
    }
}
