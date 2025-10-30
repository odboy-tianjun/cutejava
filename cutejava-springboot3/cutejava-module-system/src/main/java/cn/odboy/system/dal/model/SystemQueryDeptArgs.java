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

package cn.odboy.system.dal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SystemQueryDeptArgs {

    @Schema(name = "部门id集合")
    private List<Long> ids;

    @Schema(name = "部门名称")
    private String name;

    @Schema(name = "是否启用")
    private Boolean enabled;

    @Schema(name = "上级部门")
    private Long pid;

    @Schema(name = "PID为空查询")
    private Boolean pidIsNull;

    @Schema(name = "创建时间")
    private List<Date> createTime;
}
