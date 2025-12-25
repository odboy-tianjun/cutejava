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
      return MinioClient.builder().endpoint(minio.getEndpoint())
          .credentials(minio.getAccessKey(), minio.getSecretKey()).build();
    } catch (Exception e) {
      log.error("创建Minio客户端失败", e);
      throw new BadRequestException("创建Minio客户端失败");
    }
  }
}
