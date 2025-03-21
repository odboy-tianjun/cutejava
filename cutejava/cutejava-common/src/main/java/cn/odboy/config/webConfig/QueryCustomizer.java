package cn.odboy.config.webConfig;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * 允许在查询字符串中使用特定的特殊字符
 */
@Configuration(proxyBeanMethods = false)
public class QueryCustomizer implements TomcatConnectorCustomizer {

    @Override
    public void customize(Connector connector) {
        connector.setProperty("relaxedQueryChars", "[]{}");
    }
}
