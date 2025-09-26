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

package cn.odboy.system.framework.permission.core.handler;

import cn.hutool.core.util.StrUtil;
import cn.odboy.constant.SystemConst;
import cn.odboy.system.dal.model.SystemUserOnlineVo;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoRedis;
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
    private final SystemUserOnlineInfoRedis systemUserOnlineInfoRedis;

    /**
     * @param tokenProvider             Token
     * @param systemUserOnlineInfoRedis 用户在线
     */
    public TokenFilter(TokenProvider tokenProvider, SystemUserOnlineInfoRedis systemUserOnlineInfoRedis) {
        this.systemUserOnlineInfoRedis = systemUserOnlineInfoRedis;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = resolveToken(httpServletRequest);
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            // 获取用户Token的Key
            String loginKey = tokenProvider.loginKey(token);
            SystemUserOnlineVo userOnlineVo = systemUserOnlineInfoRedis.queryUserOnlineModelByKey(loginKey);
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
