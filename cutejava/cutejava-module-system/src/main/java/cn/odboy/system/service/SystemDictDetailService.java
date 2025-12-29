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
package cn.odboy.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import cn.odboy.system.dal.model.SystemCreateDictDetailArgs;
import cn.odboy.system.dal.model.SystemDictDetailVo;
import cn.odboy.system.dal.model.SystemQueryDictDetailArgs;
import cn.odboy.system.dal.mysql.SystemDictDetailMapper;
import cn.odboy.system.dal.mysql.SystemDictMapper;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemDictDetailService {
  @Autowired
  private SystemDictDetailMapper systemDictDetailMapper;
  @Autowired
  private SystemDictMapper systemDictMapper;

  /**
   * 创建
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveDictDetail(SystemCreateDictDetailArgs args) {
    SystemDictDetailTb dictDetail = BeanUtil.copyProperties(args, SystemDictDetailTb.class);
    dictDetail.setDictId(args.getDict().getId());
    systemDictDetailMapper.insert(dictDetail);
  }

  /**
   * 编辑
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateDictDetailById(SystemDictDetailTb args) {
    SystemDictDetailTb dictDetail = systemDictDetailMapper.selectById(args.getId());
    args.setId(dictDetail.getId());
    systemDictDetailMapper.insertOrUpdate(args);
  }

  /**
   * 删除
   *
   * @param id /
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteDictDetailById(Long id) {
    systemDictDetailMapper.deleteById(id);
  }

  @Transactional(rollbackFor = Exception.class)
  public void deleteDictDetailByDictIds(Set<Long> ids) {
    if (CollUtil.isNotEmpty(ids)) {
      LambdaQueryWrapper<SystemDictDetailTb> wrapper = new LambdaQueryWrapper<>();
      wrapper.in(SystemDictDetailTb::getDictId, ids);
      systemDictDetailMapper.delete(wrapper);
    }
  }

  private List<SystemDictDetailTb> listDictDetailByDictId(Long dictId) {
    LambdaQueryWrapper<SystemDictDetailTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDictDetailTb::getDictId, dictId);
    return systemDictDetailMapper.selectList(wrapper);
  }

  private IPage<SystemDictDetailTb> searchDictDetailByArgs(List<Long> dictIds, Page<SystemDictDetailTb> page) {
    LambdaQueryWrapper<SystemDictDetailTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.in(CollUtil.isNotEmpty(dictIds), SystemDictDetailTb::getDictId, dictIds);
    return systemDictDetailMapper.selectPage(page, wrapper);
  }

  /**
   * 分页查询
   *
   * @param args 条件
   * @param page 分页参数
   * @return /
   */
  public KitPageResult<SystemDictDetailVo> searchDictDetail(SystemQueryDictDetailArgs args,
      Page<SystemDictDetailTb> page) {
    IPage<SystemDictDetailVo> iPage = new Page<>();
    List<Long> dictIds = new ArrayList<>();
    List<SystemDictTb> dictTbs = new ArrayList<>();

    if (args == null) {
      dictTbs.addAll(systemDictMapper.listAll());
      dictIds = dictTbs.stream().map(SystemDictTb::getId).collect(Collectors.toList());
    } else {
      String dictName = args.getDictName();
      if (StrUtil.isBlank(dictName)) {
        return KitPageUtil.toPage(iPage);
      }
      SystemDictTb dictTb = systemDictMapper.getByName(dictName);
      if (dictTb == null) {
        return KitPageUtil.toPage(iPage);
      } else {
        dictIds.add(dictTb.getId());
        dictTbs.add(dictTb);
      }
    }

    Map<Long, SystemDictTb> id2ItemMap = dictTbs.stream().collect(Collectors.toMap(SystemDictTb::getId, i -> i));
    iPage = this.searchDictDetailByArgs(dictIds, page)
        .convert(i -> BeanUtil.copyProperties(i, SystemDictDetailVo.class));
    for (SystemDictDetailVo record : iPage.getRecords()) {
      record.setDict(id2ItemMap.get(record.getDictId()));
    }
    return KitPageUtil.toPage(iPage);
  }

  /**
   * 根据字典名称获取字典详情
   *
   * @param name 字典名称
   * @return /
   */
  public List<SystemDictDetailVo> listDictDetailByName(String name) {
    if (StrUtil.isBlank(name)) {
      return new ArrayList<>();
    }
    SystemDictTb systemDictTb = systemDictMapper.getByName(name);
    if (systemDictTb == null) {
      return new ArrayList<>();
    }
    return this.listDictDetailByDictId(systemDictTb.getId()).stream().map(m -> {
      SystemDictDetailVo detailVo = BeanUtil.copyProperties(m, SystemDictDetailVo.class);
      detailVo.setDict(systemDictTb);
      return detailVo;
    }).collect(Collectors.toList());
  }
}
