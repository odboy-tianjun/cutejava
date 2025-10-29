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

import cn.odboy.constant.RequestMethodEnum;
import cn.odboy.system.dal.redis.SystemUserOnlineInfoDAO;
import cn.odboy.system.framework.permission.core.handler.JwtAccessDeniedHandler;
import cn.odboy.system.framework.permission.core.handler.JwtAuthenticationEntryPoint;
import cn.odboy.system.framework.permission.core.handler.TokenProvider;
import cn.odboy.util.CsAnonTagUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

import java.util.Map;
import java.util.Set;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecurityConfig {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private CorsFilter corsFilter;
    @Autowired
    private JwtAuthenticationEntryPoint authenticationErrorHandler;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private SystemUserOnlineInfoDAO systemUserOnlineInfoDAO;

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // 去除 ROLE_ 前缀
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 密码加密方式
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // 获取匿名标记
        Map<String, Set<String>> anonymousUrls = CsAnonTagUtil.getAnonymousUrl(applicationContext);
        return httpSecurity
                // 禁用 CSRF
                .csrf().disable().addFilter(corsFilter)
                // 授权异常
                .exceptionHandling().authenticationEntryPoint(authenticationErrorHandler).accessDeniedHandler(jwtAccessDeniedHandler)
                // 防止iframe 造成跨域
                .and().headers().frameOptions().disable()
                // 不创建会话
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                // 静态资源等等
                .antMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/websocket/**").permitAll()
                // swagger 文档
                .antMatchers("/swagger-ui.html").permitAll().antMatchers("/swagger-resources/**").permitAll().antMatchers("/webjars/**").permitAll().antMatchers("/*/api-docs").permitAll()
                // 文件
                .antMatchers("/avatar/**").permitAll().antMatchers("/file/**").permitAll()
                // 阿里巴巴 druid
                .antMatchers("/druid/**").permitAll()
                // 放行OPTIONS请求
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 自定义匿名访问所有url放行：允许匿名和带Token访问，细腻化到每个 Request 类型
                // GET
                .antMatchers(HttpMethod.GET, anonymousUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0])).permitAll()
                // POST
                .antMatchers(HttpMethod.POST, anonymousUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0])).permitAll()
                // PUT
                .antMatchers(HttpMethod.PUT, anonymousUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0])).permitAll()
                // PATCH
                .antMatchers(HttpMethod.PATCH, anonymousUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0])).permitAll()
                // DELETE
                .antMatchers(HttpMethod.DELETE, anonymousUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0])).permitAll()
                // 所有类型的接口都放行
                .antMatchers(anonymousUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0])).permitAll()
                // 所有请求都需要认证
                .anyRequest().authenticated().and().apply(securityConfigurerAdapter()).and().build();
    }

    private TokenConfigurer securityConfigurerAdapter() {
        return new TokenConfigurer(tokenProvider, systemUserOnlineInfoDAO);
    }
}
