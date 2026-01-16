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
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.constant.SystemDataScopeEnum;
import cn.odboy.system.constant.SystemZhConst;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.model.export.SystemDeptExportRowVo;
import cn.odboy.system.dal.model.request.SystemCreateDeptArgs;
import cn.odboy.system.dal.model.request.SystemQueryDeptArgs;
import cn.odboy.system.dal.model.response.SystemDeptVo;
import cn.odboy.system.dal.model.response.SystemProductLineTreeVo;
import cn.odboy.system.dal.model.response.SystemProductLineVo;
import cn.odboy.system.dal.mysql.SystemDeptMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitBeanUtil;
import cn.odboy.util.KitClassUtil;
import cn.odboy.util.KitValidUtil;
import cn.odboy.util.xlsx.KitExcelExporter;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SystemDeptService {

  @Autowired
  private SystemDeptMapper systemDeptMapper;
  @Autowired
  private SystemUserDeptService systemUserDeptService;
  @Autowired
  private SystemRoleDeptService systemRoleDeptService;

  /**
   * 创建 -> TestPassed
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveDept(SystemCreateDeptArgs args) {
    KitValidUtil.notNull(args);

    SystemDeptTb record = KitBeanUtil.copyToClass(args, SystemDeptTb.class);
    systemDeptMapper.insert(record);

    this.updateDeptSubCnt(args.getPid());
  }

  /**
   * 编辑 -> TestPassed
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateDeptById(SystemDeptTb args) {
    KitValidUtil.notNull(args);
    // 旧的父部门
    Long oldPid = this.getDeptById(args.getId()).getPid();
    // 新的父部门
    Long newPid = args.getPid();
    if (args.getPid() != null && args.getId().equals(args.getPid())) {
      throw new BadRequestException("上级不能为自己");
    }
    SystemDeptTb dept = systemDeptMapper.selectById(args.getId());
    args.setId(dept.getId());
    systemDeptMapper.updateById(args);
    this.updateDeptSubCnt(oldPid);
    this.updateDeptSubCnt(newPid);
  }

  /**
   * 删除
   *
   * @param ids /
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteDeptByIds(Set<Long> ids) {
    // 查询部门, 和其所有子部门
    Set<SystemDeptVo> depts = this.traverseDeptByIdWithPids(ids);
    // 验证是否被角色或用户关联
    this.verifyBindRelationByIds(depts);
    for (SystemDeptVo dept : depts) {
      systemDeptMapper.deleteById(dept.getId());
      this.updateDeptSubCnt(dept.getPid());
    }
  }

  /**
   * 更新父节点中子节点数目 -> TestPassed
   *
   * @param deptId 部门id
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateDeptSubCnt(Long deptId) {
    if (deptId != null) {
      long count = this.countDeptByPid(deptId);
      systemDeptMapper.updateDeptSubCountById(count, deptId);
    }
  }

  /**
   * 查询所有数据
   *
   * @param args    /
   * @param isQuery /
   * @return /
   * @throws Exception /
   */
  public List<SystemDeptTb> queryAllDeptByArgs(SystemQueryDeptArgs args, Boolean isQuery) throws Exception {
    String dataScopeType = KitSecurityHelper.getDataScopeType();
    if (isQuery) {
      if (dataScopeType.equals(SystemDataScopeEnum.ALL.getValue())) {
        args.setPidIsNull(true);
      }
      List<Field> fields = KitClassUtil.getAllFields(args.getClass(), new ArrayList<>());
      List<String> fieldNames = new ArrayList<>() {{
        add("pidIsNull");
        add("enabled");
      }};
      for (Field field : fields) {
        // 设置对象的访问权限, 保证对private的属性的访问
        field.setAccessible(true);
        Object val = field.get(args);
        if (fieldNames.contains(field.getName())) {
          continue;
        }
        if (ObjectUtil.isNotNull(val)) {
          args.setPidIsNull(null);
          break;
        }
      }
    }
    // 数据权限
    args.setIds(KitSecurityHelper.getCurrentUserDataScope());
    List<SystemDeptTb> list = this.queryDeptByArgs(args);
    // 如果为空, 就代表为自定义权限或者本级权限, 就需要去重, 不理解可以注释掉，看查询结果
    if (StrUtil.isBlank(dataScopeType)) {
      return this.filterRootDeptList(list);
    }
    return list;
  }

  /**
   * 根据ID查询
   *
   * @param id /
   * @return /
   */
  public SystemDeptVo getDeptVoById(Long id) {
    return KitBeanUtil.copyToClass(systemDeptMapper.selectById(id), SystemDeptVo.class);
  }

  /**
   * 根据部门id遍历所有部门和子部门id集合
   *
   * @param deptList   部门集合
   * @param deptPidMap 父部门id和子部门集合的映射关系
   */
  public List<Long> queryChildDeptIdByDeptIds(List<SystemDeptTb> deptList, Map<Long, List<SystemDeptTb>> deptPidMap) {
    List<Long> list = new ArrayList<>();
    for (SystemDeptTb systemDeptTb : deptList) {
      if (systemDeptTb != null && systemDeptTb.getEnabled()) {
        List<SystemDeptTb> deptList1 = deptPidMap.getOrDefault(systemDeptTb.getId(), null);
        if (CollUtil.isNotEmpty(deptList1)) {
          list.addAll(this.queryChildDeptIdByDeptIds(deptList1, deptPidMap));
        }
        list.add(systemDeptTb.getId());
      }
    }
    return list;
  }

  /**
   * 构建树形数据
   *
   * @return /
   */
  public KitPageResult<SystemDeptVo> searchDeptTree(List<Long> ids, Boolean exclude) {
    Set<SystemDeptVo> deptSet1 = new LinkedHashSet<>();
    for (Long id : ids) {
      // 同级数据
      SystemDeptVo dept = this.getDeptVoById(id);
      // 上级数据
      List<SystemDeptVo> depts = this.querySuperiorDeptByPid(dept, new ArrayList<>());
      if (exclude) {
        for (SystemDeptVo data : depts) {
          if (data.getId().equals(dept.getPid())) {
            data.setSubCount(data.getSubCount() - 1);
          }
        }
        // 编辑部门时不显示自己以及自己下级的数据, 避免出现PID数据环形问题
        depts = depts.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toList());
      }
      deptSet1.addAll(depts);
    }
    List<SystemDeptVo> deptList = new ArrayList<>(deptSet1);
    // 构建部门树
    Set<SystemDeptVo> trees = new LinkedHashSet<>();
    Set<SystemDeptVo> deptSet = new LinkedHashSet<>();
    List<String> deptNames = deptList.stream().map(SystemDeptVo::getName).collect(Collectors.toList());
    boolean isChild;
    for (SystemDeptVo dept : deptList) {
      isChild = false;
      if (dept.getPid() == null) {
        trees.add(dept);
      }
      for (SystemDeptVo it : deptList) {
        if (it.getPid() != null && dept.getId().equals(it.getPid())) {
          isChild = true;
          if (dept.getChildren() == null) {
            dept.setChildren(new ArrayList<>());
          }
          dept.getChildren().add(it);
        }
      }
      if (isChild) {
        deptSet.add(dept);
      } else if (dept.getPid() != null && !deptNames.contains(this.getDeptVoById(dept.getPid()).getName())) {
        deptSet.add(dept);
      }
    }
    if (CollUtil.isEmpty(trees)) {
      trees = deptSet;
    }
    KitPageResult<SystemDeptVo> baseResult = new KitPageResult<>();
    baseResult.setContent(CollUtil.isEmpty(trees) ? new ArrayList<>(deptSet) : new ArrayList<>(trees));
    baseResult.setTotalElements(deptSet.size());
    return baseResult;
  }

  /**
   * 验证是否被角色或用户关联
   *
   * @param deptSet /
   */
  public void verifyBindRelationByIds(Set<SystemDeptVo> deptSet) {
    Set<Long> deptIds = deptSet.stream().map(SystemDeptVo::getId).collect(Collectors.toSet());
    if (systemUserDeptService.countUserByDeptIds(deptIds) > 0) {
      throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
    }
    if (systemRoleDeptService.countRoleByDeptIds(deptIds) > 0) {
      throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
    }
  }

  /**
   * 根据部门id遍历所有部门和子部门
   *
   * @param ids 部门id集合
   */
  public Set<SystemDeptVo> traverseDeptByIdWithPids(Set<Long> ids) {
    Set<SystemDeptVo> depts = new HashSet<>();
    for (Long id : ids) {
      // 根部门
      depts.add(this.getDeptVoById(id));
      // 子部门
      List<SystemDeptVo> deptList = this.listDeptVoByPid(id);
      if (CollUtil.isNotEmpty(deptList)) {
        this.queryRelationDeptByArgs(deptList, depts);
      }
    }
    return depts;
  }

  /**
   * 产品线选择组件 数据源
   *
   * @return /
   */
  public List<SystemProductLineVo> listDeptSelectDataSource() {
    List<SystemDeptTb> depts = this.listEnabledDepts();
    return this.buildDeptSelectOptions(depts);
  }

  /**
   * 产品线选择组件Pro版 数据源
   *
   * @return /
   */
  public List<SystemProductLineTreeVo> listDeptSelectProDataSource() {
    List<SystemDeptTb> depts = this.listEnabledDepts();
    // 查询所有部门并按父子关系组织
    Map<Long, SystemDeptTb> deptMap =
        depts.stream().collect(Collectors.toMap(SystemDeptTb::getId, Function.identity()));
    List<SystemProductLineTreeVo> options = new ArrayList<>();
    // 构建树形结构
    for (SystemDeptTb dept : depts) {
      // 只处理顶级部门（pid为null或0的部门）
      if (dept.getPid() == null || dept.getPid() == 0) {
        SystemProductLineTreeVo vo = this.buildDeptTreePro(dept, deptMap);
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
   * 根据父部门id查询部门集合
   *
   * @param pid 父部门id
   * @return /
   */
  public List<SystemDeptTb> listDeptByPid(long pid) {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getPid, pid);
    return systemDeptMapper.selectList(wrapper);
  }

  /**
   * 导出部门数据
   *
   * @param response /
   * @param args     /
   */
  public void exportDeptXlsx(HttpServletResponse response, SystemQueryDeptArgs args) throws Exception {
    List<SystemDeptTb> systemDeptTbs = this.queryAllDeptByArgs(args, false);
    List<SystemDeptExportRowVo> rowVos = new ArrayList<>();
    for (SystemDeptTb dataObject : systemDeptTbs) {
      SystemDeptExportRowVo rowVo = new SystemDeptExportRowVo();
      rowVo.setName(dataObject.getName());
      rowVo.setEnabled(dataObject.getEnabled() ? SystemZhConst.ENABLE_STR : SystemZhConst.DISABLE_STR);
      rowVo.setCreateTime(dataObject.getCreateTime());
      rowVos.add(rowVo);
    }
    KitExcelExporter.exportSimple(response, "部门数据", SystemDeptExportRowVo.class, rowVos);
  }

  /**
   * 查询父部门非空且启用的部门数据
   *
   * @return /
   */
  public List<SystemDeptTb> listPidNonNull() {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getEnabled, 1);
    wrapper.isNotNull(SystemDeptTb::getPid);
    return systemDeptMapper.selectList(wrapper);
  }

  /**
   * 递归构建部门树
   *
   * @param dept    当前部门
   * @param deptMap 部门映射
   * @return SystemProductLineTreeVo 树节点
   */
  private SystemProductLineTreeVo buildDeptTreePro(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap) {
    SystemProductLineTreeVo vo = new SystemProductLineTreeVo();
    vo.setValue(String.valueOf(dept.getId()));
    vo.setLabel(dept.getName());
    // 查找子部门
    List<SystemProductLineTreeVo> children = new ArrayList<>();
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

  /**
   * 产品线选择组件使用
   *
   * @param depts 部门集合
   * @return /
   */
  private List<SystemProductLineVo> buildDeptSelectOptions(List<SystemDeptTb> depts) {
    // 查询所有部门并按父子关系组织
    Map<Long, SystemDeptTb> deptMap =
        depts.stream().collect(Collectors.toMap(SystemDeptTb::getId, Function.identity()));
    List<SystemProductLineVo> options = new ArrayList<>();
    for (SystemDeptTb dept : depts) {
      // 构建部门ID路径
      List<Long> pathIds = new ArrayList<>();
      this.buildDeptIdPath(dept, deptMap, pathIds);
      // 反转路径，从顶级部门到当前部门
      Collections.reverse(pathIds);
      SystemProductLineVo dto = new SystemProductLineVo();
      dto.setValue(dept.getId());
      dto.setIdPath(pathIds.stream().map(String::valueOf).collect(Collectors.joining("-")));
      // 保留名称路径用于显示
      dto.setLabel(this.buildNamePath(dept, deptMap));
      options.add(dto);
    }
    // 按部门名称路径排序
    options.sort(Comparator.comparing(SystemProductLineVo::getLabel));
    return options;
  }

  /**
   * 产品线选择组件使用
   */
  private void buildDeptIdPath(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap, List<Long> pathIds) {
    pathIds.add(dept.getId());
    if (dept.getPid() != null && dept.getPid() != 0 && deptMap.containsKey(dept.getPid())) {
      this.buildDeptIdPath(deptMap.get(dept.getPid()), deptMap, pathIds);
    }
  }

  /**
   * 产品线选择组件使用
   */
  private String buildNamePath(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap) {
    List<String> pathNames = new ArrayList<>();
    this.buildDeptNamePath(dept, deptMap, pathNames);
    Collections.reverse(pathNames);
    return String.join(" / ", pathNames);
  }

  /**
   * 产品线选择组件使用
   */
  private void buildDeptNamePath(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap, List<String> pathNames) {
    pathNames.add(dept.getName());
    if (dept.getPid() != null && dept.getPid() != 0 && deptMap.containsKey(dept.getPid())) {
      this.buildDeptNamePath(deptMap.get(dept.getPid()), deptMap, pathNames);
    }
  }

  /**
   * 查询启用的所有部门集合
   *
   * @return /
   */
  private List<SystemDeptTb> listEnabledDepts() {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getEnabled, 1);
    return systemDeptMapper.selectList(wrapper);
  }

  /**
   * 查询部门下所有关联的部门
   *
   * @param deptTbList /
   * @param depts      /
   */
  private void queryRelationDeptByArgs(List<SystemDeptVo> deptTbList, Set<SystemDeptVo> depts) {
    for (SystemDeptVo dept : deptTbList) {
      depts.add(dept);
      List<SystemDeptVo> deptList = this.listDeptVoByPid(dept.getId());
      if (CollUtil.isNotEmpty(deptList)) {
        this.queryRelationDeptByArgs(deptList, depts);
      }
    }
  }

  private List<SystemDeptVo> listDeptVoByPid(Long pid) {
    return KitBeanUtil.copyToList(this.listDeptByPid(pid), SystemDeptVo.class);
  }

  /**
   * 根据ID查询同级与上级数据
   *
   * @param dept     /
   * @param deptList /
   * @return /
   */
  private List<SystemDeptVo> querySuperiorDeptByPid(SystemDeptVo dept, List<SystemDeptVo> deptList) {
    if (dept.getPid() == null) {
      deptList.addAll(this.listRootDept());
      return deptList;
    }
    deptList.addAll(this.listDeptVoByPid(dept.getPid()));
    return querySuperiorDeptByPid(this.getDeptVoById(dept.getPid()), deptList);
  }

  /**
   * 查询所有根部门
   *
   * @return /
   */
  private List<SystemDeptVo> listRootDept() {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.isNull(SystemDeptTb::getPid);
    return KitBeanUtil.copyToList(systemDeptMapper.selectList(wrapper), SystemDeptVo.class);
  }

  /**
   * 根据部门id查询部门信息 -> TestPassed
   *
   * @param id 部门id
   * @return /
   */
  private SystemDeptTb getDeptById(Long id) {
    return systemDeptMapper.selectById(id);
  }

  /**
   * 根据父部门id统计数量
   *
   * @param pid 父部门id
   * @return /
   */
  private long countDeptByPid(Long pid) {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getPid, pid);
    return systemDeptMapper.selectCount(wrapper);
  }

  /**
   * 筛选出 list 中没有父部门（即 pid 不在其他部门 id 列表中的部门）的部门列表
   */
  private List<SystemDeptTb> filterRootDeptList(List<SystemDeptTb> list) {
    List<SystemDeptTb> deptList = new ArrayList<>();
    // 使用 Set 存储所有部门的 id
    Set<Long> idSet = list.stream().map(SystemDeptTb::getId).collect(Collectors.toSet());
    // 遍历部门列表, 筛选出没有父部门的部门
    for (SystemDeptTb dept : list) {
      if (!idSet.contains(dept.getPid())) {
        deptList.add(dept);
      }
    }
    return deptList;
  }

  /**
   * 根据复杂条件查询部门集合
   *
   * @param args /
   * @return /
   */
  private List<SystemDeptTb> queryDeptByArgs(SystemQueryDeptArgs args) {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    if (args != null) {
      wrapper.in(CollUtil.isNotEmpty(args.getIds()), SystemDeptTb::getId, args.getIds());
      wrapper.like(StrUtil.isNotBlank(args.getName()), SystemDeptTb::getName, args.getName());
      wrapper.eq(args.getEnabled() != null, SystemDeptTb::getEnabled, args.getEnabled());
      wrapper.eq(args.getPid() != null, SystemDeptTb::getPid, args.getPid());
      wrapper.isNull(args.getPidIsNull() != null, SystemDeptTb::getPid);
      if (CollUtil.isNotEmpty(args.getCreateTime()) && args.getCreateTime().size() >= 2) {
        wrapper.between(SystemDeptTb::getCreateTime, args.getCreateTime().get(0), args.getCreateTime().get(1));
      }
    }
    wrapper.orderByAsc(SystemDeptTb::getDeptSort);
    return systemDeptMapper.selectList(wrapper);
  }
}
