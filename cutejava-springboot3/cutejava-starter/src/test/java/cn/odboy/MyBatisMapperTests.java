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

package cn.odboy;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MyBatisMapperTests {
    @Autowired
    private SystemUserMapper systemUserMapper;

    @Test
    public void testMapper1() {
        // SELECT * FROM system_user WHERE (create_time > '2025-08-18T14:48:18.400+0800');
        systemUserMapper.selectList(new LambdaQueryWrapper<SystemUserTb>().gt(SystemUserTb::getCreateTime, new Date()));

        // SELECT * FROM system_user WHERE (create_time > '2025-08-18T14:50:16.786+0800');
        systemUserMapper.selectList(new LambdaQueryWrapper<SystemUserTb>().gt(SystemUserTb::getCreateTime, DateTime.now()));

        // SELECT * FROM system_user WHERE (create_time > '2025-08-18 14:53:46');
        systemUserMapper.selectList(new LambdaQueryWrapper<SystemUserTb>().gt(SystemUserTb::getCreateTime, DateUtil.formatDateTime(new Date())));

        // SELECT * FROM system_user WHERE (create_time > '2025-08-18');
        systemUserMapper.selectList(new LambdaQueryWrapper<SystemUserTb>().gt(SystemUserTb::getCreateTime, DateUtil.formatDate(new Date())));
    }
}

