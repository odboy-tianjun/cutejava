package cn.odboy.framework.server.core;

import cn.odboy.framework.server.converters.DateStringArraysToListDateConverter;
import cn.odboy.framework.server.converters.DateStringToDateConverter;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import lombok.RequiredArgsConstructor;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class ConfigurerAdapter implements WebMvcConfigurer {
    private final DateStringArraysToListDateConverter dateStringArraysToListDateConverter;
    private final DateStringToDateConverter dateStringToDateConverter;
    private final FileUploadPathHelper fileUploadPathHelper;

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
        FastJsonConfig config = new FastJsonConfig();
        config.setDateFormat("yyyy-MM-dd HH:mm:ss");
        // 开启引用检测, 枚举支持
        config.setWriterFeatures(
                // 是否输出值为null的字段
                // SerializerFeature.WriteMapNullValue,
                // 字段如果为null,输出为false,而非null
//                JSONWriter.Feature.WriteNullBooleanAsFalse,
                // 字段如果为null,输出为[],而非null
//                JSONWriter.Feature.WriteNullListAsEmpty,
                // 字符类型字段如果为null,输出为"",而非null
//                JSONWriter.Feature.WriteNullStringAsEmpty,
                // 太具体的数值会直接影响逻辑本身, 所以不要这个
                // SerializerFeature.WriteNullNumberAsZero,
                JSONWriter.Feature.WriteEnumUsingToString,
//                SerializerFeature.DisableCircularReferenceDetect,
                JSONWriter.Feature.ReferenceDetection);
        fastJsonConverter.setFastJsonConfig(config);
        fastJsonConverter.setSupportedMediaTypes(supportMediaTypeList);
        fastJsonConverter.setDefaultCharset(StandardCharsets.UTF_8);
        // 将 FastJsonHttpMessageConverter 添加到列表末尾
        converters.add(fastJsonConverter);
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
