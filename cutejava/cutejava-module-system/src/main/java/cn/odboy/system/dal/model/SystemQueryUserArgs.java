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

import cn.odboy.base.KitObject;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SystemQueryUserArgs extends KitObject {
    @ApiModelProperty(value = "ID") private Long id;
    @ApiModelProperty(value = "多个ID") private Set<Long> deptIds = new HashSet<>();
    @ApiModelProperty(value = "模糊查询") private String blurry;
    @ApiModelProperty(value = "是否启用") private Boolean enabled;
    @ApiModelProperty(value = "部门ID") private Long deptId;
    @ApiModelProperty(value = "创建时间") private List<Date> createTime;
    @ApiModelProperty(value = "页码", example = "1") private Integer page = 1;
    @ApiModelProperty(value = "每页数据量", example = "10") private Integer size = 10;
}
