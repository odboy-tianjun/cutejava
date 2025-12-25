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

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static cn.odboy.util.KitFileUtil.getPrefix;
import static cn.odboy.util.KitFileUtil.getSize;
import static cn.odboy.util.KitFileUtil.getSuffix;
import static cn.odboy.util.KitFileUtil.toFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class KitFileUtilTest {

    @Test
    public void testToFile() {
        long retval = toFile(new MockMultipartFile("foo", (byte[])null)).getTotalSpace();
        assertEquals(500695072768L, retval);
    }

    @Test
    public void testGetExtensionName() {
        assertEquals("foo", getSuffix("foo"));
        assertEquals("exe", getSuffix("bar.exe"));
    }

    @Test
    public void testGetFileNameNoEx() {
        assertEquals("foo", getPrefix("foo"));
        assertEquals("bar", getPrefix("bar.txt"));
    }

    @Test
    public void testGetSize() {
        assertEquals("1000B   ", getSize(1000));
        assertEquals("1.00KB   ", getSize(1024));
        assertEquals("1.00MB   ", getSize(1048576));
        assertEquals("1.00GB   ", getSize(1073741824));
    }
}
