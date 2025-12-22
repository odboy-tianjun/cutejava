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

import cn.hutool.core.util.StrUtil;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import cn.odboy.system.dal.model.SystemQueryDictArgs;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 字典 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemDictMapper extends BaseMapper<SystemDictTb> {
    default void injectQueryParams(SystemQueryDictArgs args, LambdaQueryWrapper<SystemDictTb> wrapper) {
        if (args != null) {
            wrapper.and(StrUtil.isNotBlank(args.getBlurry()), c -> c.like(SystemDictTb::getName, args.getBlurry()).or()
                .like(SystemDictTb::getDescription, args.getBlurry()));
        }
    }
    default List<SystemDictTb> selectDictByArgs(SystemQueryDictArgs args) {
        LambdaQueryWrapper<SystemDictTb> wrapper = new LambdaQueryWrapper<>();
        injectQueryParams(args, wrapper);
        return selectList(wrapper);
    }
    default List<SystemDictTb> selectDictByArgs(SystemQueryDictArgs args, Page<SystemDictTb> page) {
        LambdaQueryWrapper<SystemDictTb> wrapper = new LambdaQueryWrapper<>();
        injectQueryParams(args, wrapper);
        return selectList(page, wrapper);
    }
}
