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

import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.model.SystemQueryMenuArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 菜单 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemMenuMapper extends BaseMapper<SystemMenuTb> {

    List<SystemMenuTb> selectMenuByArgs(@Param("criteria") SystemQueryMenuArgs criteria);

    LinkedHashSet<SystemMenuTb> selectMenuByRoleIdsAndType(@Param("roleIds") Set<Long> roleIds, @Param("type") Integer type);

    List<SystemMenuTb> selectMenuByPidIsNullOrderByMenuSort();

    List<SystemMenuTb> selectMenuByPidOrderByMenuSort(@Param("pid") Long pid);

    SystemMenuTb getMenuByTitle(@Param("title") String title);

    SystemMenuTb getMenuByComponentName(@Param("name") String name);

    Integer countMenuByPid(@Param("pid") Long pid);

    void updateMenuSubCntByMenuId(@Param("count") int count, @Param("menuId") Long menuId);
}
