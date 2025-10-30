/*
 * Copyright 2021-2025 Odboy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.odboy.system.framework.storage.local;

import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.properties.AppProperties;
import jakarta.servlet.MultipartConfigElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import java.io.File;

@Slf4j
@Configuration
public class MultipartConfig {
    @Value("${spring.application.name}")
    private String name;
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