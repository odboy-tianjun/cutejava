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

import cn.odboy.base.CsPageResult;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分页工具
 *
 * @author odboy
 */
@UtilityClass
public final class CsPageUtil extends cn.hutool.core.util.PageUtil {

    /**
     * List 分页
     */
    public static <T> List<T> softPaging(long page, long size, List<T> list) {
        int pageIndex = Math.toIntExact(page - 1);
        int fromIndex = Math.toIntExact(pageIndex * size);
        int toIndex = Math.toIntExact(pageIndex * size + size);
        if (fromIndex > list.size()) {
            return Collections.emptyList();
        } else if (toIndex >= list.size()) {
            return list.subList(fromIndex, list.size());
        } else {
            return list.subList(fromIndex, toIndex);
        }
    }

    /**
     * Page 数据处理
     */
    public static <T> CsPageResult<T> toPage(IPage<T> page) {
        return new CsPageResult<>(page.getRecords(), page.getTotal());
    }

    /**
     * 自定义分页
     */
    public static <T> CsPageResult<T> toPage(List<T> list) {
        return new CsPageResult<>(list, list.size());
    }

    /**
     * 返回空数据
     */
    public static <T> CsPageResult<T> emptyListData() {
        return new CsPageResult<>(new ArrayList<>(), 0);
    }

    /**
     * 返回空数据
     */
    public static <T> CsPageResult<T> emptyData() {
        return new CsPageResult<>(null, 0);
    }

    /**
     * 自定义分页
     */
    public static <T> CsPageResult<T> toPage(List<T> list, long totalElements) {
        return new CsPageResult<>(list, totalElements);
    }

    public static <T> Page<T> getCount(BaseMapper<T> baseMapper) {
        return new Page<>(1, baseMapper.selectCount(null));
    }
}
