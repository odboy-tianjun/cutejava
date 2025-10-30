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

import cn.odboy.framework.mybatisplus.core.CsMpCmdGenUtil;

import java.util.List;

/**
 * 代码生成入口
 */
public class GenCode {
    private static final String ADDR = "192.168.100.128";
    private static final Integer PORT = 23306;
    private static final String DATABASE_NAME = "cutejava";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PWD = "kd123456";
    private static final String URL = String.format("jdbc:mysql://%s:%s/%s", ADDR, PORT, DATABASE_NAME);

    public static void main(String[] args) {
        CsMpCmdGenUtil generator = new CsMpCmdGenUtil();
        generator.setDatabaseUrl(URL);
        generator.setDriverClassName("com.mysql.cj.jdbc.Driver");
        generator.setDatabaseUsername(DATABASE_USER);
        generator.setDatabasePassword(DATABASE_PWD);
        genTaskCode(generator);
    }

    private static void genTaskCode(CsMpCmdGenUtil generator) {
        generator.gen("task", "", List.of("task_instance_info", "task_instance_detail", "task_template_info", "task_instance_step_detail"));
    }
}
