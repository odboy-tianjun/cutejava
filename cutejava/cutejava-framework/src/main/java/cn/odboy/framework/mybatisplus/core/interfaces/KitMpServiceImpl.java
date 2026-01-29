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
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.framework.mybatisplus.core.KitMpQUtil;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitValidUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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

  public <G> int saveFeatureClazz(G resources) {
    return getBaseMapper().insert(KitBeanUtil.copyToClass(resources, this.getEntityClass()));
  }

  public <G> int batchSaveFeatureClazz(List<G> resources) {
    return getBaseMapper().insert(KitBeanUtil.copyToList(resources, this.getEntityClass())).size();
  }

  public <G> int updateFeatureClazzById(G resources) {
    return getBaseMapper().updateById(KitBeanUtil.copyToClass(resources, this.getEntityClass()));
  }

  public <G> int batchUpdateFeatureClazzById(Collection<G> resources, int batchSize) {
    return getBaseMapper().updateById(KitBeanUtil.copyToList(resources, this.getEntityClass()), batchSize).size();
//    String sqlStatement = this.getSqlStatement(SqlMethod.UPDATE_BY_ID);
//    return this.executeBatch(
//        resources, batchSize, (sqlSession, entity) -> {
//          MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
//          param.put("et", KitBeanUtil.copyToClass(entity, this.getEntityClass()));
//          sqlSession.update(sqlStatement, param);
//        }
//    );
  }

  public <G> G getFeatureClazzById(Serializable id, Class<G> targetClazz) {
    return KitBeanUtil.copyToClass(this.getById(id), targetClazz);
  }

  public T getClazzByArgs(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn) {
    KitValidUtil.isNull(orderColumn, "排序列", "orderColumn");
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    wrapper.orderByDesc(orderColumn);
    wrapper.last("limit 1");
    return baseMapper.selectOne(wrapper);
  }

  public <G> G getFeatureClazzByArgs(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn, Class<G> targetClazz) {
    KitValidUtil.isNull(orderColumn, "排序列", "orderColumn");
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    wrapper.orderByDesc(orderColumn);
    wrapper.last("limit 1");
    return KitBeanUtil.copyToClass(baseMapper.selectOne(wrapper), targetClazz);
  }

  public <G> List<G> queryFeatureClazzListByIds(List<Serializable> ids, Class<G> targetClazz) {
    List<T> entitys = this.listByIds(ids);
    return KitBeanUtil.copyToList(entitys, targetClazz);
  }

  public <G> List<G> queryFeatureClazzListByArgs(LambdaQueryWrapper<T> wrapper, Class<G> targetClazz) {
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    return KitBeanUtil.copyToList(baseMapper.selectList(wrapper), targetClazz);
  }

  public KitPageResult<T> searchClazzPageByArgs(LambdaQueryWrapper<T> wrapper, IPage<T> pageable) {
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
    return new KitPageResult<>(originPageData.getRecords(), originPageData.getTotal());
  }

  public <G> KitPageResult<G> searchFeatureClazzPageByArgs(LambdaQueryWrapper<T> wrapper, IPage<T> pageable, Class<G> targetClazz) {
    if (wrapper == null) {
      wrapper = new LambdaQueryWrapper<>();
    }
    IPage<T> originPageData = baseMapper.selectPage(pageable, wrapper);
    return new KitPageResult<>(
        KitBeanUtil.copyToList(originPageData.getRecords(), targetClazz),
        originPageData.getTotal()
    );
  }

  public int updateClazzByArgs(LambdaUpdateWrapper<T> wrapper) {
    KitValidUtil.isNull(wrapper, "查询条件与值", "wrapper");
    return baseMapper.update(null, wrapper);
  }
}
