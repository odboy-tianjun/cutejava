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
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 结果封装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class KitPageResult<T> extends KitObject {
    private List<T> content;
    private long totalElements;

    public static <T> KitPageResult<T> emptyListData() {
        KitPageResult<T> result = new KitPageResult<>();
        result.setTotalElements(0);
        result.setContent(new ArrayList<>());
        return result;
    }

    public static <T> KitPageResult<T> listData(IPage<T> page) {
        KitPageResult<T> result = new KitPageResult<>();
        result.setTotalElements(page.getTotal());
        result.setContent(page.getRecords());
        return result;
    }
}
