package cn.odboy.config;

import cn.odboy.config.model.ContentRsaEncodeSettingModel;
import cn.odboy.config.model.FileUploadSettingModel;
import cn.odboy.config.model.JwtAuthSettingModel;
import cn.odboy.config.model.QuartzTaskThreadPoolSettingModel;
import cn.odboy.config.model.SwaggerApiDocSettingModel;
import cn.odboy.config.model.UserLoginSettingModel;
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
    private FileUploadSettingModel file;
    private ContentRsaEncodeSettingModel rsa;
    private JwtAuthSettingModel jwt;
    private UserLoginSettingModel login;
    private SwaggerApiDocSettingModel swagger;
    private QuartzTaskThreadPoolSettingModel asyncTaskPool;
}


