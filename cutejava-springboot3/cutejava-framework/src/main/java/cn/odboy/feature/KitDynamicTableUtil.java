/*
 * Copyright 2021-2026 Odboy
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
package cn.odboy.feature;

import cn.odboy.base.KitPageArgs;
import cn.odboy.framework.mybatisplus.core.KitMpQUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.experimental.UtilityClass;

/**
 * 动态表格辅助工具
 *
 * @author odboy
 */
@UtilityClass
public class KitDynamicTableUtil {

  /**
   * 渲染排序规则
   *
   * @param pageArgs 分页参数
   * @param <M>      数据表对象
   * @param <T>      渲染对象
   * @return /
   */
  public static <M, T> QueryWrapper<M> renderOrder(KitPageArgs<T> pageArgs) {
    QueryWrapper<M> wrapper = new QueryWrapper<>();
    KitPageArgs.OrderBy orderBy = pageArgs.getOrderBy();
    if (orderBy != null) {
      wrapper.orderBy(true, orderBy.isAsc(), orderBy.getColumn());
    }
    return wrapper;
  }

  /**
   * 渲染所有规则（需要配合 @KitMpQuery 注解使用）
   *
   * @param pageArgs 分页参数
   * @param <M>      数据表对象
   * @param <T>      渲染对象
   * @return /
   */
  public static <M, T> LambdaQueryWrapper<M> renderAll(KitPageArgs<T> pageArgs) {
    QueryWrapper<M> wrapper = KitMpQUtil.build(pageArgs.getArgs());
    KitPageArgs.OrderBy orderBy = pageArgs.getOrderBy();
    if (orderBy != null) {
      wrapper.orderBy(true, orderBy.isAsc(), orderBy.getColumn());
    }
    return wrapper.lambda();
  }
}
