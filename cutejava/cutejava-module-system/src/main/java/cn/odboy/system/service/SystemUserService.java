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

package cn.odboy.system.service;

import cn.hutool.core.util.StrUtil;
import cn.odboy.base.CsPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.server.core.CsFileLocalUploadHelper;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemQueryUserArgs;
import cn.odboy.system.dal.mysql.SystemUserJobMapper;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import cn.odboy.system.dal.mysql.SystemUserRoleMapper;
import cn.odboy.system.dal.redis.SystemUserInfoDAO;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.system.framework.permission.core.CsSecurityHelper;
import cn.odboy.util.CsFileUtil;
import cn.odboy.util.CsPageUtil;
import cn.odboy.util.CsStringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SystemUserService {
    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private SystemUserJobMapper systemUserJobMapper;
    @Autowired
    private SystemUserRoleMapper systemUserRoleMapper;
    @Autowired
    private SystemUserInfoDAO systemUserInfoDAO;
    @Autowired
    private SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;
    @Autowired
    private CsFileLocalUploadHelper fileUploadPathHelper;

    /**
     * 新增用户
     *
     * @param args /
     */

    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SystemUserTb args) {
        args.setDeptId(args.getDept().getId());
        if (systemUserMapper.getUserByUsername(args.getUsername()) != null) {
            throw new BadRequestException("用户名已存在");
        }
        if (systemUserMapper.getUserByEmail(args.getEmail()) != null) {
            throw new BadRequestException("邮箱已存在");
        }
        if (systemUserMapper.getUserByPhone(args.getPhone()) != null) {
            throw new BadRequestException("手机号已存在");
        }
        systemUserMapper.insert(args);
        // 保存用户岗位
        systemUserJobMapper.batchInsertUserJob(args.getJobs(), args.getId());
        // 保存用户角色
        systemUserRoleMapper.batchInsertUserRole(args.getRoles(), args.getId());
    }

    /**
     * 编辑用户
     *
     * @param args /
     * @throws Exception /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyUserById(SystemUserTb args) {
        SystemUserTb user = systemUserMapper.selectById(args.getId());
        SystemUserTb user1 = systemUserMapper.getUserByUsername(args.getUsername());
        SystemUserTb user2 = systemUserMapper.getUserByEmail(args.getEmail());
        SystemUserTb user3 = systemUserMapper.getUserByPhone(args.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new BadRequestException("用户名已存在");
        }
        if (user2 != null && !user.getId().equals(user2.getId())) {
            throw new BadRequestException("邮箱已存在");
        }
        if (user3 != null && !user.getId().equals(user3.getId())) {
            throw new BadRequestException("手机号已存在");
        }
        // 如果用户被禁用, 则清除用户登录信息
        if (!args.getEnabled()) {
            systemUserOnlineInfoDAO.kickOutByUsername(args.getUsername());
        }
        user.setDeptId(args.getDept().getId());
        user.setUsername(args.getUsername());
        user.setEmail(args.getEmail());
        user.setEnabled(args.getEnabled());
        user.setRoles(args.getRoles());
        user.setDept(args.getDept());
        user.setJobs(args.getJobs());
        user.setPhone(args.getPhone());
        user.setNickName(args.getNickName());
        user.setGender(args.getGender());
        systemUserMapper.insertOrUpdate(user);
        // 清除用户登录缓存
        systemUserInfoDAO.deleteUserLoginInfoByUserName(user.getUsername());
        // 更新用户岗位
        systemUserJobMapper.batchDeleteUserJob(Collections.singleton(args.getId()));
        systemUserJobMapper.batchInsertUserJob(args.getJobs(), args.getId());
        // 更新用户角色
        systemUserRoleMapper.batchDeleteUserRole(Collections.singleton(args.getId()));
        systemUserRoleMapper.batchInsertUserRole(args.getRoles(), args.getId());
    }

    /**
     * 用户自助修改资料
     *
     * @param args /
     */

    @Transactional(rollbackFor = Exception.class)
    public void modifyUserCenterInfoById(SystemUserTb args) {
        SystemUserTb user = systemUserMapper.selectById(args.getId());
        SystemUserTb user1 = systemUserMapper.getUserByPhone(args.getPhone());
        if (user1 != null && !user.getId().equals(user1.getId())) {
            throw new BadRequestException("手机号已存在");
        }
        user.setNickName(args.getNickName());
        user.setPhone(args.getPhone());
        user.setGender(args.getGender());
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
        SystemUserTb user = systemUserMapper.getUserByUsername(CsSecurityHelper.getCurrentUsername());
        String username = user.getUsername();
        if (StrUtil.isBlank(username)) {
            throw new BadRequestException("异常用户数据, 请联系管理员处理");
        }
        // 文件大小验证
        CsFileUtil.checkSize(fileUploadPathHelper.getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = CsFileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (fileType != null && !image.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + image + " 格式");
        }
        String oldPath = user.getAvatarPath();
        File file = CsFileUtil.upload(multipartFile, fileUploadPathHelper.getPath());
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        systemUserMapper.insertOrUpdate(user);
        if (StrUtil.isNotBlank(oldPath)) {
            CsFileUtil.del(oldPath);
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
        CsFileUtil.downloadExcel(list, response);
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */

    public CsPageResult<SystemUserTb> queryUserByArgs(SystemQueryUserArgs criteria, Page<SystemUserTb> page) {
        IPage<SystemUserTb> users = systemUserMapper.selectUserByArgs(criteria, page);
        return CsPageUtil.toPage(users);
    }

    /**
     * 查询全部不分页
     *
     * @param criteria 条件
     * @return /
     */

    public List<SystemUserTb> queryUserByArgs(SystemQueryUserArgs criteria) {
        return systemUserMapper.selectUserByArgs(criteria);
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

    public List<SystemUserTb> queryUserByUserIds(List<Long> userIds) {
        return systemUserMapper.selectByIds(userIds);
    }
}
