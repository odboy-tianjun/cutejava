package cn.odboy.application.core.context;

import cn.hutool.core.util.StrUtil;
import cn.odboy.application.core.service.impl.UserOnlineServiceImpl;
import cn.odboy.constant.SystemConst;
import cn.odboy.model.system.model.UserOnlineVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
public class TokenFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private final UserOnlineServiceImpl onlineUserService;

    /**
     * @param tokenProvider     Token
     * @param onlineUserService 用户在线
     */
    public TokenFilter(TokenProvider tokenProvider, UserOnlineServiceImpl onlineUserService) {
        this.onlineUserService = onlineUserService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            // 获取用户Token的Key
            String loginKey = tokenProvider.loginKey(token);
            UserOnlineVo userOnlineVo = onlineUserService.describeUserOnlineModelByKey(loginKey);
            // 判断用户在线信息是否为空
            if (userOnlineVo != null) {
                // Token 续期判断
                tokenProvider.checkRenewal(token);
                // 获取认证信息，设置上下文
                Authentication authentication = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(SystemConst.HEADER_NAME);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SystemConst.TOKEN_PREFIX)) {
            // 去掉令牌前缀
            return bearerToken.replace(SystemConst.TOKEN_PREFIX + " ", "");
        } else {
            log.debug("非法Token：{}", bearerToken);
        }
        return null;
    }
}
