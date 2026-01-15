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
package cn.odboy.framework.mybatisplus.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties;
import com.alibaba.druid.util.Utils;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnWebApplication
@AutoConfigureAfter(DruidDataSourceAutoConfigure.class)
@ConditionalOnProperty(name = "spring.datasource.druid.stat-view-servlet.enabled", havingValue = "true",
    matchIfMissing = true)
public class RemoveDruidAdConfig {

  /**
   * 方法名: removeDruidAdFilterRegistrationBean 方法描述 除去页面底部的广告
   *
   * @param properties com.alibaba.druid.spring.boot.autoconfigure.properties.DruidStatProperties
   * @return org.springframework.boot.web.servlet.FilterRegistrationBean
   */
  @Bean
  public FilterRegistrationBean<Filter> removeDruidAdFilterRegistrationBean(DruidStatProperties properties) {
    // 获取web监控页面的参数
    DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
    // 提取common.js的配置路径
    String pattern = config.getUrlPattern() != null ? config.getUrlPattern() : "/druid/*";
    String commonJsPattern = pattern.replaceAll("\\*", "js/common.js");
    final String filePath = "support/http/resources/js/common.js";
    //创建filter进行过滤
    Filter filter = new Filter() {
      @Override
      public void init(FilterConfig filterConfig) throws ServletException {
      }

      @Override
      public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (httpRequest.getRequestURI().endsWith("js/common.js")) {
          // 获取common.js
          String text = Utils.readFromResource(filePath);
          // 正则替换banner, 除去底部的广告信息
          text = text.replaceAll("<a.*?druid_banner\"></a><br/>", "");
          text = text.replaceAll("powered by.*?shrek.wang</a>", "");
          httpResponse.setContentType("application/javascript");
          httpResponse.setCharacterEncoding("UTF-8");
          httpResponse.getWriter().write(text);
        } else {
          chain.doFilter(request, response);
        }
      }

      @Override
      public void destroy() {
      }
    };
    FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(filter);
    registrationBean.addUrlPatterns(commonJsPattern);
    return registrationBean;
  }
}
