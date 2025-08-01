package cn.odboy.system.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.odboy.annotation.AnonymousPostMapping;
import cn.odboy.constant.CaptchaCodeEnum;
import cn.odboy.constant.SystemConst;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.redis.CsRedisHelper;
import cn.odboy.system.dal.model.SystemUserInfoVo;
import cn.odboy.system.dal.model.SystemUserJwtVo;
import cn.odboy.system.dal.model.SystemUserLoginArgs;
import cn.odboy.system.dal.redis.SystemRedisKey;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.system.framework.permission.core.CsSecurityHelper;
import cn.odboy.system.framework.permission.core.handler.TokenProvider;
import cn.odboy.system.framework.permission.core.handler.UserDetailsHandler;
import cn.odboy.util.CsRsaEncryptUtil;
import cn.odboy.util.CsStringUtil;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "系统：系统授权接口")
public class SystemAuthController {
    @Autowired
    private CsRedisHelper redisHelper;
    @Autowired
    private SystemUserOnlineInfoDAO onlineUserService;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AppProperties properties;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsHandler userDetailsService;

    @ApiOperation("登录授权")
    @AnonymousPostMapping(value = "/login")
    public ResponseEntity<Map<String, Object>> login(@Validated @RequestBody SystemUserLoginArgs loginRequest, HttpServletRequest request) throws Exception {
        // 密码解密
        String password = CsRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), loginRequest.getPassword());
        // 查询验证码
        String code = redisHelper.get(loginRequest.getUuid(), String.class);
        // 清除验证码
        redisHelper.del(loginRequest.getUuid());
        if (CsStringUtil.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (CsStringUtil.isBlank(loginRequest.getCode()) || !loginRequest.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }
        // 获取用户信息
        SystemUserJwtVo jwtUser = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        // 验证用户密码
        if (!passwordEncoder.matches(password, jwtUser.getPassword())) {
            throw new BadRequestException("登录密码错误");
        }
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
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
            onlineUserService.kickOutByUsername(loginRequest.getUsername());
        }
        // 保存在线信息
        onlineUserService.saveUserJwtModelByToken(jwtUser, token, request);
        // 返回登录信息
        return ResponseEntity.ok(authInfo);
    }

    @ApiOperation("获取用户信息")
    @PostMapping(value = "/info")
    public ResponseEntity<SystemUserInfoVo> getUserInfo() {
        SystemUserJwtVo jwtUser = (SystemUserJwtVo)CsSecurityHelper.getCurrentUser();
        SystemUserInfoVo userInfoVo = BeanUtil.copyProperties(jwtUser, SystemUserInfoVo.class);
        return ResponseEntity.ok(userInfoVo);
    }

    @ApiOperation("获取验证码")
    @AnonymousPostMapping(value = "/captcha/getImage")
    public ResponseEntity<Map<String, Object>> getCode() {
        // 获取运算的结果
        Captcha captcha = properties.getLogin().getCaptchaSetting().getCaptcha();
        String uuid = SystemRedisKey.CAPTCHA_LOGIN + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时, captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        if (captcha.getCharType() - 1 == CaptchaCodeEnum.ARITHMETIC.ordinal() && captchaValue.contains(SystemConst.SYMBOL_DOT)) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisHelper.set(uuid, captchaValue, properties.getLogin().getCaptchaSetting().getExpiration(), TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }

    @ApiOperation("退出登录")
    @AnonymousPostMapping(value = "/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        onlineUserService.logoutByToken(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
