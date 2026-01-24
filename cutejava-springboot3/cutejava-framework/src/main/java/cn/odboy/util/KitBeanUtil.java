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
package cn.odboy.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.experimental.UtilityClass;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Bean拷贝相关
 *
 * @author odboy
 * @date 2026-01-06
 */
@UtilityClass
public class KitBeanUtil {

  public static <T> T copyToClass(Object source, Class<T> tClass) {
    return BeanUtil.copyProperties(source, tClass);
  }

  public static void copyToTarget(Object source, Object target) {
    BeanUtil.copyProperties(source, target, CopyOptions.create().setIgnoreNullValue(true));
  }

  public static <T> List<T> copyToList(Collection<?> collection, Class<T> targetType) {
    List<T> ts = BeanUtil.copyToList(collection, targetType);
    if (ts == null) {
      return new ArrayList<>(0);
    }
    return ts;
  }
}
