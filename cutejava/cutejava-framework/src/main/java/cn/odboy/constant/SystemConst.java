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
package cn.odboy.constant;

/**
 * 常用静态常量
 */
public interface SystemConst {
    /**
     * win 系统
     */
    String WIN = "win";
    /**
     * mac 系统
     */
    String MAC = "mac";
    /**
     * 请求头名称
     */
    String HEADER_NAME = "Authorization";
    /**
     * Token前缀
     */
    String TOKEN_PREFIX = "Bearer";
    /**
     * 小数点
     */
    String SYMBOL_DOT = ".";
    String SYMBOL_EQUAL = "=";
    String SYMBOL_ADD = "+";
    String SYMBOL_SUBTRACT = "-";
    String SYMBOL_AT = "@";
    String PROPERTY_OS_NAME = "os.name";
    String OS_NAME_WINDOWS = "Windows";
}
