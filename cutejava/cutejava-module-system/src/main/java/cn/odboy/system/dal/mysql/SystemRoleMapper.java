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

import cn.odboy.system.dal.dataobject.SystemRoleTb;
import cn.odboy.system.dal.model.SystemQueryRoleArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 角色 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemRoleMapper extends BaseMapper<SystemRoleTb> {
    List<SystemRoleTb> selectAllRole();

    List<SystemRoleTb> selectRoleByArgs(@Param("criteria") SystemQueryRoleArgs criteria);

    Long countRoleByArgs(@Param("criteria") SystemQueryRoleArgs criteria);

    SystemRoleTb getRoleByName(@Param("name") String name);

    SystemRoleTb getRoleById(@Param("roleId") Long roleId);

    List<SystemRoleTb> selectRoleByUserId(@Param("userId") Long userId);

    Integer countRoleByDeptIds(@Param("deptIds") Set<Long> deptIds);

    List<SystemRoleTb> selectRoleByMenuId(@Param("menuId") Long menuId);
}
