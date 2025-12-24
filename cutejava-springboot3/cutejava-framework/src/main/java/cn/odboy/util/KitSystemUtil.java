
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

package cn.odboy.util;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

/**
 * 系统层相关
 *
 * @author odboy
 * @date 2025-08-11
 */
public class KitSystemUtil {
    public static int getCpuCount() {
        SystemInfo systemInfo = new SystemInfo();
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        return processor.getPhysicalProcessorCount();
    }

    /**
     * 获取当前系统的用户名
     */
    public static String getCurrentUserName() {
        return System.getProperty("user.name");
    }
}
