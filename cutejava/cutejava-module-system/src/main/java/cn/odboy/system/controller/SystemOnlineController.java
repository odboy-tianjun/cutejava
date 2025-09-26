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

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResult;
import cn.odboy.system.dal.model.SystemUserOnlineVo;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoRedis;
import cn.odboy.util.CsDesEncryptUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/user/online")
@Api(tags = "系统：在线用户管理")
public class SystemOnlineController {
    @Autowired
    private SystemUserOnlineInfoRedis systemUserOnlineInfoRedis;

    @ApiOperation("查询在线用户")
    @PostMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<CsPageResult<SystemUserOnlineVo>> queryOnlineUser(@Validated @RequestBody CsPageArgs<SystemUserOnlineVo> args) {
        IPage<SystemUserOnlineVo> page = new Page<>(args.getPage(), args.getSize());
        return new ResponseEntity<>(
                systemUserOnlineInfoRedis.queryUserOnlineModelPage(args.getArgs(), page),
                HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check()")
    public void exportOnlineUser(HttpServletResponse response, String username) throws IOException {
        systemUserOnlineInfoRedis.downloadUserOnlineModelExcel(systemUserOnlineInfoRedis.queryUserOnlineModelListByUsername(
                username), response);
    }

    @ApiOperation("踢出用户")
    @PostMapping(value = "/kickOutUser")
    @PreAuthorize("@el.check()")
    public ResponseEntity<Void> kickOutUser(@RequestBody Set<String> keys) throws Exception {
        for (String token : keys) {
            // 解密Key
            token = CsDesEncryptUtil.desDecrypt(token);
            systemUserOnlineInfoRedis.logoutByToken(token);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
