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
package cn.odboy.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.system.dal.dataobject.SystemDictDetailTb;
import cn.odboy.system.dal.dataobject.SystemDictTb;
import cn.odboy.system.dal.model.request.SystemCreateDictDetailArgs;
import cn.odboy.system.dal.model.request.SystemQueryDictDetailArgs;
import cn.odboy.system.dal.model.response.SystemDictDetailVo;
import cn.odboy.system.dal.mysql.SystemDictDetailMapper;
import cn.odboy.system.dal.mysql.SystemDictMapper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitPageUtil;
import cn.odboy.util.KitValidUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SystemDictDetailService {

  @Autowired
  private SystemDictDetailMapper systemDictDetailMapper;
  @Autowired
  private SystemDictMapper systemDictMapper;

  /**
   * 创建 -> TestPassed
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveDictDetail(SystemCreateDictDetailArgs args) {
    KitValidUtil.notNull(args);
    KitValidUtil.notNull(args.getDict());
    SystemDictDetailTb dictDetail = KitBeanUtil.copyToClass(args, SystemDictDetailTb.class);
    dictDetail.setDictId(args.getDict().getId());
    systemDictDetailMapper.insert(dictDetail);
  }

  /**
   * 编辑 -> TestPassed
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
   * 删除 -> TestPassed
   *
   * @param id /
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteDictDetailById(Long id) {
    systemDictDetailMapper.deleteById(id);
  }

  /**
   * 根据字典id集合删除字典明细
   *
   * @param ids 字典id集合
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteDictDetailByDictIds(Set<Long> ids) {
    if (CollUtil.isNotEmpty(ids)) {
      LambdaQueryWrapper<SystemDictDetailTb> wrapper = new LambdaQueryWrapper<>();
      wrapper.in(SystemDictDetailTb::getDictId, ids);
      systemDictDetailMapper.delete(wrapper);
    }
  }

  /**
   * 分页查询 -> TestPassed
   *
   * @param args 条件
   * @param page 分页参数
   * @return /
   */
  public KitPageResult<SystemDictDetailVo> searchDictDetail(
      SystemQueryDictDetailArgs args,
      Page<SystemDictDetailTb> page
  ) {
    IPage<SystemDictDetailVo> iPage = systemDictDetailMapper.selectPageByArgs(page, args);
    List<Long> dictIds =
        iPage.getRecords().stream().map(SystemDictDetailVo::getDictId).collect(Collectors.toList());
    if (CollUtil.isNotEmpty(dictIds)) {
      Map<Long, SystemDictTb> id2ItemMap =
          systemDictMapper.selectByIds(dictIds).stream().collect(Collectors.toMap(SystemDictTb::getId, i -> i));
      for (SystemDictDetailVo record : iPage.getRecords()) {
        record.setDict(id2ItemMap.get(record.getDictId()));
      }
    }
    return KitPageUtil.toPage(iPage);
  }

  /**
   * 根据字典名称查询字典详情 -> TestPassed
   *
   * @param name 字典名称
   * @return /
   */
  public List<SystemDictDetailVo> listDictDetailByName(String name) {
    if (StrUtil.isBlank(name)) {
      return new ArrayList<>();
    }
    List<SystemDictDetailVo> detailVos = systemDictDetailMapper.listDictDetailByName(name);
    if (CollUtil.isNotEmpty(detailVos)) {
      SystemDictDetailVo systemDictDetailVo = detailVos.stream().findFirst().orElse(null);
      if (systemDictDetailVo != null) {
        SystemDictTb systemDictTb = systemDictMapper.selectById(systemDictDetailVo.getDictId());
        if (systemDictTb != null) {
          for (SystemDictDetailVo detailVo : detailVos) {
            detailVo.setDict(systemDictTb);
          }
        }
      }
    }
    return detailVos;
  }
}
