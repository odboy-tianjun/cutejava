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
    private CaptchaModel captcha;
    private StorageOSSModel oss;
}


