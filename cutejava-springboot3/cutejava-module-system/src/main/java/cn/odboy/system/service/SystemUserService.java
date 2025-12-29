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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.base.KitSelectOptionVo;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.server.core.KitFileLocalUploadHelper;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserJobTb;
import cn.odboy.system.dal.dataobject.SystemUserRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemQueryUserArgs;
import cn.odboy.system.dal.model.SystemUserVo;
import cn.odboy.system.dal.mysql.SystemDeptMapper;
import cn.odboy.system.dal.mysql.SystemJobMapper;
import cn.odboy.system.dal.mysql.SystemRoleMapper;
import cn.odboy.system.dal.mysql.SystemUserJobMapper;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import cn.odboy.system.dal.mysql.SystemUserRoleMapper;
import cn.odboy.system.dal.redis.SystemUserInfoDAO;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SystemUserService {

    @Autowired
    private SystemUserMapper systemUserMapper;
    @Autowired
    private SystemUserJobService systemUserJobService;
    @Autowired
    private SystemUserRoleService systemUserRoleService;
    @Autowired
    private SystemUserInfoDAO systemUserInfoDAO;
    @Autowired
    private SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;
    @Autowired
    private KitFileLocalUploadHelper fileUploadPathHelper;
    @Autowired
    private SystemUserJobMapper systemUserJobMapper;
    @Autowired
    private SystemUserRoleMapper systemUserRoleMapper;
    @Autowired
    private SystemJobMapper systemJobMapper;
    @Autowired
    private SystemRoleMapper systemRoleMapper;
    @Autowired
    private SystemDeptMapper systemDeptMapper;

    /**
     * 新增用户
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SystemUserVo args) {
        args.setDeptId(args.getDept().getId());
        if (this.getUserByUsername(args.getUsername()) != null) {
            throw new BadRequestException("用户名已存在");
        }
        if (this.getUserByEmail(args.getEmail()) != null) {
            throw new BadRequestException("邮箱已存在");
        }
        if (this.getUserByPhone(args.getPhone()) != null) {
            throw new BadRequestException("手机号已存在");
        }
        systemUserMapper.insert(args);
        // 保存用户岗位
        systemUserJobService.batchInsertUserJob(args.getJobs(), args.getId());
        // 保存用户角色
        systemUserRoleService.batchInsertUserRole(args.getRoles(), args.getId());
    }

    /**
     * 编辑用户
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserById(SystemUserVo args) {
        SystemUserTb user = systemUserMapper.selectById(args.getId());
        SystemUserTb user1 = this.getUserByUsername(args.getUsername());
        SystemUserTb user2 = this.getUserByEmail(args.getEmail());
        SystemUserTb user3 = this.getUserByPhone(args.getPhone());
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
        user.setDept(args.getDept());
        user.setPhone(args.getPhone());
        user.setNickName(args.getNickName());
        user.setGender(args.getGender());
        systemUserMapper.insertOrUpdate(user);
        // 清除用户登录缓存
        systemUserInfoDAO.deleteUserLoginInfoByUserName(user.getUsername());
        // 更新用户岗位
        systemUserJobService.batchDeleteUserJob(Collections.singleton(args.getId()));
        systemUserJobService.batchInsertUserJob(args.getJobs(), args.getId());
        // 更新用户角色
        systemUserRoleService.batchDeleteUserRole(Collections.singleton(args.getId()));
        systemUserRoleService.batchInsertUserRole(args.getRoles(), args.getId());
    }

    /**
     * 用户自助修改资料
     *
     * @param args /
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserCenterInfoById(SystemUserTb args) {
        SystemUserTb user = systemUserMapper.selectById(args.getId());
        SystemUserTb user1 = this.getUserByPhone(args.getPhone());
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
        systemUserJobService.batchDeleteUserJob(ids);
        // 删除用户角色
        systemUserRoleService.batchDeleteUserRole(ids);
    }

    /**
     * 修改密码
     *
     * @param username        用户名
     * @param encryptPassword 密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserPasswordByUsername(String username, String encryptPassword) {
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
        systemUserMapper.updateUserPasswordByUserIds(password, ids);
    }

    /**
     * 修改头像
     *
     * @param multipartFile 文件
     * @return /
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> updateUserAvatar(MultipartFile multipartFile) {
        SystemUserTb user = this.getUserByUsername(KitSecurityHelper.getCurrentUsername());
        String username = user.getUsername();
        if (StrUtil.isBlank(username)) {
            throw new BadRequestException("异常用户数据, 请联系管理员处理");
        }
        // 文件大小验证
        KitFileUtil.checkSize(fileUploadPathHelper.getAvatarMaxSize(), multipartFile.getSize());
        // 验证文件上传的格式
        String image = "gif jpg png jpeg";
        String fileType = KitFileUtil.getSuffix(multipartFile.getOriginalFilename());
        if (fileType != null && !image.contains(fileType)) {
            throw new BadRequestException("文件格式错误！, 仅支持 " + image + " 格式");
        }
        String oldPath = user.getAvatarPath();
        File file = KitFileUtil.upload(multipartFile, fileUploadPathHelper.getPath());
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        systemUserMapper.insertOrUpdate(user);
        if (StrUtil.isNotBlank(oldPath)) {
            KitFileUtil.del(oldPath);
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
    public void updateUserEmailByUsername(String username, String email) {
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
    public void exportUserExcel(List<SystemUserVo> users, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SystemUserVo user : users) {
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
        KitFileUtil.downloadExcel(list, response);
    }

    /**
     * 查询全部
     *
     * @param args 条件
     * @param page 分页参数
     * @return /
     */
    public KitPageResult<SystemUserVo> searchUserByArgs(SystemQueryUserArgs args, Page<SystemUserTb> page) {
        // 查询用户基本信息
        LambdaQueryWrapper<SystemUserTb> wrapper = buildUserQueryWrapper(args);
        IPage<SystemUserTb> userPage = systemUserMapper.selectPage(page, wrapper);
        List<SystemUserTb> users = userPage.getRecords();
        // 转换为SystemUserVo并关联查询
        List<SystemUserVo> userVos = users.stream().map(this::convertToUserVo).collect(Collectors.toList());
        return KitPageUtil.toPage(userVos, userPage.getTotal());
    }

    /**
     * 查询全部不分页
     *
     * @param args 条件
     * @return /
     */
    public List<SystemUserVo> queryUserByArgs(SystemQueryUserArgs args) {
        // 查询用户基本信息
        LambdaQueryWrapper<SystemUserTb> wrapper = buildUserQueryWrapper(args);
        List<SystemUserTb> users = systemUserMapper.selectList(wrapper);
        // 转换为SystemUserVo并关联查询
        return users.stream().map(this::convertToUserVo).collect(Collectors.toList());
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

    public List<KitSelectOptionVo> queryUserMetadataOptions(KitPageArgs<SystemQueryUserArgs> pageArgs) {
        SystemQueryUserArgs args = pageArgs.getArgs();
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SystemUserTb::getId, SystemUserTb::getDeptId, SystemUserTb::getEmail, SystemUserTb::getPhone);
        wrapper.and(c -> {
            c.eq(SystemUserTb::getPhone, args.getBlurry());
            c.or();
            c.eq(SystemUserTb::getEmail, args.getBlurry());
            c.or();
            c.like(SystemUserTb::getUsername, args.getBlurry());
            c.or();
            c.like(SystemUserTb::getNickName, args.getBlurry());
        });
        return systemUserMapper.selectPage(new Page<>(args.getPage(), 50), wrapper).getRecords().stream().map(m -> {
            Map<String, Object> ext = new HashMap<>(1);
            ext.put("id", m.getId());
            ext.put("deptId", m.getDeptId());
            ext.put("email", m.getEmail());
            ext.put("phone", m.getPhone());
            return KitSelectOptionVo.builder().label(m.getNickName()).value(String.valueOf(m.getId())).ext(ext).build();
        }).collect(Collectors.toList());
    }

    /**
     * 根据岗位ID统计用户数量
     *
     * @param jobIds 岗位ID集合
     * @return /
     */
    public Long countUserByJobIds(Set<Long> jobIds) {
        if (CollUtil.isEmpty(jobIds)) {
            return 0L;
        }
        LambdaQueryWrapper<SystemUserJobTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SystemUserJobTb::getJobId, jobIds);
        return systemUserJobMapper.selectCount(wrapper);
    }

    /**
     * 根据角色ID统计用户数量
     *
     * @param roleIds 角色ID集合
     * @return /
     */
    public Long countUserByRoleIds(Set<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return 0L;
        }
        LambdaQueryWrapper<SystemUserRoleTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SystemUserRoleTb::getRoleId, roleIds);
        return systemUserRoleMapper.selectCount(wrapper);
    }

    /**
     * 构建用户查询条件
     *
     * @param args 查询条件
     * @return /
     */
    private LambdaQueryWrapper<SystemUserTb> buildUserQueryWrapper(SystemQueryUserArgs args) {
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        if (args != null) {
            if (args.getId() != null) {
                wrapper.eq(SystemUserTb::getId, args.getId());
            }
            if (args.getEnabled() != null) {
                wrapper.eq(SystemUserTb::getEnabled, args.getEnabled());
            }
            if (CollUtil.isNotEmpty(args.getDeptIds())) {
                wrapper.in(SystemUserTb::getDeptId, args.getDeptIds());
            }
            if (StrUtil.isNotBlank(args.getBlurry())) {
                wrapper.and(w -> w.like(SystemUserTb::getUsername, args.getBlurry())
                    .or().like(SystemUserTb::getNickName, args.getBlurry())
                    .or().like(SystemUserTb::getEmail, args.getBlurry()));
            }
            if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
                wrapper.between(SystemUserTb::getCreateTime, args.getCreateTime().get(0),
                    args.getCreateTime().get(1));
            }
        }
        wrapper.orderByDesc(SystemUserTb::getCreateTime);
        return wrapper;
    }

    /**
     * 转换为SystemUserVo并关联查询部门、角色、岗位信息
     *
     * @param user 用户基本信息
     * @return 包含关联信息的SystemUserVo
     */
    private SystemUserVo convertToUserVo(SystemUserTb user) {
        if (user == null) {
            return null;
        }
        SystemUserVo userVo = BeanUtil.copyProperties(user, SystemUserVo.class);

        // 查询关联的部门信息
        if (user.getDeptId() != null) {
            SystemDeptTb dept = systemDeptMapper.selectById(user.getDeptId());
            userVo.setDept(dept);
        }

        // 查询关联的岗位信息
        LambdaQueryWrapper<SystemUserJobTb> jobWrapper = new LambdaQueryWrapper<>();
        jobWrapper.eq(SystemUserJobTb::getUserId, user.getId());
        List<SystemUserJobTb> userJobs = systemUserJobMapper.selectList(jobWrapper);
        if (CollUtil.isNotEmpty(userJobs)) {
            Set<Long> jobIds = userJobs.stream().map(SystemUserJobTb::getJobId).collect(Collectors.toSet());
            List<SystemJobTb> jobs = systemJobMapper.selectByIds(jobIds);
            userVo.setJobs(new LinkedHashSet<>(jobs));
        }

        // 查询关联的角色信息
        LambdaQueryWrapper<SystemUserRoleTb> roleWrapper = new LambdaQueryWrapper<>();
        roleWrapper.eq(SystemUserRoleTb::getUserId, user.getId());
        List<SystemUserRoleTb> userRoles = systemUserRoleMapper.selectList(roleWrapper);
        if (CollUtil.isNotEmpty(userRoles)) {
            Set<Long> roleIds = userRoles.stream().map(SystemUserRoleTb::getRoleId).collect(Collectors.toSet());
            List<SystemRoleTb> roles = systemRoleMapper.selectByIds(roleIds);
            userVo.setRoles(new LinkedHashSet<>(roles));
        }

        return userVo;
    }

    public SystemUserTb getUserByUsername(String username) {
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemUserTb::getUsername, username);
        wrapper.eq(SystemUserTb::getEnabled, 1);
        return systemUserMapper.selectOne(wrapper);
    }

    public SystemUserVo getUserVoByUsername(String username) {
        return this.convertToUserVo(this.getUserByUsername(username));
    }

    public long countUserByDeptIds(Set<Long> deptIds) {
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SystemUserTb::getDeptId, deptIds);
        return systemUserMapper.selectCount(wrapper);
    }

    public SystemUserTb getUserByEmail(String email) {
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemUserTb::getEmail, email);
        wrapper.eq(SystemUserTb::getEnabled, 1);
        return systemUserMapper.selectOne(wrapper);
    }

    public SystemUserTb getUserByPhone(String phone) {
        LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemUserTb::getPhone, phone);
        wrapper.eq(SystemUserTb::getEnabled, 1);
        return systemUserMapper.selectOne(wrapper);
    }
}
