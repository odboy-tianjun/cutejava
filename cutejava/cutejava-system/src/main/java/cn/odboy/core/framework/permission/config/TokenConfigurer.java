package cn.odboy.core.framework.permission.config;

import cn.odboy.core.dal.redis.system.SystemUserOnlineApi;
import cn.odboy.core.framework.permission.core.handler.TokenFilter;
import cn.odboy.core.framework.permission.core.handler.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final SystemUserOnlineApi systemUserOnlineApi;

    @Override
    public void configure(HttpSecurity http) {
        TokenFilter customFilter = new TokenFilter(tokenProvider, systemUserOnlineApi);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
