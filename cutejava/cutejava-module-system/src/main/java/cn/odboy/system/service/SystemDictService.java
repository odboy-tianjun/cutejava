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
import cn.odboy.system.dal.dataobject.SystemDictTb;
import cn.odboy.system.dal.model.export.SystemDictExportRowVo;
import cn.odboy.system.dal.model.request.SystemCreateDictArgs;
import cn.odboy.system.dal.model.request.SystemQueryDictArgs;
import cn.odboy.system.dal.model.response.SystemDictDetailVo;
import cn.odboy.system.dal.mysql.SystemDictMapper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitPageUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SystemDictService {

  @Autowired
  private SystemDictMapper systemDictMapper;
  @Autowired
  private SystemDictDetailService systemDictDetailService;

  /**
   * 创建
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveDict(SystemCreateDictArgs args) {
    SystemDictTb dictTb = KitBeanUtil.copyToClass(args, SystemDictTb.class);
    systemDictMapper.insert(dictTb);
  }

  /**
   * 编辑
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateDictById(SystemDictTb args) {
    SystemDictTb dict = systemDictMapper.selectById(args.getId());
    dict.setName(args.getName());
    dict.setDescription(args.getDescription());
    systemDictMapper.insertOrUpdate(dict);
  }

  /**
   * 删除
   *
   * @param ids /
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteDictByIds(Set<Long> ids) {
    // 删除字典
    systemDictMapper.deleteByIds(ids);
    // 删除字典详情
    systemDictDetailService.deleteDictDetailByDictIds(ids);
  }

  /**
   * 分页查询
   *
   * @param args 条件
   * @param page 分页参数
   * @return /
   */
  public KitPageResult<SystemDictTb> searchDict(SystemQueryDictArgs args, Page<SystemDictTb> page) {
    LambdaQueryWrapper<SystemDictTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(args, wrapper);
    Page<SystemDictTb> selectPage = systemDictMapper.selectPage(page, wrapper);
    return KitPageUtil.toPage(selectPage);
  }

  /**
   * 复杂条件查询字典列表
   *
   * @param args /
   * @return /
   */
  public List<SystemDictTb> queryDictByArgs(SystemQueryDictArgs args) {
    LambdaQueryWrapper<SystemDictTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(args, wrapper);
    return systemDictMapper.selectList(wrapper);
  }

  /**
   * 导出字典数据
   *
   * @param response /
   * @param args     /
   */
  public void exportDictXlsx(HttpServletResponse response, SystemQueryDictArgs args) {
    List<SystemDictTb> systemDictTbs = this.queryDictByArgs(args);
    List<SystemDictExportRowVo> rowVos = new ArrayList<>();
    for (SystemDictTb dataObject : systemDictTbs) {
      List<SystemDictDetailVo> dictDetails = systemDictDetailService.listDictDetailByName(dataObject.getName());
      if (CollUtil.isEmpty(dictDetails)) {
        SystemDictExportRowVo rowVo = new SystemDictExportRowVo();
        rowVo.setName(dataObject.getName());
        rowVo.setDescription(dataObject.getDescription());
        rowVo.setCreateTime(dataObject.getCreateTime());
        rowVo.setLabel("");
        rowVo.setValue("");
        rowVos.add(rowVo);
        continue;
      }
      for (SystemDictDetailVo dictDetail : dictDetails) {
        SystemDictExportRowVo rowVo = new SystemDictExportRowVo();
        rowVo.setName(dataObject.getName());
        rowVo.setDescription(dataObject.getDescription());
        rowVo.setCreateTime(dataObject.getCreateTime());
        rowVo.setLabel(dictDetail.getLabel());
        rowVo.setValue(dictDetail.getValue());
        rowVos.add(rowVo);
      }
    }
    KitExcelExporter.exportSimple(response, "字典数据", SystemDictExportRowVo.class, rowVos);
  }

  /**
   * 构建查询条件
   *
   * @param args    /
   * @param wrapper /
   */
  private void injectQueryParams(SystemQueryDictArgs args, LambdaQueryWrapper<SystemDictTb> wrapper) {
    if (args != null) {
      wrapper.and(StrUtil.isNotBlank(args.getBlurry()), c -> c.like(SystemDictTb::getName, args.getBlurry()).or()
          .like(SystemDictTb::getDescription, args.getBlurry()));
    }
  }
}
