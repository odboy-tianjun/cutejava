package cn.odboy.context;

import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

/**
 * 主要目的是输出应用的服务地址
 *
 * @author odboy
 * @date 2024-04-20
 */
@Slf4j
public class BootApplication {
    @SneakyThrows
    protected static void initd(ConfigurableApplicationContext application) {
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = StrUtil.isEmpty(env.getProperty("config.servlet.context-path")) ? "" : env.getProperty("config.servlet.context-path");
        log.info(
                "\n----------------------------------------------------------\n\t"
                        + "Application is running! Access URLs:\n\t"
                        + "Local: \t\thttp://localhost:" + port + path + "/\n\t"
                        + "External: \thttp://" + ip + ":" + port + path + "/\n\t"
                        + "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n"
                        + "----------------------------------------------------------");
    }

    @Bean
    public SpringBeanHolder springContextHolder() {
        return new SpringBeanHolder();
    }
}