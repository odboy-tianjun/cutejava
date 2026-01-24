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

import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.application.model.CuteProductLineSelectVo;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CuteProductLineSelectService extends CuteBaseService {

  /**
   * 产品线选择组件 数据源
   *
   * @return /
   */
  public List<CuteProductLineSelectVo> listMetadata() {
    List<SystemDeptTb> depts = this.listEnabledDept();
    return this.buildDeptMetadata(depts);
  }

  private List<CuteProductLineSelectVo> buildDeptMetadata(List<SystemDeptTb> depts) {
    // 查询所有部门并按父子关系组织
    Map<Long, SystemDeptTb> deptMap =
        depts.stream().collect(Collectors.toMap(SystemDeptTb::getId, Function.identity()));
    List<CuteProductLineSelectVo> options = new ArrayList<>();
    for (SystemDeptTb dept : depts) {
      // 构建部门ID路径
      List<Long> pathIds = new ArrayList<>();
      this.buildDeptIdPath(dept, deptMap, pathIds);
      // 反转路径，从顶级部门到当前部门
      Collections.reverse(pathIds);
      CuteProductLineSelectVo dto = new CuteProductLineSelectVo();
      dto.setValue(Long.toString(dept.getId()));
      dto.setIdPath(pathIds.stream().map(String::valueOf).collect(Collectors.joining("-")));
      // 保留名称路径用于显示
      dto.setLabel(this.buildNamePath(dept, deptMap));
      options.add(dto);
    }
    // 按部门名称路径排序
    options.sort(Comparator.comparing(CuteProductLineSelectVo::getLabel));
    return options;
  }

  private void buildDeptIdPath(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap, List<Long> pathIds) {
    pathIds.add(dept.getId());
    if (dept.getPid() != null && dept.getPid() != 0 && deptMap.containsKey(dept.getPid())) {
      this.buildDeptIdPath(deptMap.get(dept.getPid()), deptMap, pathIds);
    }
  }

  private String buildNamePath(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap) {
    List<String> pathNames = new ArrayList<>();
    this.buildDeptNamePath(dept, deptMap, pathNames);
    Collections.reverse(pathNames);
    return String.join(" / ", pathNames);
  }

  private void buildDeptNamePath(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap, List<String> pathNames) {
    pathNames.add(dept.getName());
    if (dept.getPid() != null && dept.getPid() != 0 && deptMap.containsKey(dept.getPid())) {
      this.buildDeptNamePath(deptMap.get(dept.getPid()), deptMap, pathNames);
    }
  }
}
