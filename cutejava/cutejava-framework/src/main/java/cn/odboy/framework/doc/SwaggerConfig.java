package cn.odboy.framework.doc;

import cn.odboy.constant.SystemConst;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.util.AnonTagUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spring.web.plugins.WebFluxRequestHandlerProvider;
import springfox.documentation.spring.web.plugins.WebMvcRequestHandlerProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * api页面 /doc.html
 */
@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {
    @Value("${server.servlet.context-path:}")
    private String apiPath;
    private final AppProperties properties;
    private final ApplicationContext applicationContext;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .enable(properties.getSwagger().getEnabled())
                .pathMapping("/")
                .apiInfo(apiInfo())
                .select()
                .paths(PathSelectors.regex("^(?!/error).*"))
                .paths(PathSelectors.any())
                .build()
                //添加登陆认证
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("一个简单且易上手的 Spring boot 后台管理框架")
                .title("CuteJava 接口文档")
                .version("1.4.1")
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        //设置请求头信息
        List<SecurityScheme> securitySchemes = new ArrayList<>();
        ApiKey apiKey = new ApiKey(SystemConst.HEADER_NAME, SystemConst.HEADER_NAME, "header");
        securitySchemes.add(apiKey);
        return securitySchemes;
    }

    private List<SecurityContext> securityContexts() {
        //设置需要登录认证的路径
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(getContextByPath());
        return securityContexts;
    }

    private SecurityContext getContextByPath() {
        Set<String> urls = AnonTagUtil.getAllAnonymousUrl(applicationContext);
        urls = urls.stream().filter(url -> !"/".equals(url)).collect(Collectors.toSet());
        String regExp = "^(?!" + apiPath + String.join("|" + apiPath, urls) + ").*$";
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(o -> o.requestMappingPattern()
                        // 排除不需要认证的接口
                        .matches(regExp))
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> securityReferences = new ArrayList<>();
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        securityReferences.add(new SecurityReference(SystemConst.HEADER_NAME, authorizationScopes));
        return securityReferences;
    }

    /**
     * 解决Springfox与SpringBoot集成后, WebMvcRequestHandlerProvider和WebFluxRequestHandlerProvider冲突问题
     *
     * @return /
     */
    @Bean
    @SuppressWarnings({"unchecked", "all"})
    public static BeanPostProcessor springfoxHandlerProviderBeanPostProcessor() {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                if (bean instanceof WebMvcRequestHandlerProvider || bean instanceof WebFluxRequestHandlerProvider) {
                    customizeSpringfoxHandlerMappings(getHandlerMappings(bean));
                }
                return bean;
            }

            private <T extends RequestMappingInfoHandlerMapping> void customizeSpringfoxHandlerMappings(List<T> mappings) {
                List<T> filteredMappings = mappings.stream()
                        .filter(mapping -> mapping.getPatternParser() == null)
                        .collect(Collectors.toList());
                mappings.clear();
                mappings.addAll(filteredMappings);
            }

            private List<RequestMappingInfoHandlerMapping> getHandlerMappings(Object bean) {
                Field field = ReflectionUtils.findField(bean.getClass(), "handlerMappings");
                if (field != null) {
                    field.setAccessible(true);
                    try {
                        return (List<RequestMappingInfoHandlerMapping>) field.get(bean);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Failed to access handlerMappings field", e);
                    }
                }
                return Collections.emptyList();
            }
        };
    }
}
