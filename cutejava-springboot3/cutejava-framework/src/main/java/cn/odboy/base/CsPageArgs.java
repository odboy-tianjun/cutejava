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

package cn.odboy.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据分页参数
 *
 * @author odboy
 * @date 2025-01-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CsPageArgs<T> implements Serializable {
    @NotNull(message = "参数page不能为空")
    @Min(value = 1, message = "参数page最小值为1")
    @Schema(name ="页码", example = "1")
    private Integer page;
    @NotNull(message = "参数size不能为空")
    @Min(value = 1, message = "参数size最小值为1")
    @Schema(name ="每页数据量", example = "10")
    private Integer size;
    private T args;
}
