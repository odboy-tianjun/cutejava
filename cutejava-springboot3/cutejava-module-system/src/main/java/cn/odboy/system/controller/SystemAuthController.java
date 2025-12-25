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

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.annotation.AnonymousPostMapping;
import cn.odboy.constant.CaptchaCodeEnum;
import cn.odboy.constant.SystemConst;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.redis.KitRedisHelper;
import cn.odboy.system.dal.model.SystemUserInfoVo;
import cn.odboy.system.dal.model.SystemUserJwtVo;
import cn.odboy.system.dal.model.SystemUserLoginArgs;
import cn.odboy.system.dal.redis.SystemCacheKey;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.system.framework.permission.core.handler.TokenProvider;
import cn.odboy.system.framework.permission.core.handler.UserDetailsHandler;
import cn.odboy.util.KitRsaEncryptUtil;
import com.wf.captcha.base.Captcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "系统：系统授权接口")
public class SystemAuthController {
    @Autowired private KitRedisHelper redisHelper;
    @Autowired private SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;
    @Autowired private TokenProvider tokenProvider;
    @Autowired private AppProperties properties;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserDetailsHandler userDetailsService;

    @Operation(summary = "登录授权")
    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@Validated @RequestBody SystemUserLoginArgs loginRequest,
        HttpServletRequest request) throws Exception {
        // 密码解密
        String password =
            KitRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), loginRequest.getPassword());
        // 查询验证码
        String code = redisHelper.get(loginRequest.getUuid(), String.class);
        // 清除验证码
        redisHelper.del(loginRequest.getUuid());
        if (StrUtil.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StrUtil.isBlank(loginRequest.getCode()) || !loginRequest.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        // 获取用户信息
        SystemUserJwtVo jwtUser = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        // 验证用户密码
        if (!passwordEncoder.matches(password, jwtUser.getPassword())) {
            throw new BadRequestException("登录密码错误");
        }
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成令牌
        String token = tokenProvider.createToken(jwtUser);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<>(2) {{
            put("token", String.format("%s %s", SystemConst.TOKEN_PREFIX, token));
            put("user", BeanUtil.copyProperties(jwtUser, SystemUserInfoVo.class));
        }};
        if (properties.getLogin().isSingle()) {
            // 踢掉之前已经登录的token
            systemUserOnlineInfoDAO.kickOutByUsername(loginRequest.getUsername());
        }
        // 保存在线信息
        systemUserOnlineInfoDAO.saveUserJwtModelByToken(jwtUser, token, request);
        // 返回登录信息
        return ResponseEntity.ok(authInfo);
    }

    @Operation(summary = "获取用户信息")
    @PostMapping(value = "/info")
    public ResponseEntity<SystemUserInfoVo> getUserInfo() {
        SystemUserJwtVo jwtUser = (SystemUserJwtVo)KitSecurityHelper.getCurrentUser();
        SystemUserInfoVo userInfoVo = BeanUtil.copyProperties(jwtUser, SystemUserInfoVo.class);
        return ResponseEntity.ok(userInfoVo);
    }

    @Operation(summary = "获取验证码")
    @AnonymousPostMapping(value = "/captcha/getImage")
    public ResponseEntity<Map<String, Object>> getCode() {
        // 获取运算的结果
        Captcha captcha = properties.getLogin().getCaptchaSetting().getCaptcha();
        String uuid = SystemCacheKey.CAPTCHA_LOGIN + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时, captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == CaptchaCodeEnum.ARITHMETIC.ordinal() &&
            captchaValue.contains(SystemConst.SYMBOL_DOT)) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisHelper.set(uuid, captchaValue, properties.getLogin().getCaptchaSetting().getExpiration(),
            TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @Operation(summary = "退出登录")
    @AnonymousPostMapping(value = "/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        systemUserOnlineInfoDAO.logoutByToken(token);
        return ResponseEntity.ok(null);
    }
}
