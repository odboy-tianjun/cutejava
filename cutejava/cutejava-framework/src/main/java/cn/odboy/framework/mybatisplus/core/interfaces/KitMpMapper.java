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
package cn.odboy.framework.mybatisplus.core.interfaces;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

/**
 * 公共抽象Mapper接口类
 *
 * @author odboy
 * @date 2022-04-03
 */
public interface KitMpMapper<E> extends BaseMapper<E> {

  /**
   * 批量插入（已测试）
   *
   * @param entityList List<E>
   * @return 影响的行数
   */
  int insertBatchSomeColumn(List<E> entityList);

  /**
   * 根据id全量更新，为空的字段也会被更新（已测试）
   *
   * @param entity E
   * @return 影响的行数
   */
  int alwaysUpdateSomeColumnById(@Param("et") E entity);

  /**
   * 获取字典（已测试）
   *
   * @param wrapper   LambdaQueryWrapper<E>
   * @param keyName   键对应的列名称（下划线会自动转驼峰）
   * @param valueName 值对应的列名称
   * @return Map<String, Object>
   */
  default Map<String, Object> getMapWithKv(LambdaQueryWrapper<E> wrapper, String keyName, String valueName) {
    Map<String, Object> result = new HashMap<>(1);
    List<E> list = this.selectList(wrapper);
    if (CollUtil.isEmpty(list)) {
      return new HashMap<>(1);
    }
    E tempObj = list.stream().findFirst().orElse(null);
    if (tempObj == null) {
      return new HashMap<>(1);
    }
    Field[] fields = ReflectUtil.getFields(tempObj.getClass());
    Field keyField = null;
    Field valueField = null;
    for (Field field : fields) {
      String fieldName = field.getName();
      if (fieldName.equals(keyName)) {
        keyField = field;
      } else if (fieldName.equals(valueName)) {
        valueField = field;
        break;
      }
    }
    for (E e : list) {
      String fieldKey = String.valueOf(ReflectUtil.getFieldValue(e, keyField));
      if (fieldKey != null) {
        // 下划线转驼峰
        String camelFieldKey = StrUtil.toCamelCase(fieldKey);
        Object fieldValue = ReflectUtil.getFieldValue(e, valueField);
        result.putIfAbsent(camelFieldKey, fieldValue);
      }
    }
    return result;
  }

  /**
   * 条件查询列表, 并返回期望的对象（已测试）
   *
   * @param wrapper LambdaQueryWrapper<E>
   * @param clazz   期望的对象类型
   * @return List<T>
   */
  default <T> List<T> queryFeatureClazz(LambdaQueryWrapper<E> wrapper, Class<T> clazz) {
    List<E> list = this.selectList(wrapper);
    return KitBeanUtil.copyToList(list, clazz);
  }

  /**
   * 条件查询分页列表, 并返回期望的对象（已测试）
   *
   * @param wrapper  LambdaQueryWrapper<E> wrapper
   * @param clazz    期望的对象类型
   * @param pageable 分页参数
   * @return IPage<T>
   */
  default <T> KitPageResult<T> searchFeatureClazz(LambdaQueryWrapper<E> wrapper, Class<T> clazz, Page<E> pageable) {
    IPage<E> pageInfo = this.selectPage(pageable, wrapper);
    return new KitPageResult<>(KitBeanUtil.copyToList(pageInfo.getRecords(), clazz), pageInfo.getTotal());
  }
}
