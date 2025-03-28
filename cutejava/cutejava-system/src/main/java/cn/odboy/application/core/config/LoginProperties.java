package cn.odboy.application.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 登录配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "login")
public class LoginProperties {

    /**
     * 账号单用户 登录
     */
    private boolean singleLogin = false;
}
