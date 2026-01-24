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

import cn.odboy.system.application.model.CuteProductLineSelectProVo;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CuteProductLineSelectProService extends CuteBaseService {

  /**
   * 产品线选择组件Pro版 数据源
   *
   * @return /
   */
  public List<CuteProductLineSelectProVo> listMetadata() {
    List<SystemDeptTb> depts = this.listEnabledDept();
    // 查询所有部门并按父子关系组织
    Map<Long, SystemDeptTb> deptMap =
        depts.stream().collect(Collectors.toMap(SystemDeptTb::getId, Function.identity()));
    List<CuteProductLineSelectProVo> options = new ArrayList<>();
    // 构建树形结构
    for (SystemDeptTb dept : depts) {
      // 只处理顶级部门（pid为null或0的部门）
      if (dept.getPid() == null || dept.getPid() == 0) {
        CuteProductLineSelectProVo vo = this.buildDeptTreePro(dept, deptMap);
        options.add(vo);
      }
    }
    // 按排序字段排序
    options.sort(Comparator.comparingInt(o -> {
      Long id = Long.valueOf(o.getValue());
      return deptMap.get(id) != null ? deptMap.get(id).getDeptSort() : 0;
    }));
    return options;
  }

  /**
   * 递归构建部门树
   *
   * @param dept    当前部门
   * @param deptMap 部门映射
   * @return CuteProductLineSelectProVo 树节点
   */
  private CuteProductLineSelectProVo buildDeptTreePro(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap) {
    CuteProductLineSelectProVo vo = new CuteProductLineSelectProVo();
    vo.setValue(String.valueOf(dept.getId()));
    vo.setLabel(dept.getName());
    // 查找子部门
    List<CuteProductLineSelectProVo> children = new ArrayList<>();
    for (SystemDeptTb childDept : deptMap.values()) {
      if (dept.getId().equals(childDept.getPid())) {
        children.add(this.buildDeptTreePro(childDept, deptMap));
      }
    }
    // 子部门按排序字段排序
    children.sort(Comparator.comparingInt(o -> {
      Long id = Long.valueOf(o.getValue());
      return deptMap.get(id) != null ? deptMap.get(id).getDeptSort() : 0;
    }));
    vo.setChildren(children);
    return vo;
  }
}
