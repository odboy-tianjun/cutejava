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
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.model.SystemQueryMenuArgs;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 菜单 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenuTb> {
    default void updateMenuSubCntByMenuId(Long count, Long menuId) {
        if (count != null) {
            LambdaUpdateWrapper<SystemMenuTb> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SystemMenuTb::getId, menuId);
            wrapper.set(SystemMenuTb::getSubCount, count);
            update(wrapper);
        }
    }
    default SystemMenuTb getMenuByComponentName(String componentName) {
        LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemMenuTb::getComponentName, componentName);
        return selectOne(wrapper);
    }

    default SystemMenuTb getMenuByTitle(String title) {
        LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemMenuTb::getTitle, title);
        return selectOne(wrapper);
    }

    default Long countMenuByPid(Long pid) {
        LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemMenuTb::getPid, pid);
        return selectCount(wrapper);
    }

    default List<SystemMenuTb> selectMenuByArgs(SystemQueryMenuArgs criteria) {
        LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
        if (criteria != null) {
            wrapper.isNull(criteria.getPidIsNull() != null, SystemMenuTb::getPid);
            wrapper.eq(criteria.getPid() != null, SystemMenuTb::getPid, criteria.getPid());
            wrapper.and(StrUtil.isNotBlank(criteria.getBlurry()),
                c -> c.like(SystemMenuTb::getTitle, criteria.getBlurry()).or()
                    .like(SystemMenuTb::getComponentName, criteria.getBlurry()).or()
                    .like(SystemMenuTb::getPermission, criteria.getBlurry()));
            if (CollUtil.isNotEmpty(criteria.getCreateTime()) && criteria.getCreateTime().size() >= 2) {
                wrapper.between(SystemMenuTb::getCreateTime, criteria.getCreateTime().get(0),
                    criteria.getCreateTime().get(1));
            }
        }
        wrapper.orderByAsc(SystemMenuTb::getMenuSort);
        return selectList(wrapper);
    }

    default List<SystemMenuTb> selectMenuByPid(Long pid) {
        LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SystemMenuTb::getPid, pid);
        wrapper.orderByAsc(SystemMenuTb::getMenuSort);
        return selectList(wrapper);
    }

    default List<SystemMenuTb> selectMenuByPidIsNull() {
        LambdaQueryWrapper<SystemMenuTb> wrapper = new LambdaQueryWrapper<>();
        wrapper.isNull(SystemMenuTb::getPid);
        wrapper.orderByAsc(SystemMenuTb::getMenuSort);
        return selectList(wrapper);
    }
}
