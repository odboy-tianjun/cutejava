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

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CsPageResult<T> extends CsObject {
    private List<T> content;
    private long totalElements;

    public static <T> CsPageResult<T> emptyListData() {
        CsPageResult<T> result = new CsPageResult<>();
        result.setTotalElements(0);
        result.setContent(new ArrayList<>());
        return result;
    }

    public static <T> CsPageResult<T> listData(IPage<T> page) {
        CsPageResult<T> result = new CsPageResult<>();
        result.setTotalElements(page.getTotal());
        result.setContent(page.getRecords());
        return result;
    }
}
