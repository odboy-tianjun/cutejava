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

import static cn.odboy.util.KitStringUtil.getWeekDay;
import static cn.odboy.util.KitStringUtil.toCamelCase;
import static cn.odboy.util.KitStringUtil.toCapitalizeCamelCase;
import static cn.odboy.util.KitStringUtil.toUnderScoreCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class KitStringUtilTest {

  @Test
  public void testToCamelCase() {
    assertNull(toCamelCase(null));
  }

  @Test
  public void testToCapitalizeCamelCase() {
    assertNull(KitStringUtil.toCapitalizeCamelCase(null));
    assertEquals("HelloWorld", toCapitalizeCamelCase("hello_world"));
  }

  @Test
  public void testToUnderScoreCase() {
    assertNull(KitStringUtil.toUnderScoreCase(null));
    assertEquals("hello_world", toUnderScoreCase("helloWorld"));
    assertEquals("\u0000\u0000", toUnderScoreCase("\u0000\u0000"));
    assertEquals("\u0000_a", toUnderScoreCase("\u0000A"));
  }

  @Test
  public void testGetWeekDay() {
    SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
    assertEquals(simpleDateformat.format(new Date()), getWeekDay());
  }

  @Test
  public void testGetIP() {
    Assertions.assertEquals("127.0.0.1", KitBrowserUtil.getIp(new MockHttpServletRequest()));
  }
}
