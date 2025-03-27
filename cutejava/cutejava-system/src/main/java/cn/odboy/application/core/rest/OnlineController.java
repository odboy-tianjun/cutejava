package cn.odboy.application.core.rest;

import cn.odboy.application.core.service.UserOnlineService;
import cn.odboy.base.PageResult;
import cn.odboy.model.system.dto.UserOnlineDto;
import cn.odboy.util.DESEncryptUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/online")
@Api(tags = "系统：在线用户管理")
public class OnlineController {
    private final UserOnlineService userOnlineService;

    @ApiOperation("查询在线用户")
    @GetMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<PageResult<UserOnlineDto>> queryOnlineUser(String username, Pageable pageable) {
        return new ResponseEntity<>(userOnlineService.queryOnlineUserPage(username, pageable), HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check()")
    public void exportOnlineUser(HttpServletResponse response, String username) throws IOException {
        userOnlineService.downloadExcel(userOnlineService.selectOnlineUserByUsername(username), response);
    }

    @ApiOperation("踢出用户")
    @DeleteMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> deleteOnlineUser(@RequestBody Set<String> keys) throws Exception {
        for (String token : keys) {
            // 解密Key
            token = DESEncryptUtil.desDecrypt(token);
            userOnlineService.logoutByToken(token);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
