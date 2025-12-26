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
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.dal.dataobject.SystemJobTb;
import cn.odboy.system.dal.model.SystemCreateJobArgs;
import cn.odboy.system.dal.model.SystemQueryJobArgs;
import cn.odboy.system.dal.mysql.SystemJobMapper;
import cn.odboy.system.dal.mysql.SystemUserMapper;
import cn.odboy.util.KitFileUtil;
import cn.odboy.util.KitPageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemJobService {

  @Autowired
  private SystemJobMapper systemJobMapper;
  @Autowired
  private SystemUserMapper systemUserMapper;

  /**
   * 创建
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveJob(SystemCreateJobArgs args) {
    SystemJobTb job = this.getJobByName(args.getName());
    if (job != null) {
      throw new BadRequestException("职位名称已存在");
    }
    systemJobMapper.insert(BeanUtil.copyProperties(args, SystemJobTb.class));
  }

  /**
   * 编辑
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void modifyJobById(SystemJobTb args) {
    SystemJobTb job = systemJobMapper.selectById(args.getId());
    SystemJobTb old = this.getJobByName(args.getName());
    if (old != null && !old.getId().equals(args.getId())) {
      throw new BadRequestException("职位名称已存在");
    }
    args.setId(job.getId());
    systemJobMapper.insertOrUpdate(args);
  }

  /**
   * 删除
   *
   * @param ids /
   */
  @Transactional(rollbackFor = Exception.class)
  public void removeJobByIds(Set<Long> ids) {
    systemJobMapper.deleteByIds(ids);
  }

  /**
   * 导出数据
   *
   * @param jobs     待导出的数据
   * @param response /
   * @throws IOException
   */
  public void exportJobExcel(List<SystemJobTb> jobs, HttpServletResponse response) throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (SystemJobTb job : jobs) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("岗位名称", job.getName());
      map.put("岗位状态", job.getEnabled() ? "启用" : "停用");
      map.put("创建日期", job.getCreateTime());
      list.add(map);
    }
    KitFileUtil.downloadExcel(list, response);
  }

  /**
   * 分页查询
   *
   * @param args 条件
   * @param page 分页参数
   * @return
   */
  public KitPageResult<SystemJobTb> queryJobByArgs(SystemQueryJobArgs args, Page<SystemJobTb> page) {
    return KitPageUtil.toPage(this.selectJobByArgs(args, page));
  }

  /**
   * 查询全部数据
   *
   * @param args /
   * @return /
   */
  public List<SystemJobTb> queryJobByArgs(SystemQueryJobArgs args) {
    return this.selectJobByArgs(args);
  }

  /**
   * 验证是否被用户关联
   *
   * @param ids /
   */
  public void verifyBindRelationByIds(Set<Long> ids) {
    if (systemUserMapper.countUserByJobIds(ids) > 0) {
      throw new BadRequestException("所选的岗位中存在用户关联, 请解除关联再试！");
    }
  }


  public SystemJobTb getJobByName(String name) {
    LambdaQueryWrapper<SystemJobTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemJobTb::getName, name);
    return systemJobMapper.selectOne(wrapper);
  }

  public void injectQueryParams(SystemQueryJobArgs criteria, LambdaQueryWrapper<SystemJobTb> wrapper) {
    if (criteria != null) {
      wrapper.like(StrUtil.isNotBlank(criteria.getName()), SystemJobTb::getName, criteria.getName());
      wrapper.eq(criteria.getEnabled() != null, SystemJobTb::getEnabled, criteria.getEnabled());
      if (CollUtil.isNotEmpty(criteria.getCreateTime()) && criteria.getCreateTime().size() >= 2) {
        wrapper.between(SystemJobTb::getCreateTime, criteria.getCreateTime().get(0),
            criteria.getCreateTime().get(1));
      }
    }
    wrapper.orderByDesc(SystemJobTb::getJobSort, SystemJobTb::getId);
  }

  public IPage<SystemJobTb> selectJobByArgs(SystemQueryJobArgs criteria, Page<SystemJobTb> page) {
    LambdaQueryWrapper<SystemJobTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(criteria, wrapper);
    return systemJobMapper.selectPage(page, wrapper);
  }

  public List<SystemJobTb> selectJobByArgs(SystemQueryJobArgs criteria) {
    LambdaQueryWrapper<SystemJobTb> wrapper = new LambdaQueryWrapper<>();
    this.injectQueryParams(criteria, wrapper);
    return systemJobMapper.selectList(wrapper);
  }
}
