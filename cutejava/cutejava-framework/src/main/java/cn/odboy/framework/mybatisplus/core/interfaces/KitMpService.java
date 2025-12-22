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
    <G> int saveFeatureClazz(G resources);
    <G> int saveFeatureClazzList(List<G> resources);
    <G> int modifyFeatureClazzById(G resources);
    <G> boolean modifyFeatureClazzListById(Collection<G> resources, int batchSize);
    <G> G queryFeatureClazzById(Serializable id, Class<G> targetClazz);
    T queryClazzByArgs(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn);
    <G> G queryFeatureClazzByArgs(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> orderColumn, Class<G> clazz);
    <G> List<G> queryFeatureClazzListByIds(List<Serializable> ids, Class<G> targetClazz);
    <Q> List<T> queryClazzListByArgs(Q criteria);
    List<T> queryClazzListByArgs(LambdaQueryWrapper<T> wrapper);
    <G, Q> List<G> queryFeatureClazzListByArgs(Q criteria, Class<G> targetClazz);
    <G> List<G> queryFeatureClazzListByArgs(LambdaQueryWrapper<T> wrapper, Class<G> targetClazz);
    KitPageResult<T> queryClazzPageByArgs(LambdaQueryWrapper<T> wrapper, IPage<T> pageable);
    <G> KitPageResult<G> queryFeatureClazzPageByArgs(LambdaQueryWrapper<T> wrapper, IPage<T> pageable,
        Class<G> targetClazz);
    <G, Q> KitPageResult<G> queryFeatureClazzPageByArgs(Q criteria, IPage<T> pageable, Class<G> targetClazz);
    <Q> KitPageResult<T> queryClazzPageByArgs(Q criteria, IPage<T> pageable);
    int modifyClazzByArgs(LambdaQueryWrapper<T> wrapper, T entity);
    int removeClazzByArgs(LambdaQueryWrapper<T> wrapper);
}
