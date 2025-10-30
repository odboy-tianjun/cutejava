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
package cn.odboy.task.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 任务状态(固定值)
 *
 * @author odboy
 * @date 2025-09-26
 */
@Getter
@AllArgsConstructor
public enum TaskStatusEnum {
    Running("running", "运行中"), Success("success", "执行成功"), Fail("fail", "执行失败");
    private final String code;
    private final String name;

    public static String getDesc(String code) {
        for (TaskStatusEnum taskStatusEnum : TaskStatusEnum.values()) {
            if (taskStatusEnum.code.equals(code)) {
                return taskStatusEnum.getName();
            }
        }
        return code;
    }
}
