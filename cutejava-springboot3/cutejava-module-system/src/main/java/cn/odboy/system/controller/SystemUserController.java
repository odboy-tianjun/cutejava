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
import cn.odboy.base.KitSelectOptionVo;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.SystemQueryUserArgs;
import cn.odboy.system.dal.model.SystemUpdateUserPasswordArgs;
import cn.odboy.system.dal.model.SystemUserVo;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.system.service.SystemDataService;
import cn.odboy.system.service.SystemDeptService;
import cn.odboy.system.service.SystemUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import jakarta.servlet.http.HttpServletResponse;
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

@Api(tags = "系统：用户管理")
@RestController
@RequestMapping("/api/user")
public class SystemUserController {

  @Autowired
  private SystemUserService systemUserService;
  @Autowired
  private SystemDataService systemDataService;
  @Autowired
  private SystemDeptService systemDeptService;


  @ApiOperation("导出用户数据")
  @GetMapping(value = "/download")
  @PreAuthorize("@el.check('user:list')")
  public void exportUserExcel(HttpServletResponse response, SystemQueryUserArgs args) throws IOException {
    systemUserService.exportUserExcel(systemUserService.queryUserByArgs(args), response);
  }

  @ApiOperation("查询用户")
  @PostMapping
  @PreAuthorize("@el.check('user:list')")
  public ResponseEntity<KitPageResult<SystemUserVo>> queryUserByArgs(@Validated @RequestBody KitPageArgs<SystemQueryUserArgs> pageArgs) {
    String currentUsername = KitSecurityHelper.getCurrentUsername();
    Page<SystemUserTb> page = new Page<>(pageArgs.getPage(), pageArgs.getSize());
    return ResponseEntity.ok(systemUserService.aggregationSearchUserByArgs(page, pageArgs.getArgs(), currentUsername));
  }

  @ApiOperation("新增用户")
  @PostMapping(value = "/saveUser")
  @PreAuthorize("@el.check('user:add')")
  public ResponseEntity<Object> saveUser(@Validated @RequestBody SystemUserVo args) {
    systemUserService.saveUser(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("修改用户")
  @PostMapping(value = "/modifyUserById")
  @PreAuthorize("@el.check('user:edit')")
  public ResponseEntity<Object> modifyUserById(@Validated(SystemUserTb.Update.class) @RequestBody SystemUserVo args) {
    systemUserService.updateUserById(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("修改用户：个人中心")
  @PostMapping(value = "modifyUserCenterInfoById")
  public ResponseEntity<Object> modifyUserCenterInfoById(@Validated(SystemUserTb.Update.class) @RequestBody SystemUserTb args) {
    systemUserService.updateUserCenterInfoById(args);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("删除用户")
  @PostMapping(value = "/removeUserByIds")
  @PreAuthorize("@el.check('user:del')")
  public ResponseEntity<Object> removeUserByIds(@RequestBody Set<Long> ids) {
    systemUserService.removeUserByIds(ids);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("修改密码")
  @PostMapping(value = "/modifyUserPasswordByUsername")
  public ResponseEntity<Object> modifyUserPasswordByUsername(@RequestBody SystemUpdateUserPasswordArgs passVo) throws Exception {
    String currentUsername = KitSecurityHelper.getCurrentUsername();
    systemUserService.updateUserPasswordByUsername(currentUsername, passVo);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("重置密码")
  @PostMapping(value = "/resetUserPasswordByIds")
  public ResponseEntity<Object> resetUserPasswordByIds(@RequestBody Set<Long> ids) {
    systemUserService.resetUserPasswordByIds(ids);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("修改头像")
  @PostMapping(value = "/modifyUserAvatar")
  public ResponseEntity<Object> modifyUserAvatar(@RequestParam MultipartFile avatar) {
    return ResponseEntity.ok(systemUserService.updateUserAvatar(avatar));
  }

  @ApiOperation("修改邮箱")
  @PostMapping(value = "/modifyUserEmailByUsername/{code}")
  public ResponseEntity<Object> modifyUserEmailByUsername(@PathVariable String code, @RequestBody SystemUserTb args) throws Exception {
    String currentUsername = KitSecurityHelper.getCurrentUsername();
    systemUserService.updateUserEmailByUsername(currentUsername, args, code);
    return ResponseEntity.ok(null);
  }

  @ApiOperation("查询用户基础数据")
  @PostMapping(value = "/queryUserMetadataOptions")
  @PreAuthorize("@el.check('user:list')")
  public ResponseEntity<List<KitSelectOptionVo>> queryUserMetadataOptions(@Validated @RequestBody KitPageArgs<SystemQueryUserArgs> args) {
    List<KitSelectOptionVo> collect = systemUserService.queryUserMetadataOptions(args);
    return ResponseEntity.ok(collect);
  }
}
