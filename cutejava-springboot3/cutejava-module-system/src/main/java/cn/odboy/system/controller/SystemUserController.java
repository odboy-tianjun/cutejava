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
package cn.odboy.system.controller;

import cn.odboy.base.KitPageArgs;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.request.SystemQueryUserArgs;
import cn.odboy.system.dal.model.request.SystemUpdateUserPasswordArgs;
import cn.odboy.system.dal.model.response.SystemUserJwtVo;
import cn.odboy.system.dal.model.response.SystemUserVo;
import cn.odboy.system.framework.operalog.OperationLog;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.system.service.SystemAuthService;
import cn.odboy.system.service.SystemUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Set;

@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/api/user")
public class SystemUserController {

  @Autowired
  private SystemUserService systemUserService;
  @Autowired
  private SystemAuthService systemAuthService;

  @OperationLog
  @ApiOperation("导出用户数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('user:list')")
  public void exportUserExcel(HttpServletResponse response, SystemQueryUserArgs args) {
    systemUserService.exportUserXlsx(response, args);
  }

  @ApiOperation("查询用户")
  @PostMapping(value = "/aggregationSearchUser")
  @PreAuthorize("@el.check('user:list')")
  public ResponseEntity<KitPageResult<SystemUserVo>> queryUserByArgs(@Validated @RequestBody KitPageArgs<SystemQueryUserArgs> pageArgs) {
    String currentUsername = KitSecurityHelper.getCurrentUsername();
    Page<SystemUserTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemUserService.aggregationSearchUserByArgs(page, pageArgs.getArgs(), currentUsername));
  }

  @OperationLog
  @ApiOperation("新增用户")
  @PostMapping(value = "/saveUser")
  @PreAuthorize("@el.check('user:add')")
  public ResponseEntity<Object> saveUser(@Validated @RequestBody SystemUserVo args) {
    systemUserService.saveUser(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改用户数据")
  @PostMapping(value = "/updateUserById")
  @PreAuthorize("@el.check('user:edit')")
  public ResponseEntity<Object> updateUserById(@Validated(SystemUserTb.Update.class) @RequestBody SystemUserVo args) {
    systemUserService.updateUserById(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改个人中心信息")
  @PostMapping(value = "/updateUserCenterInfoById")
  public ResponseEntity<Object> updateUserCenterInfoById(@Validated(SystemUserTb.Update.class) @RequestBody SystemUserTb args) {
    systemUserService.updateUserCenterInfoById(args);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("删除用户")
  @PostMapping(value = "/deleteUserByIds")
  @PreAuthorize("@el.check('user:del')")
  public ResponseEntity<Object> deleteUserByIds(@RequestBody Set<Long> ids) {
    systemUserService.deleteUserByIds(ids);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改用户密码")
  @PostMapping(value = "/updateUserPasswordByUsername")
  public ResponseEntity<Object> updateUserPasswordByUsername(@RequestBody SystemUpdateUserPasswordArgs passVo) throws Exception {
    String currentUsername = KitSecurityHelper.getCurrentUsername();
    systemUserService.updateUserPasswordByUsername(currentUsername, passVo);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("重置用户密码")
  @PostMapping(value = "/resetUserPasswordByIds")
  public ResponseEntity<Object> resetUserPasswordByIds(@RequestBody Set<Long> ids) {
    systemUserService.resetUserPasswordByIds(ids);
    return ResponseEntity.ok(null);
  }

  @OperationLog
  @ApiOperation("修改用户头像")
  @PostMapping(value = "/updateUserAvatar")
  public ResponseEntity<Object> updateUserAvatar(@RequestParam MultipartFile avatar) {
    return ResponseEntity.ok(systemUserService.updateUserAvatar(avatar));
  }

  @OperationLog
  @ApiOperation("修改用户邮箱")
  @PostMapping(value = "/updateUserEmailByUsername/{code}")
  public ResponseEntity<Object> updateUserEmailByUsername(@PathVariable String code, @RequestBody SystemUserTb args) throws Exception {
    String currentUsername = KitSecurityHelper.getCurrentUsername();
    systemUserService.updateUserEmailByUsername(currentUsername, args, code);
    return ResponseEntity.ok(null);
  }


  @ApiOperation("获取用户信息")
  @PostMapping(value = "/getCurrentUserInfo")
  public ResponseEntity<SystemUserJwtVo> getUserInfo() {
    SystemUserJwtVo userInfoVo = systemAuthService.getCurrentUserInfo();
    return ResponseEntity.ok(userInfoVo);
  }
}
