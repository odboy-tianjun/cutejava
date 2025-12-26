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
import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import cn.odboy.system.dal.model.SystemRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Set;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 角色 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemRoleMapper extends BaseMapper<SystemRoleTb> {

  default Long countRoleByArgs(SystemQueryRoleArgs criteria) {
    LambdaQueryWrapper<SystemRoleTb> wrapper = new LambdaQueryWrapper<>();
    if (criteria != null) {
      wrapper.and(StrUtil.isNotBlank(criteria.getBlurry()),
          c -> c.like(SystemRoleTb::getName, criteria.getBlurry()).or()
              .like(SystemRoleTb::getDescription, criteria.getBlurry()));
      if (CollUtil.isNotEmpty(criteria.getCreateTime()) && criteria.getCreateTime().size() >= 2) {
        wrapper.between(SystemRoleTb::getCreateTime, criteria.getCreateTime().get(0),
            criteria.getCreateTime().get(1));
      }
    }
    return selectCount(wrapper);
  }

  List<SystemRoleVo> selectRoleByArgs(@Param("criteria") SystemQueryRoleArgs criteria);

  List<SystemRoleVo> selectRoleByUserId(@Param("userId") Long userId);

  List<SystemRoleVo> selectRoleByMenuId(@Param("menuId") Long menuId);

  SystemRoleVo getRoleById(@Param("roleId") Long roleId);

  Integer countRoleByDeptIds(@Param("deptIds") Set<Long> deptIds);
}
