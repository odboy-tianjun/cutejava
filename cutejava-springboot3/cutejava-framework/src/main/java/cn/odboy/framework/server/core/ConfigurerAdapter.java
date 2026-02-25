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

package cn.odboy.framework.server.core;

import cn.odboy.framework.server.converters.DateStringArraysToListDateConverter;
import cn.odboy.framework.server.converters.DateStringToDateConverter;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring6.http.converter.FastJsonHttpMessageConverter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 跨越、静态资源映射、Http消息转换
 *
 * @author odboy
 * @date 2025-03-21
 */
@Configuration
@EnableWebMvc
public class ConfigurerAdapter implements WebMvcConfigurer {

  @Autowired
  private DateStringArraysToListDateConverter dateStringArraysToListDateConverter;
  @Autowired
  private DateStringToDateConverter dateStringToDateConverter;
  @Autowired
  private KitFileLocalUploadHelper fileUploadPathHelper;

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOriginPattern("*");
    config.addAllowedHeader("*");
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    String path = fileUploadPathHelper.getPath();
    String pathUtl = "file:" + path.replace("\\", "/");
    registry.addResourceHandler("/avatar/**").addResourceLocations(pathUtl).setCachePeriod(0);
    registry.addResourceHandler("/file/**").addResourceLocations(pathUtl).setCachePeriod(0);
    registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/").setCachePeriod(0);
  }

  /**
   * Http消息转换器
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // 添加默认的 StringHttpMessageConverter
    converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    // 配置 FastJsonHttpMessageConverter
    FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
    List<MediaType> supportMediaTypeList = new ArrayList<>();
    supportMediaTypeList.add(MediaType.APPLICATION_JSON);
    FastJsonConfig config = this.getFastJsonConfig();
    fastJsonConverter.setFastJsonConfig(config);
    fastJsonConverter.setSupportedMediaTypes(supportMediaTypeList);
    fastJsonConverter.setDefaultCharset(StandardCharsets.UTF_8);
    // 将 FastJsonHttpMessageConverter 添加到列表末尾
    converters.add(fastJsonConverter);
  }

  private FastJsonConfig getFastJsonConfig() {
    FastJsonConfig config = new FastJsonConfig();
    config.setDateFormat("yyyy-MM-dd HH:mm:ss");
    // 开启引用检测, 枚举支持
    /// 是否输出值为null的字段
    //        config.setWriterFeatures(JSONWriter.Feature.WriteMapNullValue);
    /// 字段如果为null,输出为false,而非null
    //        config.setWriterFeatures(JSONWriter.Feature.WriteNullBooleanAsFalse);
    /// 字段如果为null,输出为[],而非null
    //        config.setWriterFeatures(JSONWriter.Feature.WriteNullListAsEmpty);
    /// 字符类型字段如果为null,输出为"",而非null
    //        config.setWriterFeatures(JSONWriter.Feature.WriteNullStringAsEmpty);
    /// 太具体的数值会直接影响逻辑本身, 所以不要这个
    //        config.setWriterFeatures(JSONWriter.Feature.WriteNullNumberAsZero);
    /// 全局Long转字符串，会导致基础框架本身异常, 所以不要这个
    //        config.setWriterFeatures(JSONWriter.Feature.WriteLongAsString);
    config.setWriterFeatures(JSONWriter.Feature.WriteEnumUsingToString);
    config.setWriterFeatures(JSONWriter.Feature.ReferenceDetection);
    return config;
  }

  /**
   * 自定义时间转换器
   */
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(dateStringArraysToListDateConverter);
    registry.addConverter(dateStringToDateConverter);
  }
}
