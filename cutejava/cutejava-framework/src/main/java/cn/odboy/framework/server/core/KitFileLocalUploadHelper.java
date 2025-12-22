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
package cn.odboy.framework.server.core;

import cn.odboy.constant.SystemConst;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 简化文件上传路径配置
 *
 * @author odboy
 */
@Component
public class KitFileLocalUploadHelper {
    /**
     * 单位MB
     */
    private static final int AVATAR_MAX_SIZE = 5;
    private static final String PATH_WINDOWS = "C:\\${appName}\\file\\";
    private static final String PATH_MAC = "/Users/odboy/${appName}/file/";
    private static final String PATH_LINUX = "/home/${appName}/file/";
    @Value("${spring.application.name}") private String appName;

    public String getPath() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith(SystemConst.WIN)) {
            return PATH_WINDOWS.replace("${appName}", appName);
        } else if (os.toLowerCase().startsWith(SystemConst.MAC)) {
            return PATH_MAC.replace("${appName}", appName);
        }
        return PATH_LINUX.replace("${appName}", appName);
    }

    public int getAvatarMaxSize() {
        return AVATAR_MAX_SIZE;
    }
}
