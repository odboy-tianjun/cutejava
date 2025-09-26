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

package cn.odboy.system.framework.permission.config;

import cn.odboy.system.dal.redis.SystemUserOnlineInfoRedis;
import cn.odboy.system.framework.permission.core.handler.TokenFilter;
import cn.odboy.system.framework.permission.core.handler.TokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final SystemUserOnlineInfoRedis systemUserOnlineInfoRedis;

    public TokenConfigurer(TokenProvider tokenProvider, SystemUserOnlineInfoRedis systemUserOnlineInfoRedis) {
        this.tokenProvider = tokenProvider;
        this.systemUserOnlineInfoRedis = systemUserOnlineInfoRedis;
    }

    @Override
    public void configure(HttpSecurity http) {
        TokenFilter customFilter = new TokenFilter(tokenProvider, systemUserOnlineInfoRedis);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
