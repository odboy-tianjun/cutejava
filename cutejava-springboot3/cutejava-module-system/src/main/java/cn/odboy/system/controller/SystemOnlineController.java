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
package cn.odboy.system.controller;

import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.model.SystemUserOnlineVo;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.util.KitDesEncryptUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.Set;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user/online")
@Api(tags = "系统：在线用户管理")
public class SystemOnlineController {

  @Autowired
  private SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;

  @ApiOperation("查询在线用户")
  @PostMapping
  @PreAuthorize("@el.check()")
  public ResponseEntity<KitPageResult<SystemUserOnlineVo>> queryOnlineUser(
      @Validated @RequestBody KitPageArgs<SystemUserOnlineVo> args) {
    IPage<SystemUserOnlineVo> page = new Page<>(args.getPage(), args.getSize());
    return ResponseEntity.ok(systemUserOnlineInfoDAO.queryUserOnlineModelPage(args.getArgs(), page));
  }

  @ApiOperation("导出数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check()")
  public void exportOnlineUser(HttpServletResponse response, String username) throws IOException {
    systemUserOnlineInfoDAO.downloadUserOnlineModelExcel(
        systemUserOnlineInfoDAO.queryUserOnlineModelListByUsername(username), response);
  }

  @ApiOperation("踢出用户")
  @PostMapping(value = "/kickOutUser")
  @PreAuthorize("@el.check()")
  public ResponseEntity<Void> kickOutUser(@RequestBody Set<String> keys) throws Exception {
    for (String token : keys) {
      // 解密Key
      token = KitDesEncryptUtil.desDecrypt(token);
      systemUserOnlineInfoDAO.logoutByToken(token);
    }
    return ResponseEntity.ok(null);
  }
}
