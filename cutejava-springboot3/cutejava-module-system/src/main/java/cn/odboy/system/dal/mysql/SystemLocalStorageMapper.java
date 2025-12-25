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
package cn.odboy.system.dal.mysql;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.system.dal.dataobject.SystemLocalStorageTb;
import cn.odboy.system.dal.model.SystemQueryStorageArgs;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 本地存储记录 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemLocalStorageMapper extends BaseMapper<SystemLocalStorageTb> {

  default void injectQueryParams(SystemQueryStorageArgs criteria, LambdaQueryWrapper<SystemLocalStorageTb> wrapper) {
    if (criteria != null) {
      wrapper.and(StrUtil.isNotBlank(criteria.getBlurry()),
          c -> c.like(SystemLocalStorageTb::getName, criteria.getBlurry()).or()
              .like(SystemLocalStorageTb::getSuffix, criteria.getBlurry()).or()
              .like(SystemLocalStorageTb::getType, criteria.getBlurry()).or()
              .like(SystemLocalStorageTb::getCreateBy, criteria.getBlurry()));
      if (CollUtil.isNotEmpty(criteria.getCreateTime()) && criteria.getCreateTime().size() >= 2) {
        wrapper.between(SystemLocalStorageTb::getUpdateTime, criteria.getCreateTime().get(0),
            criteria.getCreateTime().get(1));
      }
    }
    wrapper.orderByDesc(SystemLocalStorageTb::getId);
  }

  default List<SystemLocalStorageTb> selectLocalStorageByArgs(SystemQueryStorageArgs criteria) {
    LambdaQueryWrapper<SystemLocalStorageTb> wrapper = new LambdaQueryWrapper<>();
    injectQueryParams(criteria, wrapper);
    return selectList(wrapper);
  }

  default IPage<SystemLocalStorageTb> selectLocalStorageByArgs(SystemQueryStorageArgs criteria,
      Page<SystemLocalStorageTb> page) {
    LambdaQueryWrapper<SystemLocalStorageTb> wrapper = new LambdaQueryWrapper<>();
    injectQueryParams(criteria, wrapper);
    return selectPage(page, wrapper);
  }
}
