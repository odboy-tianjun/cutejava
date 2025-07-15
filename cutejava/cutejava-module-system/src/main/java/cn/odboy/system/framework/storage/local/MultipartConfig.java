package cn.odboy.system.framework.storage.local;

import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.server.core.FileUploadPathHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.File;


@Slf4j
@Configuration
public class MultipartConfig {
    @Value("${spring.application.name}")
    private String name;
    @Autowired
    private FileUploadPathHelper fileUploadPathHelper;
    @Autowired
    private AppProperties properties;

    /**
     * 文件上传临时路径
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(properties.getOss().getMaxSize()));
        factory.setMaxRequestSize(DataSize.ofMegabytes(properties.getOss().getMaxSize()));
        String location = System.getProperty("user.home") + "/" + name + "/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            if (!tmpFile.mkdirs()) {
                log.error("创建临时文件失败");
                throw new BadRequestException("创建临时文件失败");
            }
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}