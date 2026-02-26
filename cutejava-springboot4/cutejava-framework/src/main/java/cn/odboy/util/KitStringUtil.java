/*
 * Copyright 2021-2026 Odboy
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

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 */
@Slf4j
@UtilityClass
public final class KitStringUtil {

  private static final char SEPARATOR = '_';

  /**
   * 驼峰命名法工具
   *
   * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
   */
  public static String toCamelCase(String s) {
    if (s == null) {
      return null;
    }
    s = s.toLowerCase();
    StringBuilder sb = new StringBuilder(s.length());
    boolean upperCase = false;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == SEPARATOR) {
        upperCase = true;
      } else if (upperCase) {
        sb.append(Character.toUpperCase(c));
        upperCase = false;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * 驼峰命名法工具
   *
   * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
   */
  public static String toCapitalizeCamelCase(String s) {
    if (s == null) {
      return null;
    }
    s = toCamelCase(s);
    return s.substring(0, 1).toUpperCase() + s.substring(1);
  }

  /**
   * 驼峰命名法工具
   *
   * @return toCamelCase(" hello_world ") == "helloWorld" toCapitalizeCamelCase("hello_world") == "HelloWorld" toUnderScoreCase("helloWorld") = "hello_world"
   */
  public static String toUnderScoreCase(String s) {
    if (s == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    boolean upperCase = false;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      boolean nextUpperCase = true;
      if (i < (s.length() - 1)) {
        nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
      }
      if ((i > 0) && Character.isUpperCase(c)) {
        if (!upperCase || !nextUpperCase) {
          sb.append(SEPARATOR);
        }
        upperCase = true;
      } else {
        upperCase = false;
      }
      sb.append(Character.toLowerCase(c));
    }
    return sb.toString();
  }
}
