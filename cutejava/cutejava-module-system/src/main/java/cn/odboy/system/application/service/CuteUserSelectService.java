/*
 * Copyright 2021-2026 Odboy
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
package cn.odboy.system.application.service;

import cn.hutool.core.collection.CollUtil;
import cn.odboy.system.application.model.CuteUserSelectVo;
import cn.odboy.system.dal.dataobject.SystemUserTb;
import cn.odboy.system.dal.model.request.SystemQueryUserArgs;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuteUserSelectService {

  @Autowired
  private SystemUserMapper systemUserMapper;

  /**
   * 模糊查询用户基础信息，限制返回的条数
   *
   * @param args 查询参数
   * @return /
   */
  public List<CuteUserSelectVo> listMetadata(SystemQueryUserArgs args) {
    LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.select(SystemUserTb::getUsername, SystemUserTb::getNickName, SystemUserTb::getDeptId, SystemUserTb::getEmail, SystemUserTb::getPhone);
    wrapper.and(c -> {
      c.eq(SystemUserTb::getPhone, args.getBlurry());
      c.or();
      c.eq(SystemUserTb::getEmail, args.getBlurry());
      c.or();
      c.like(SystemUserTb::getUsername, args.getBlurry());
      c.or();
      c.like(SystemUserTb::getNickName, args.getBlurry());
    });
    return systemUserMapper.selectPage(new Page<>(1, 50), wrapper).convert(m -> {
      CuteUserSelectVo selectVo = new CuteUserSelectVo();
      selectVo.setValue(m.getUsername());
      selectVo.setLabel(m.getNickName());
      selectVo.setDeptId(Long.toString(m.getDeptId()));
      selectVo.setEmail(m.getEmail());
      selectVo.setPhone(m.getPhone());
      return selectVo;
    }).getRecords();
  }

  public List<CuteUserSelectVo> listMetadataByUsernames(List<String> usernameList) {
    if (CollUtil.isEmpty(usernameList)) {
      return new ArrayList<>();
    }
    LambdaQueryWrapper<SystemUserTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.select(SystemUserTb::getUsername, SystemUserTb::getNickName, SystemUserTb::getDeptId, SystemUserTb::getEmail, SystemUserTb::getPhone);
    wrapper.in(SystemUserTb::getUsername, usernameList);
    return systemUserMapper.selectList(wrapper).stream().map(m -> {
      CuteUserSelectVo selectVo = new CuteUserSelectVo();
      selectVo.setValue(m.getUsername());
      selectVo.setLabel(m.getNickName());
      selectVo.setDeptId(Long.toString(m.getDeptId()));
      selectVo.setEmail(m.getEmail());
      selectVo.setPhone(m.getPhone());
      return selectVo;
    }).collect(Collectors.toList());
  }
}
