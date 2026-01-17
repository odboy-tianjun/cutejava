/*
 * Copyright 2021-2026 Odboy
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
package cn.odboy.system.dal.redis;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.framework.redis.KitRedisHelper;
import cn.odboy.system.constant.SystemCacheKey;
import cn.odboy.system.dal.model.response.SystemUserJwtVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用户缓存管理
 */
@Component
public class SystemUserInfoDAO {

  @Autowired
  private KitRedisHelper redisHelper;

  /**
   * 添加缓存到Redis
   *
   * @param userName 用户名
   */
  @Async
  public void saveUserLoginInfoByUserName(String userName, SystemUserJwtVo user) {
    if (StrUtil.isNotEmpty(userName)) {
      // 添加数据, 避免数据同时过期（2小时左右）
      long time = 7200 + RandomUtil.randomInt(900, 1800);
      redisHelper.set(SystemCacheKey.USER_INFO + userName, user, time);
    }
  }

  /**
   * 清理用户缓存信息 用户信息变更时
   *
   * @param userName 用户名
   */
  @Async
  public void deleteUserLoginInfoByUserName(String userName) {
    if (StrUtil.isNotEmpty(userName)) {
      // 清除数据
      redisHelper.del(SystemCacheKey.USER_INFO + userName);
    }
  }

  /**
   * 返回用户缓存
   *
   * @param username 用户名
   * @return UserJwtVo
   */
  public SystemUserJwtVo getUserLoginInfoByUserName(String username) {
    if (StrUtil.isNotEmpty(username)) {
      // 获取数据
      return redisHelper.get(SystemCacheKey.USER_INFO + username, SystemUserJwtVo.class);
    }
    return null;
  }
}
