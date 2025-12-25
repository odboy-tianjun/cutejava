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
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.model.SystemQueryDeptArgs;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * 部门 Mapper
 *
 * @author odboy
 */
@Mapper
public interface SystemDeptMapper extends BaseMapper<SystemDeptTb> {

  default List<SystemDeptTb> selectDeptByPidIsNull() {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.isNull(SystemDeptTb::getPid);
    return selectList(wrapper);
  }

  default long countDeptByPid(Long pid) {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getPid, pid);
    return selectCount(wrapper);
  }

  default List<SystemDeptTb> queryDeptByArgs(SystemQueryDeptArgs criteria) {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    if (criteria != null) {
      wrapper.in(CollUtil.isNotEmpty(criteria.getIds()), SystemDeptTb::getId, criteria.getIds());
      wrapper.like(StrUtil.isNotBlank(criteria.getName()), SystemDeptTb::getName, criteria.getName());
      wrapper.eq(criteria.getEnabled() != null, SystemDeptTb::getEnabled, criteria.getEnabled());
      wrapper.eq(criteria.getPid() != null, SystemDeptTb::getPid, criteria.getPid());
      wrapper.isNull(criteria.getPidIsNull() != null, SystemDeptTb::getPid);
      if (CollUtil.isNotEmpty(criteria.getCreateTime()) && criteria.getCreateTime().size() >= 2) {
        wrapper.between(SystemDeptTb::getCreateTime, criteria.getCreateTime().get(0),
            criteria.getCreateTime().get(1));
      }
    }
    wrapper.orderByAsc(SystemDeptTb::getDeptSort);
    return selectList(wrapper);
  }

  default List<SystemDeptTb> selectEnabledDepts() {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getEnabled, 1);
    return selectList(wrapper);
  }

  default List<SystemDeptTb> selectDeptByPid(long pid) {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getPid, pid);
    return selectList(wrapper);
  }

  default void updateDeptSubCountById(long count, Long deptId) {
    LambdaUpdateWrapper<SystemDeptTb> wrapper = new LambdaUpdateWrapper<>();
    wrapper.eq(SystemDeptTb::getId, deptId);
    wrapper.set(SystemDeptTb::getSubCount, count);
    update(null, wrapper);
  }
}
