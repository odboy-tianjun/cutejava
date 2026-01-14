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

import static cn.odboy.util.KitDesEncryptUtil.desDecrypt;
import static cn.odboy.util.KitDesEncryptUtil.desEncrypt;
import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

public class KitDesEncryptUtilTest {

  @SneakyThrows
  public static void main(String[] args) {
    System.err.println(desEncrypt("abc12345678"));
  }

  /**
   * 对称加密
   */
  @Test
  public void testDesEncrypt() {
    try {
      assertEquals("7772841DC6099402", desEncrypt("abc12345678"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 对称解密
   */
  @Test
  public void testDesDecrypt() {
    try {
      assertEquals("abc12345678", desDecrypt("7772841DC6099402"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
