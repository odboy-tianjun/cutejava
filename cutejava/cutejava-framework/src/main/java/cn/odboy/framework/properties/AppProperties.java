package cn.odboy.framework.properties;

import cn.odboy.framework.properties.model.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 应用配置
 *
 * @author odboy
 * @date 2025-04-13
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private ContentRsaEncodeSettingModel rsa;
    private JwtAuthSettingModel jwt;
    private UserLoginSettingModel login;
    private SwaggerApiDocSettingModel swagger;
    private ThreadPoolSettingModel asyncTaskPool;
    private ThreadPoolSettingModel pipelineTaskPool;
}


