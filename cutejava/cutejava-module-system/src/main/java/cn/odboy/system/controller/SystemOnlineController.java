package cn.odboy.system.controller;

import cn.odboy.base.CsPageArgs;
import cn.odboy.base.CsPageResultVo;
import cn.odboy.system.dal.model.SystemUserOnlineVo;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.util.CsDesEncryptUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/online")
@Api(tags = "系统：在线用户管理")
public class SystemOnlineController {
    private final SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;

    @ApiOperation("查询在线用户")
    @PostMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<CsPageResultVo<List<SystemUserOnlineVo>>> queryOnlineUser(@Validated @RequestBody CsPageArgs<SystemUserOnlineVo> args) {
        IPage<SystemUserOnlineVo> page = new Page<>(args.getPage(), args.getSize());
        return new ResponseEntity<>(systemUserOnlineInfoDAO.queryUserOnlineModelPage(args.getArgs(), page), HttpStatus.OK);
    }

    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check()")
    public void exportOnlineUser(HttpServletResponse response, String username) throws IOException {
        systemUserOnlineInfoDAO.downloadUserOnlineModelExcel(systemUserOnlineInfoDAO.queryUserOnlineModelListByUsername(username), response);
    }

    @ApiOperation("踢出用户")
    @PostMapping(value = "/kickOutUser")
    @PreAuthorize("@el.check()")
    public ResponseEntity<Void> kickOutUser(@RequestBody Set<String> keys) throws Exception {
        for (String token : keys) {
            // 解密Key
            token = CsDesEncryptUtil.desDecrypt(token);
            systemUserOnlineInfoDAO.logoutByToken(token);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
