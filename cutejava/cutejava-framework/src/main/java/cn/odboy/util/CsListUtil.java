/*
 *  Copyright 2021-2025 Odboy
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cn.odboy.util;

import cn.hutool.core.date.DateTime;
import cn.odboy.base.CsBaseUserTimeTb;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * List 工具
 *
 * @author odboy
 * @date 2024-10-01
 */
@UtilityClass
public final class CsListUtil {
    /**
     * 根据对象T的属性去重
     *
     * @param items      对象T集合
     * @param classifier 对象T属性表达式, 比如: User::getName
     */
    public static <T, K> List<T> distinctByArgs(List<T> items, Function<? super T, ? extends K> classifier) {
        return items.stream()
                .collect(Collectors.groupingBy(classifier))
                .values().stream()
                .map(monitorItem -> monitorItem.iterator().next())
                .collect(Collectors.toList());
    }

    /**
     * list转map, 保留新值 -> Map<String, User> userMap = items.stream().collect(Collectors.toMap(User::getName, user -> user)) <br>
     * list转map, 保留旧值 -> Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getName, user -> user, (existing, replacement) -> existing));
     */
    public static void main(String[] args) {
        List<CsBaseUserTimeTb> entities = new ArrayList<>();
        int total = 50;
        for (int i = 0; i < total; i++) {
            CsBaseUserTimeTb entity = new CsBaseUserTimeTb();
            if (i % 2 == 0) {
                entity.setCreateBy("odboy");
            } else {
                entity.setCreateBy("admin");
            }
            entity.setUpdateBy("admin");
            entity.setCreateTime(DateTime.now().toTimestamp());
            entity.setUpdateTime(DateTime.now().toTimestamp());
            entities.add(entity);
        }
        List<CsBaseUserTimeTb> entities1 = CsListUtil.distinctByArgs(entities, CsBaseUserTimeTb::getCreateBy);
        System.err.println(entities1);
    }
}
