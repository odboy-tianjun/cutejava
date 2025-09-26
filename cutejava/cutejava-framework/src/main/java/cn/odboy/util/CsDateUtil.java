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

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.experimental.UtilityClass;

import java.util.Date;

@UtilityClass
public class CsDateUtil {
    /**
     * 获取当前时间毫秒数
     *
     * @return /
     */
    public static String getNowDateTimeMsStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_MS_PATTERN);
    }

    /**
     * 获取当前时间,但不包括毫秒数
     *
     * @return /
     */
    public static String getNowDateTimeStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
    }

    /**
     * 获取当前时间,但不包括毫秒数
     *
     * @return /
     */
    public static String getNowDateStr() {
        return DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN);
    }
}
