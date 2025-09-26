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

import cn.odboy.base.CsObject;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class SystemCreateDictDetailArgs extends CsObject {
    @NotNull(message = "参数dict必填")
    private SystemDictTb dict;
    @NotBlank(message = "字典标签必填")
    private String label;
    @NotBlank(message = "字典值必填")
    private String value;
    @NotNull(message = "字典排序必填")
    private Integer dictSort;
}
