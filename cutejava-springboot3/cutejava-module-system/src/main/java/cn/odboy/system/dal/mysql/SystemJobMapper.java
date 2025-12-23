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
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.model.SystemQueryJobArgs;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 职位 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemJobMapper extends BaseMapper<SystemJobTb> {
    default SystemJobTb getJobByName(String name) {
        LambdaQueryWrapper<SystemJobTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemJobTb::getName, name);
        return selectOne(wrapper);
    }
    default void injectQueryParams(SystemQueryJobArgs criteria, LambdaQueryWrapper<SystemJobTb> wrapper) {
        if (criteria != null) {
            wrapper.like(StrUtil.isNotBlank(criteria.getName()), SystemJobTb::getName, criteria.getName());
            wrapper.eq(criteria.getEnabled() != null, SystemJobTb::getEnabled, criteria.getEnabled());
            if (CollUtil.isNotEmpty(criteria.getCreateTime()) && criteria.getCreateTime().size() >= 2) {
                wrapper.between(SystemJobTb::getCreateTime, criteria.getCreateTime().get(0),
                    criteria.getCreateTime().get(1));
            }
        }
        wrapper.orderByDesc(SystemJobTb::getJobSort, SystemJobTb::getId);
    }

    default IPage<SystemJobTb> selectJobByArgs(SystemQueryJobArgs criteria, Page<SystemJobTb> page) {
        LambdaQueryWrapper<SystemJobTb> wrapper = new LambdaQueryWrapper<>();
        this.injectQueryParams(criteria, wrapper);
        return selectPage(page, wrapper);
    }

    default List<SystemJobTb> selectJobByArgs(SystemQueryJobArgs criteria) {
        LambdaQueryWrapper<SystemJobTb> wrapper = new LambdaQueryWrapper<>();
        this.injectQueryParams(criteria, wrapper);
        return selectList(wrapper);
    }
}
