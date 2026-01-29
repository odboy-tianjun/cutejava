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

import cn.odboy.base.KitPageResult;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 公共抽象Service接口类
 *
 * @author odboy
 * @date 2022-04-03
 */
public interface KitMpService<T> extends IService<T> {

  /**
   * 保存数据（已测试）
   *
   * @param resources 非Tb对象
   * @return 影响的行数
   */
  <G> int saveFeatureClazz(G resources);

  /**
   * 批量保存数据（已测试）
   *
   * @param resources 非Tb对象集合
   * @return 影响的行数
   */
  <G> int batchSaveFeatureClazz(List<G> resources);

  /**
   * 根据id更新数据（已测试）
   *
   * @param resources 非Tb对象
   * @return 影响的行数
   */
  <G> int updateFeatureClazzById(G resources);

  /**
   * 根据id更新数据（已测试）
   *
   * @param resources 非Tb对象
   * @return 影响的行数
   */
  <G> boolean batchUpdateFeatureClazzById(Collection<G> resources, int batchSize);

  /**
   * 通过id查询（已测试）
   *
   * @param id          数据id
   * @param targetClazz 目标类型
   */
  <G> G getFeatureClazzById(Serializable id, Class<G> targetClazz);

  /**
   * 根据条件查询一条数据（已测试）
   *
   * @param wrapper     查询条件
   * @param orderColumn 排序字段
   * @return /
   */
  T getClazzByArgs(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn);

  /**
   * 根据条件查询一条数据（已测试）
   *
   * @param wrapper     查询条件
   * @param orderColumn 排序字段
   * @param targetClazz 目标类型
   * @return /
   */
  <G> G getFeatureClazzByArgs(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn, Class<G> targetClazz);

  /**
   * 根据id集合查询目标数据（已测试）
   *
   * @param ids         id集合
   * @param targetClazz 目标类型
   * @return /
   */
  <G> List<G> queryFeatureClazzListByIds(List<Serializable> ids, Class<G> targetClazz);

  /**
   * 根据条件查询目标数据（已测试）
   *
   * @param wrapper     查询条件
   * @param targetClazz 目标类型
   * @return /
   */
  <G> List<G> queryFeatureClazzListByArgs(LambdaQueryWrapper<T> wrapper, Class<G> targetClazz);

  /**
   * 根据条件分页查询数据（已测试）
   *
   * @param wrapper  查询条件
   * @param pageable 分页参数
   * @return /
   */
  KitPageResult<T> searchClazzPageByArgs(LambdaQueryWrapper<T> wrapper, IPage<T> pageable);

  /**
   * 根据条件分页查询目标数据（已测试）
   *
   * @param wrapper     查询条件
   * @param pageable    分页参数
   * @param targetClazz 目标类型
   * @return /
   */
  <G> KitPageResult<G> searchFeatureClazzPageByArgs(LambdaQueryWrapper<T> wrapper, IPage<T> pageable, Class<G> targetClazz);

  /**
   * 根据条件更新数据（已测试）
   *
   * @param wrapper 查询条件与值
   * @return 影响的行数
   */
  int updateClazzByArgs(LambdaQueryWrapper<T> wrapper);
}
