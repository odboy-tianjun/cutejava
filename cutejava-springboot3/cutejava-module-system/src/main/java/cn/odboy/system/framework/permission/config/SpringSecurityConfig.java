/*
 * Copyright 2021-2026 Odboy
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
import cn.odboy.system.framework.permission.core.handler.TokenFilter;
import cn.odboy.system.framework.permission.core.handler.TokenProvider;
import cn.odboy.util.KitAnonTagUtil;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
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
    Map<String, Set<String>> anonymousUrls = KitAnonTagUtil.getAnonymousUrl(applicationContext);

    // 创建TokenFilter实例
    TokenFilter customFilter = new TokenFilter(tokenProvider, systemUserOnlineInfoDAO);

    return httpSecurity
        // 禁用 CSRF
        .csrf(AbstractHttpConfigurer::disable).addFilter(corsFilter)
        .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
        // 授权异常
        .exceptionHandling(
            exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationErrorHandler)
                .accessDeniedHandler(jwtAccessDeniedHandler))
        // 防止iframe 造成跨域
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        // 不创建会话
        .sessionManagement(
            sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            // 静态资源等
            .requestMatchers(HttpMethod.GET, "/*.html", "/**/*.html", "/**/*.css", "/**/*.js", "/websocket/**").permitAll()
            // Knife4j 和 OpenAPI 相关路径
            .requestMatchers("/doc.html", "/webjars/**", "/v3/api-docs", "/v3/api-docs/**", "/swagger-resources",
                "/swagger-resources/**", "/configuration/ui", "/configuration/security").permitAll()
            // 文件
            .requestMatchers("/avatar/**").permitAll().requestMatchers("/file/**").permitAll()
            // 阿里巴巴 druid
            .requestMatchers("/druid/**").permitAll()
            // 放行OPTIONS请求
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            // 自定义匿名访问所有url放行：允许匿名和带Token访问，细腻化到每个 Request 类型
            // GET
            .requestMatchers(HttpMethod.GET,
                anonymousUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0])).permitAll()
            // POST
            .requestMatchers(HttpMethod.POST,
                anonymousUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0])).permitAll()
            // PUT
            .requestMatchers(HttpMethod.PUT,
                anonymousUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0])).permitAll()
            // PATCH
            .requestMatchers(HttpMethod.PATCH,
                anonymousUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0])).permitAll()
            // DELETE
            .requestMatchers(HttpMethod.DELETE,
                anonymousUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0])).permitAll()
            // 所有类型的接口都放行
            .requestMatchers(anonymousUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0])).permitAll()
            // 所有请求都需要认证
            .anyRequest().authenticated()).build();
  }
}
