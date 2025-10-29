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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CsStringUtilTest {

    @Test
    public void testToCamelCase() {
        assertNull(CsStringUtil.toCamelCase(null));
    }

    @Test
    public void testToCapitalizeCamelCase() {
        assertNull(CsStringUtil.toCapitalizeCamelCase(null));
        assertEquals("HelloWorld", CsStringUtil.toCapitalizeCamelCase("hello_world"));
    }

    @Test
    public void testToUnderScoreCase() {
        assertNull(CsStringUtil.toUnderScoreCase(null));
        assertEquals("hello_world", CsStringUtil.toUnderScoreCase("helloWorld"));
        assertEquals("\u0000\u0000", CsStringUtil.toUnderScoreCase("\u0000\u0000"));
        assertEquals("\u0000_a", CsStringUtil.toUnderScoreCase("\u0000A"));
    }

    @Test
    public void testGetWeekDay() {
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E");
        assertEquals(simpleDateformat.format(new Date()), CsStringUtil.getWeekDay());
    }

    @Test
    public void testGetIP() {
        Assertions.assertEquals("127.0.0.1", CsBrowserUtil.getIp(new MockHttpServletRequest()));
    }
}
