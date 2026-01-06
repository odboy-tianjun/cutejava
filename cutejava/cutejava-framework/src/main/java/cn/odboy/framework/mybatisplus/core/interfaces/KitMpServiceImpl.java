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
package cn.odboy.framework.mybatisplus.core.interfaces;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.mybatisplus.core.KitMpQUtil;
import cn.odboy.util.KitBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import org.apache.ibatis.binding.MapperMethod;

/**
 * 公共抽象Mapper接口类
 *
 * @author odboy
 * @date 2022-04-03
 */
public class KitMpServiceImpl<M extends KitMpMapper<T>, T> extends ServiceImpl<M, T> {

  /**
   * 快速插入
   *
   * @param resources /
   */
  public <G> int saveFeatureClazz(G resources) {
    return getBaseMapper().insert(KitBeanUtil.copyToClass(resources, this.getEntityClass()));
  }

  /**
   * 快速批量插入
   *
   * @param resources /
   */
  public <G> int saveFeatureClazzList(List<G> resources) {
    return getBaseMapper().insertBatchSomeColumn(KitBeanUtil.copyToList(resources, this.getEntityClass()));
  }

  /**
   * 快速更新
   *
   * @param resources /
   */
  public <G> int modifyFeatureClazzById(G resources) {
    return getBaseMapper().updateById(KitBeanUtil.copyToClass(resources, this.getEntityClass()));
  }

  /**
   * 快速批量更新
   *
   * @param resources /
   * @param batchSize 每批数量
   */
  public <G> boolean modifyFeatureClazzListById(Collection<G> resources, int batchSize) {
    String sqlStatement = this.getSqlStatement(SqlMethod.UPDATE_BY_ID);
    return this.executeBatch(resources, batchSize, (sqlSession, entity) -> {
      MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
      param.put("et", KitBeanUtil.copyToClass(entity, this.getEntityClass()));
      sqlSession.update(sqlStatement, param);
    });
  }

  /**
   * 通过id查询
   *
   * @param id          /
   * @param targetClazz 目标类型
   */
  public <G> G queryFeatureClazzById(Serializable id, Class<G> targetClazz) {
    T entity = this.getById(id);
    return entity == null ? null : KitBeanUtil.copyToClass(entity, targetClazz);
  }

  /**
   * 查一条数据
   *
   * @param wrapper 制造条件
   */
  public T queryClazzByArgs(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn) {
    if (orderColumn == null) {
      throw new BadRequestException("参数orderColumn(排序的列)必填");
    }
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    wrapper.last("limit 1");
    wrapper.orderByDesc(orderColumn);
    List<T> ts = baseMapper.selectList(wrapper);
    if (ts.isEmpty()) {
      return null;
    }
    return ts.stream().findFirst().get();
  }

  public <G> G queryFeatureClazzByArgs(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn, Class<G> clazz) {
    if (orderColumn == null) {
      throw new BadRequestException("参数orderColumn(排序的列)必填");
    }
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    wrapper.last("limit 1");
    wrapper.orderByDesc(orderColumn);
    List<T> ts = baseMapper.selectList(wrapper);
    if (ts.isEmpty()) {
      return null;
    }
    return KitBeanUtil.copyToClass(ts.stream().findFirst().get(), clazz);
  }

  /**
   * 通过id集合查询
   *
   * @param ids         /
   * @param targetClazz 目标类型
   */
  public <G> List<G> queryFeatureClazzListByIds(List<Serializable> ids, Class<G> targetClazz) {
    List<T> entitys = this.listByIds(ids);
    return entitys == null || entitys.isEmpty() ? CollUtil.newArrayList() : KitBeanUtil.copyToList(entitys, targetClazz);
  }

  /**
   * 查有关的数据
   *
   * @param args 动态条件
   */
  public <Q> List<T> queryClazzListByArgs(Q args) {
    QueryWrapper<T> predicate = KitMpQUtil.build(args);
    return baseMapper.selectList(predicate);
  }

  /**
   * 查有关的数据
   *
   * @param wrapper 制造条件
   */
  public List<T> queryClazzListByArgs(LambdaQueryWrapper<T> wrapper) {
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    return baseMapper.selectList(wrapper);
  }

  /**
   * 查有关的数据并转成目标类型的数据
   *
   * @param args        动态条件
   * @param targetClazz 目标类型
   */
  public <G, Q> List<G> queryFeatureClazzListByArgs(Q args, Class<G> targetClazz) {
    QueryWrapper<T> predicate = KitMpQUtil.build(args);
    List<T> data = baseMapper.selectList(predicate);
    return KitBeanUtil.copyToList(data, targetClazz);
  }

  /**
   * 查有关的数据并转换为目标类型
   *
   * @param wrapper     制造条件
   * @param targetClazz 目标类型
   */
  public <G> List<G> queryFeatureClazzListByArgs(LambdaQueryWrapper<T> wrapper, Class<G> targetClazz) {
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    return KitBeanUtil.copyToList(baseMapper.selectList(wrapper), targetClazz);
  }

  /**
   * 查有关的分页数据
   *
   * @param wrapper  制造条件
   * @param pageable 分页参数
   */
  public KitPageResult<T> queryClazzPageByArgs(LambdaQueryWrapper<T> wrapper, IPage<T> pageable) {
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
    return new KitPageResult<>(originPageData.getRecords(), originPageData.getTotal());
  }

  /**
   * 查有关的分页数据
   *
   * @param wrapper     制造条件
   * @param pageable    分页参数
   * @param targetClazz 目标类型
   */
  public <G> KitPageResult<G> queryFeatureClazzPageByArgs(LambdaQueryWrapper<T> wrapper, IPage<T> pageable,
      Class<G> targetClazz) {
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
    return new KitPageResult<>(KitBeanUtil.copyToList(originPageData.getRecords(), targetClazz),
        originPageData.getTotal());
  }

  public <G, Q> KitPageResult<G> queryFeatureClazzPageByArgs(Q args, IPage<T> pageable, Class<G> targetClazz) {
    QueryWrapper<T> wrapper = KitMpQUtil.build(args);
    IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
    return new KitPageResult<>(KitBeanUtil.copyToList(originPageData.getRecords(), targetClazz),
        originPageData.getTotal());
  }

  public <Q> KitPageResult<T> queryClazzPageByArgs(Q args, IPage<T> pageable) {
    QueryWrapper<T> wrapper = KitMpQUtil.build(args);
    IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
    return new KitPageResult<>(originPageData.getRecords(), originPageData.getTotal());
  }

  /**
   * 条件更新
   *
   * @param wrapper 制造条件
   * @param entity  更新内容
   */
  public int modifyClazzByArgs(LambdaQueryWrapper<T> wrapper, T entity) {
    if (wrapper == null) {
      throw new BadRequestException("参数wrapper必填");
    }
    return baseMapper.update(entity, wrapper);
  }

  /**
   * 条件删除
   *
   * @param wrapper 制造条件
   */
  public int removeClazzByArgs(LambdaQueryWrapper<T> wrapper) {
    if (wrapper == null) {
      throw new BadRequestException("参数wrapper必填");
    }
    return baseMapper.delete(wrapper);
  }
}
