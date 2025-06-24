package cn.odboy.core.service;

import cn.hutool.core.util.StrUtil;
import cn.odboy.base.CsResultVo;
import cn.odboy.core.dal.dataobject.SystemJobTb;
import cn.odboy.core.dal.dataobject.SystemRoleTb;
import cn.odboy.core.dal.dataobject.SystemUserTb;
import cn.odboy.core.dal.model.QuerySystemUserArgs;
import cn.odboy.core.dal.mysql.SystemUserJobMapper;
import cn.odboy.core.dal.mysql.SystemUserMapper;
import cn.odboy.core.dal.mysql.SystemUserRoleMapper;
import cn.odboy.core.dal.redis.SystemUserInfoDAO;
import cn.odboy.core.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.core.framework.permission.core.SecurityHelper;
import cn.odboy.core.framework.properties.AppProperties;
import cn.odboy.exception.BadRequestException;
import cn.odboy.util.CsPageUtil;
import cn.odboy.util.FileUtil;
import cn.odboy.util.StringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemUserService {
    private final SystemUserMapper systemUserMapper;
    private final SystemUserJobMapper systemUserJobMapper;
    private final SystemUserRoleMapper systemUserRoleMapper;
    private final AppProperties properties;
    private final SystemUserInfoDAO systemUserInfoDAO;
    private final SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;

    /**
     * 新增用户
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SystemUserTb resources) {
        resources.setDeptId(resources.getDept().getId());
        if (systemUserMapper.getUserByUsername(resources.getUsername()) != null) {
            throw new BadRequestException("用户名已存在");
        }
        if (systemUserMapper.getUserByEmail(resources.getEmail()) != null) {
            throw new BadRequestException("邮箱已存在");
        }
        if (systemUserMapper.getUserByPhone(resources.getPhone()) != null) {
            throw new BadRequestException("手机号已存在");
        }
        systemUserMapper.insert(resources);
        // 保存用户岗位
        systemUserJobMapper.batchInsertUserJob(resources.getJobs(), resources.getId());
        // 保存用户角色
        systemUserRoleMapper.batchInsertUserRole(resources.getRoles(), resources.getId());
    }

    /**
     * 编辑用户
     *
     * @param resources /
     * @throws Exception /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyUserById(SystemUserTb resources) {
        SystemUserTb user = systemUserMapper.selectById(resources.getId());
        SystemUserTb user1 = systemUserMapper.getUserByUsername(resources.getUsername());
        SystemUserTb user2 = systemUserMapper.getUserByEmail(resources.getEmail());
        SystemUserTb user3 = systemUserMapper.getUserByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new BadRequestException("用户名已存在");
        }
        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new BadRequestException("邮箱已存在");
        }
        if (user3 != null && !user.getId().equals(user3.getId())) {
            throw new BadRequestException("手机号已存在");
        }
        // 如果用户被禁用，则清除用户登录信息
        if (!resources.getEnabled()) {
            systemUserOnlineInfoDAO.kickOutByUsername(resources.getUsername());
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
        systemUserMapper.insertOrUpdate(user);
        // 清除用户登录缓存
        systemUserInfoDAO.deleteUserLoginInfoByUserName(user.getUsername());
        // 更新用户岗位
        systemUserJobMapper.batchDeleteUserJob(Collections.singleton(resources.getId()));
        systemUserJobMapper.batchInsertUserJob(resources.getJobs(), resources.getId());
        // 更新用户角色
        systemUserRoleMapper.batchDeleteUserRole(Collections.singleton(resources.getId()));
        systemUserRoleMapper.batchInsertUserRole(resources.getRoles(), resources.getId());
    }

    /**
     * 用户自助修改资料
     *
     * @param resources /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyUserCenterInfoById(SystemUserTb resources) {
        SystemUserTb user = systemUserMapper.selectById(resources.getId());
        SystemUserTb user1 = systemUserMapper.getUserByPhone(resources.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new BadRequestException("手机号已存在");
        }
        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        systemUserMapper.insertOrUpdate(user);
        systemUserInfoDAO.deleteUserLoginInfoByUserName(user.getUsername());
    }

    /**
     * 删除用户
     *
     * @param ids /
     */

    @Transactional(rollbackFor = Exception.class)
    public void removeUserByIds(Set<Long> ids) {
        for (Long id : ids) {
            // 清理缓存
            SystemUserTb user = systemUserMapper.selectById(id);
            systemUserInfoDAO.deleteUserLoginInfoByUserName(user.getUsername());
        }
        systemUserMapper.deleteByIds(ids);
        // 删除用户岗位
        systemUserJobMapper.batchDeleteUserJob(ids);
        // 删除用户角色
        systemUserRoleMapper.batchDeleteUserRole(ids);
    }

    /**
     * 修改密码
     *
     * @param username        用户名
     * @param encryptPassword 密码
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyUserPasswordByUsername(String username, String encryptPassword) {
        systemUserMapper.updateUserPasswordByUsername(username, encryptPassword);
        systemUserInfoDAO.deleteUserLoginInfoByUserName(username);
    }

    /**
     * 重置密码
     *
     * @param ids      用户id
     * @param password 密码
     */

    @Transactional(rollbackFor = Exception.class)
    public void resetUserPasswordByIds(Set<Long> ids, String password) {
        List<SystemUserTb> users = systemUserMapper.selectByIds(ids);
        // 清除缓存
        users.forEach(user -> {
            // 清除缓存
            systemUserInfoDAO.deleteUserLoginInfoByUserName(user.getUsername());
            // 强制退出
            systemUserOnlineInfoDAO.kickOutByUsername(user.getUsername());
        });
        // 重置密码
        systemUserMapper.batchUpdatePassword(password, ids);
    }

    /**
     * 修改头像
     *
     * @param multipartFile 文件
     * @return /
     */

    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> modifyUserAvatar(MultipartFile multipartFile) {
        SystemUserTb user = systemUserMapper.getUserByUsername(SecurityHelper.getCurrentUsername());
        String username = user.getUsername();
        if (StrUtil.isBlank(username)) {
            throw new BadRequestException("异常用户数据，请联系管理员处理");
        }
        // 文件大小验证
        FileUtil.checkSize(properties.getFile().getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (fileType != null && !image.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + image + " 格式");
        }
        String oldPath = user.getAvatarPath();
        File file = FileUtil.upload(multipartFile, properties.getFile().getPath().getAvatar());
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        systemUserMapper.insertOrUpdate(user);
        if (StringUtil.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
        systemUserInfoDAO.deleteUserLoginInfoByUserName(username);
        return new HashMap<>(1) {{
            put("avatar", file.getName());
        }};
    }

    /**
     * 修改邮箱
     *
     * @param username 用户名
     * @param email    邮箱
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyUserEmailByUsername(String username, String email) {
        systemUserMapper.updateUserEmailByUsername(username, email);
        systemUserInfoDAO.deleteUserLoginInfoByUserName(username);
    }

    /**
     * 导出数据
     *
     * @param users    待导出的数据
     * @param response /
     * @throws IOException /
     */

    public void exportUserExcel(List<SystemUserTb> users, HttpServletResponse response) throws IOException {
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
     * 查询全部
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */

    public CsResultVo<List<SystemUserTb>> queryUserByArgs(QuerySystemUserArgs criteria, Page<Object> page) {
        criteria.setOffset(page.offset());
        List<SystemUserTb> users = systemUserMapper.selectUserByArgs(criteria, CsPageUtil.getCount(systemUserMapper)).getRecords();
        Long total = systemUserMapper.countUserByArgs(criteria);
        return CsPageUtil.toPage(users, total);
    }

    /**
     * 查询全部不分页
     *
     * @param criteria 条件
     * @return /
     */

    public List<SystemUserTb> queryUserByArgs(QuerySystemUserArgs criteria) {
        return systemUserMapper.selectUserByArgs(criteria, CsPageUtil.getCount(systemUserMapper)).getRecords();
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */

    public SystemUserTb getUserById(long id) {
        return systemUserMapper.selectById(id);
    }

    /**
     * 根据用户名查询
     *
     * @param username /
     * @return /
     */

    public SystemUserTb getUserByUsername(String username) {
        return systemUserMapper.getUserByUsername(username);
    }

    /**
     * 分页模糊查询
     *
     * @param wrapper
     * @param page
     * @return
     */
    public IPage<SystemUserTb> queryUserByBlurry(LambdaQueryWrapper<SystemUserTb> wrapper, Page<SystemUserTb> page) {
        return systemUserMapper.selectPage(page, wrapper);
    }
}
