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

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.base.KitSelectOptionVo;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.server.core.KitFileLocalUploadHelper;
import cn.odboy.system.constant.SystemCaptchaBizEnum;
import cn.odboy.system.constant.SystemZhConst;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserJobTb;
import cn.odboy.system.dal.dataobject.SystemUserRoleTb;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.export.SystemUserExportRowVo;
import cn.odboy.system.dal.model.request.SystemQueryUserArgs;
import cn.odboy.system.dal.model.request.SystemUpdateUserPasswordArgs;
import cn.odboy.system.dal.model.response.SystemDeptVo;
import cn.odboy.system.dal.model.response.SystemRoleVo;
import cn.odboy.system.dal.model.response.SystemUserVo;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import cn.odboy.system.dal.redis.SystemUserInfoDAO;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import cn.odboy.util.KitRsaEncryptUtil;
import cn.odboy.util.KitValidUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SystemUserService {

  @Autowired
  private SystemUserMapper systemUserMapper;
  @Autowired
  private SystemJobService systemJobService;
  @Autowired
  private SystemRoleService systemRoleService;
  @Autowired
  private SystemDeptService systemDeptService;
  @Autowired
  private SystemUserJobService systemUserJobService;
  @Autowired
  private SystemUserRoleService systemUserRoleService;
  @Autowired
  private SystemEmailService systemEmailService;
  @Autowired
  private SystemDataService systemDataService;
  @Autowired
  private SystemUserInfoDAO systemUserInfoDAO;
  @Autowired
  private SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;
  @Autowired
  private KitFileLocalUploadHelper fileUploadPathHelper;
  @Autowired
  private AppProperties properties;
  @Autowired
  private PasswordEncoder passwordEncoder;


  /**
   * 新增用户
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveUser(SystemUserVo args) {
    systemUserRoleService.checkLevel(args);
    // 默认密码 123456
    args.setPassword(passwordEncoder.encode("123456"));
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
    // 保存用户
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
    systemUserRoleService.checkLevel(args);
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
    if (!args.getId().equals(KitSecurityHelper.getCurrentUserId())) {
      throw new BadRequestException("不能修改他人资料");
    }
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
  public void deleteUserByIds(Set<Long> ids) {
    Long currentUserId = KitSecurityHelper.getCurrentUserId();
    // 校验权限
    for (Long id : ids) {
      Integer currentLevel = Collections.min(
          systemUserRoleService.queryRoleByUsersId(currentUserId).stream()
              .map(SystemRoleVo::getLevel).collect(Collectors.toList()));
      Integer optLevel = Collections.min(
          systemUserRoleService.queryRoleByUsersId(id).stream().map(SystemRoleVo::getLevel)
              .collect(Collectors.toList()));
      if (currentLevel > optLevel) {
        throw new BadRequestException(
            "角色权限不足, 不能删除：" + this.getUserById(id).getUsername());
      }
    }
    // 清理缓存
    List<String> usernameList = systemUserMapper.selectByIds(ids).stream().map(SystemUserTb::getUsername).distinct().collect(Collectors.toList());
    for (String username : usernameList) {
      systemUserInfoDAO.deleteUserLoginInfoByUserName(username);
    }
    // 删除用户
    systemUserMapper.deleteByIds(ids);
    // 删除用户岗位
    systemUserJobService.batchDeleteUserJob(ids);
    // 删除用户角色
    systemUserRoleService.batchDeleteUserRole(ids);
  }

  /**
   * 修改密码
   *
   * @param username 用户名
   * @param args     参数
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateUserPasswordByUsername(String username, SystemUpdateUserPasswordArgs args) throws Exception {
    String oldPass =
        KitRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), args.getOldPass());
    String newPass =
        KitRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), args.getNewPass());
    SystemUserTb user = this.getUserByUsername(username);
    if (!passwordEncoder.matches(oldPass, user.getPassword())) {
      throw new BadRequestException("修改失败，旧密码错误");
    }
    if (passwordEncoder.matches(newPass, user.getPassword())) {
      throw new BadRequestException("新密码不能与旧密码相同");
    }
    String encryptPassword = passwordEncoder.encode(newPass);
    systemUserMapper.updateUserPasswordByUsername(username, encryptPassword);
    systemUserInfoDAO.deleteUserLoginInfoByUserName(username);
  }

  /**
   * 重置密码
   *
   * @param ids 用户id
   */
  @Transactional(rollbackFor = Exception.class)
  public void resetUserPasswordByIds(Set<Long> ids) {
    String defaultPwd = passwordEncoder.encode("123456");
    List<SystemUserTb> users = systemUserMapper.selectByIds(ids);
    // 清除缓存
    for (SystemUserTb user : users) {
      // 清除缓存
      systemUserInfoDAO.deleteUserLoginInfoByUserName(user.getUsername());
      // 强制退出
      systemUserOnlineInfoDAO.kickOutByUsername(user.getUsername());
    }
    // 重置密码
    systemUserMapper.updateUserPasswordByUserIds(defaultPwd, ids);
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
   * @param args     参数
   * @param code     验证码
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateUserEmailByUsername(String username, SystemUserTb args, String code) throws Exception {
    String password =
        KitRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), args.getPassword());
    SystemUserTb user = this.getUserByUsername(username);
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BadRequestException("密码错误");
    }
    systemEmailService.checkEmailCaptchaV1(SystemCaptchaBizEnum.EMAIL_RESET_EMAIL_CODE, args.getEmail(), code);
    systemUserMapper.updateUserEmailByUsername(username, args.getEmail());
    systemUserInfoDAO.deleteUserLoginInfoByUserName(username);
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

  public long countUserByArgs(SystemQueryUserArgs args) {
    LambdaQueryWrapper<SystemUserTb> wrapper = buildUserQueryWrapper(args);
    return systemUserMapper.selectCount(wrapper);
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
    return systemUserMapper.selectPage(new Page<>(pageArgs.getPage(), 50), wrapper).getRecords().stream().map(m -> {
      Map<String, Object> ext = new HashMap<>(1);
      ext.put("id", m.getId());
      ext.put("deptId", m.getDeptId());
      ext.put("email", m.getEmail());
      ext.put("phone", m.getPhone());
      return KitSelectOptionVo.builder().label(m.getNickName()).value(String.valueOf(m.getId())).ext(ext).build();
    }).collect(Collectors.toList());
  }

  /**
   * 构建用户查询条件
   *
   * @param args 查询条件
   * @return /
   */
  private LambdaQueryWrapper<SystemUserTb> buildUserQueryWrapper(SystemQueryUserArgs args) {
    KitValidUtil.notNull(args);
    LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
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
    SystemUserVo userVo = KitBeanUtil.copyToClass(user, SystemUserVo.class);
    // 查询关联的部门信息
    if (user.getDeptId() != null) {
      SystemDeptVo dept = systemDeptService.getDeptVoById(user.getDeptId());
      userVo.setDept(dept);
    }
    // 查询关联的岗位信息
    List<SystemUserJobTb> userJobs = systemUserJobService.listUserJobByUserId(user.getId());
    if (CollUtil.isNotEmpty(userJobs)) {
      Set<Long> jobIds = userJobs.stream().map(SystemUserJobTb::getJobId).collect(Collectors.toSet());
      List<SystemJobTb> jobs = systemJobService.listByIds(jobIds);
      userVo.setJobs(new LinkedHashSet<>(jobs));
    }
    // 查询关联的角色信息
    List<SystemUserRoleTb> userRoles = systemUserRoleService.listUserRoleByUserId(user.getId());
    if (CollUtil.isNotEmpty(userRoles)) {
      Set<Long> roleIds = userRoles.stream().map(SystemUserRoleTb::getRoleId).collect(Collectors.toSet());
      List<SystemRoleTb> roles = systemRoleService.listByIds(roleIds);
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

  public KitPageResult<SystemUserVo> aggregationSearchUserByArgs(Page<SystemUserTb> page, SystemQueryUserArgs args, String currentUsername) {
    if (!ObjectUtils.isEmpty(args.getDeptId())) {
      args.getDeptIds().add(args.getDeptId());
      // 先查找是否存在子节点
      List<SystemDeptVo> data = systemDeptService.listDeptByPid(args.getDeptId());
      // 然后把子节点的ID都加入到集合中
      args.getDeptIds().addAll(systemDeptService.queryChildDeptIdByDeptIds(data));
    }
    // 数据权限
    List<Long> dataScopes = systemDataService.queryDeptIdByArgs(this.getUserVoByUsername(currentUsername));
    // args.getDeptIds() 不为空并且数据权限不为空则取交集
    if (!CollectionUtils.isEmpty(args.getDeptIds()) && !CollectionUtils.isEmpty(dataScopes)) {
      // 取交集
      args.getDeptIds().retainAll(dataScopes);
      if (!CollectionUtil.isEmpty(args.getDeptIds())) {
        return this.searchUserByArgs(args, page);
      }
    } else {
      // 否则取并集
      args.getDeptIds().addAll(dataScopes);
      return this.searchUserByArgs(args, page);
    }
    return KitPageUtil.emptyData();
  }

  public void exportUserXlsx(HttpServletResponse response, SystemQueryUserArgs args) {
    long totalCount = this.countUserByArgs(args);
    KitExcelExporter.exportByPage(response, "用户数据", SystemUserExportRowVo.class, totalCount, (long pageNum, long pageSize) -> {
      KitPageResult<SystemUserVo> pageResult = this.searchUserByArgs(args, new Page<>(pageNum, pageSize));
      List<SystemUserExportRowVo> rowVos = new ArrayList<>();
      for (SystemUserVo dataObject : pageResult.getContent()) {
        SystemUserExportRowVo rowVo = new SystemUserExportRowVo();
        rowVo.setUsername(dataObject.getUsername());
        rowVo.setRoles(dataObject.getRoles().stream().map(SystemRoleTb::getName).collect(Collectors.joining(",")));
        rowVo.setDept(dataObject.getDept().getName());
        rowVo.setJobs(dataObject.getJobs().stream().map(SystemJobTb::getName).collect(Collectors.joining(",")));
        rowVo.setEmail(dataObject.getEmail());
        rowVo.setStatus(dataObject.getEnabled() ? SystemZhConst.ENABLE_STR : SystemZhConst.DISABLE_STR);
        rowVo.setMobile(dataObject.getPhone());
        rowVo.setUpdatePwdTime(dataObject.getPwdResetTime());
        rowVo.setCreateTime(dataObject.getCreateTime());
        rowVos.add(rowVo);
      }
      return rowVos;
    });
  }
}
