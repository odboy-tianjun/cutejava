package cn.odboy.system.framework.storage;

import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import cn.odboy.framework.properties.model.OSSConfigModel;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OssStorageConfiguration {
    @Autowired
    private AppProperties properties;

    @Bean
    public MinioClient minioClient() {
        try {
            OSSConfigModel minio = properties.getOss().getMinio();
            return MinioClient.builder().endpoint(minio.getEndpoint()).credentials(minio.getAccessKey(), minio.getSecretKey()).build();
        } catch (Exception e) {
            log.error("创建Minio客户端失败", e);
            throw new BadRequestException("创建Minio客户端失败");
        }
    }
}
