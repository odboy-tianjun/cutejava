package cn.odboy.system.service;

import cn.odboy.constant.SystemConst;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.system.dal.model.request.SystemUserLoginArgs;
import cn.odboy.system.dal.model.response.SystemAuthVo;
import cn.odboy.system.dal.model.response.SystemUserInfoVo;
import cn.odboy.system.dal.model.response.SystemUserJwtVo;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.system.framework.permission.core.handler.TokenProvider;
import cn.odboy.system.framework.permission.core.handler.UserDetailsHandler;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitRsaEncryptUtil;
import cn.odboy.util.KitValidUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 用户认证
 */
@Service
public class SystemAuthService {

    @Autowired
    private AppProperties properties;
    @Autowired
    private SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsHandler userDetailsService;
    @Autowired
    private SystemCaptchaService systemCaptchaService;

    /**
     * 登录 -> TestPassed
     */
    public SystemAuthVo doLogin(SystemUserLoginArgs loginArgs, HttpServletRequest request) throws Exception {
        KitValidUtil.notNull(loginArgs);

        // 密码解密
        String password =
            KitRsaEncryptUtil.decryptByPrivateKey(properties.getRsa().getPrivateKey(), loginArgs.getPassword());
        // 校验验证码
        systemCaptchaService.validate(loginArgs.getUuid(), loginArgs.getCode());
        // 获取用户信息
        SystemUserJwtVo jwtUser = userDetailsService.loadUserByUsername(loginArgs.getUsername());
        // 验证用户密码
        if (!passwordEncoder.matches(password, jwtUser.getPassword())) {
            throw new BadRequestException("登录密码错误");
        }

        // 登录验证
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(jwtUser, null, jwtUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成令牌
        String token = tokenProvider.createToken(jwtUser);
        // 返回 token 与 用户信息
        SystemAuthVo authInfo = new SystemAuthVo();
        authInfo.setToken(String.format("%s %s", SystemConst.TOKEN_PREFIX, token));
        /// 这里是为了清空密码
        authInfo.setUser(KitBeanUtil.copyToClass(jwtUser, SystemUserInfoVo.class));

        if (properties.getLogin().isSingle()) {
            // 踢掉之前已经登录的token
            systemUserOnlineInfoDAO.kickOutByUsername(loginArgs.getUsername());
        }
        // 保存在线信息
        systemUserOnlineInfoDAO.saveUserJwtModelByToken(jwtUser, token, request);
        // 返回登录信息
        return authInfo;
    }

    /**
     * 获取当前登录人信息 -> TestPassed
     */
    public SystemUserJwtVo getCurrentUserInfo() {
        SystemUserJwtVo jwtUser = (SystemUserJwtVo)KitSecurityHelper.getCurrentUser();
        if (jwtUser.getUser() != null) {
            jwtUser.getUser().setPassword(null);
        }
        return jwtUser;
    }

    /**
     * 退出登录 -> TestPassed
     */
    public void doLogout(HttpServletRequest request) {
        String token = tokenProvider.getToken(request);
        systemUserOnlineInfoDAO.logoutByToken(token);
    }
}
