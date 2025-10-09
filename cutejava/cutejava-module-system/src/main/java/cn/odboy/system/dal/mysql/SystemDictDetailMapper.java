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

import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.model.SystemQueryDictDetailArgs;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 字典明细 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemDictDetailMapper extends BaseMapper<SystemDictDetailTb> {
    IPage<SystemDictDetailTb> selectDictDetailByArgs(@Param("criteria") SystemQueryDictDetailArgs criteria, Page<Object> page);

    List<SystemDictDetailTb> selectDictDetailByArgs(@Param("criteria") SystemQueryDictDetailArgs criteria);

    void deleteDictDetailByDictIds(@Param("dictIds") Set<Long> dictIds);
}
