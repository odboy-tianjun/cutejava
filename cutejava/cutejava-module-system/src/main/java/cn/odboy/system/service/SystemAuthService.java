package cn.odboy.system.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
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
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitRsaEncryptUtil;
import com.wf.captcha.base.Captcha;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SystemAuthService {

  @Autowired
  private KitRedisHelper redisHelper;
  @Autowired
  private SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;
  @Autowired
  private TokenProvider tokenProvider;
  @Autowired
  private AppProperties properties;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserDetailsHandler userDetailsService;

  public Map<String, Object> doLogin(SystemUserLoginArgs loginRequest, HttpServletRequest request) throws Exception {
    // 密码解密
    String password = KitRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), loginRequest.getPassword());
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
    Authentication authentication = new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // 生成令牌
    String token = tokenProvider.createToken(jwtUser);
    // 返回 token 与 用户信息
    Map<String, Object> authInfo = new HashMap<>(2) {{
      put("token", String.format("%s %s", SystemConst.TOKEN_PREFIX, token));
      put("user", KitBeanUtil.copyToClass(jwtUser, SystemUserInfoVo.class));
    }};
    if (properties.getLogin().isSingle()) {
      // 踢掉之前已经登录的token
      systemUserOnlineInfoDAO.kickOutByUsername(loginRequest.getUsername());
    }
    // 保存在线信息
    systemUserOnlineInfoDAO.saveUserJwtModelByToken(jwtUser, token, request);
    // 返回登录信息
    return authInfo;
  }

  public SystemUserJwtVo getCurrentUserInfo() {
    SystemUserJwtVo jwtUser = (SystemUserJwtVo) KitSecurityHelper.getCurrentUser();
    if (jwtUser.getUser() != null) {
      jwtUser.getUser().setPassword(null);
    }
    return jwtUser;
  }

  public Map<String, Object> getCaptchaInfo() {
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
    return new HashMap<>(2) {{
      put("img", captcha.toBase64());
      put("uuid", uuid);
    }};
  }

  public void doLogout(HttpServletRequest request) {
    String token = tokenProvider.getToken(request);
    systemUserOnlineInfoDAO.logoutByToken(token);
  }
}
