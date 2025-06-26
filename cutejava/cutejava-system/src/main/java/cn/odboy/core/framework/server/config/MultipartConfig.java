package cn.odboy.core.framework.server.config;

import cn.odboy.core.framework.server.core.FileUploadPathHelper;
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

    /**
     * 文件上传临时路径
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(fileUploadPathHelper.getFileMaxSize()));
        factory.setMaxRequestSize(DataSize.ofMegabytes(fileUploadPathHelper.getAvatarMaxSize()));
        String location = System.getProperty("user.home") + "/" + name + "/tmp";
        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            if (!tmpFile.mkdirs()) {
                log.error("创建临时文件失败");
            }
        }
        factory.setLocation(location);
        return factory.createMultipartConfig();
    }
}