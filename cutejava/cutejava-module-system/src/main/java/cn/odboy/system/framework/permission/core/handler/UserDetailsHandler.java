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
package cn.odboy.system.framework.permission.core.handler;

import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemRoleCodeVo;
import cn.odboy.system.dal.model.SystemUserJwtVo;
import cn.odboy.system.dal.redis.SystemUserInfoDAO;
import cn.odboy.system.service.SystemDataService;
import cn.odboy.system.service.SystemRoleService;
import cn.odboy.system.service.SystemUserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * TODO 通过用户名判断用户信息，并获取拥有的权限与部门
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailsHandler implements UserDetailsService {

  @Autowired
  private SystemRoleService systemRoleService;
  @Autowired
  private SystemUserService systemUserService;
  @Autowired
  private SystemDataService systemDataService;
  @Autowired
  private SystemUserInfoDAO systemUserInfoDAO;

  @Override
  public SystemUserJwtVo loadUserByUsername(String username) {
    SystemUserJwtVo userJwtVo = systemUserInfoDAO.getUserLoginInfoByUserName(username);
    if (userJwtVo == null) {
      SystemUserTb user = systemUserService.getUserByUsername(username);
      if (user == null) {
        throw new BadRequestException("用户不存在");
      } else {
        if (!user.getEnabled()) {
          throw new BadRequestException("账号未激活");
        }
        // 获取用户的权限
        List<SystemRoleCodeVo> authorities = systemRoleService.buildUserRolePermissions(user);
        // 初始化JwtUserDto
        userJwtVo = new SystemUserJwtVo(user, systemDataService.findDeptIdListByArgs(user), authorities);
        // 添加缓存数据
        systemUserInfoDAO.saveUserLoginInfoByUserName(username, userJwtVo);
      }
    }
    return userJwtVo;
  }
}
