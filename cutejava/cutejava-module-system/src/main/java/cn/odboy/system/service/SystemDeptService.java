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
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageResult;
import cn.odboy.framework.exception.BadRequestException;
import cn.odboy.system.constant.SystemDataScopeEnum;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.model.SystemCreateDeptArgs;
import cn.odboy.system.dal.model.SystemProductLineTreeVo;
import cn.odboy.system.dal.model.SystemProductLineVo;
import cn.odboy.system.dal.model.SystemQueryDeptArgs;
import cn.odboy.system.dal.mysql.SystemDeptMapper;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.util.KitClassUtil;
import cn.odboy.util.KitFileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SystemDeptService {

  @Autowired
  private SystemDeptMapper systemDeptMapper;
  @Autowired
  private SystemUserService systemUserService;
  @Autowired
  private SystemRoleService systemRoleService;

  /**
   * 创建
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void saveDept(SystemCreateDeptArgs args) {
    systemDeptMapper.insert(BeanUtil.copyProperties(args, SystemDeptTb.class));
    this.updateDeptSubCnt(args.getPid());
  }

  /**
   * 编辑
   *
   * @param args /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateDept(SystemDeptTb args) {
    // 旧的部门
    Long oldPid = this.getDeptById(args.getId()).getPid();
    Long newPid = args.getPid();
    if (args.getPid() != null && args.getId().equals(args.getPid())) {
      throw new BadRequestException("上级不能为自己");
    }
    SystemDeptTb dept = systemDeptMapper.selectById(args.getId());
    args.setId(dept.getId());
    systemDeptMapper.insertOrUpdate(args);
    this.updateDeptSubCnt(oldPid);
    this.updateDeptSubCnt(newPid);
  }

  /**
   * 删除
   *
   * @param deptSet /
   */
  @Transactional(rollbackFor = Exception.class)
  public void deleteDeptByIds(Set<SystemDeptTb> deptSet) {
    for (SystemDeptTb dept : deptSet) {
      systemDeptMapper.deleteById(dept.getId());
      this.updateDeptSubCnt(dept.getPid());
    }
  }

  /**
   * 导出数据
   *
   * @param deptList 待导出的数据
   * @param response /
   * @throws IOException /
   */
  public void exportDeptExcel(List<SystemDeptTb> deptList, HttpServletResponse response) throws IOException {
    List<Map<String, Object>> list = new ArrayList<>();
    for (SystemDeptTb dept : deptList) {
      Map<String, Object> map = new LinkedHashMap<>();
      map.put("部门名称", dept.getName());
      map.put("部门状态", dept.getEnabled() ? "启用" : "停用");
      map.put("创建日期", dept.getCreateTime());
      list.add(map);
    }
    KitFileUtil.downloadExcel(list, response);
  }

  /**
   * 更新父节点中子节点数目
   *
   * @param deptId /
   */
  @Transactional(rollbackFor = Exception.class)
  public void updateDeptSubCnt(Long deptId) {
    if (deptId != null) {
      long count = this.countDeptByPid(deptId);
      systemDeptMapper.updateDeptSubCountById(count, deptId);
    }
  }

  private long countDeptByPid(Long pid) {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getPid, pid);
    return systemDeptMapper.selectCount(wrapper);
  }

  /**
   * 筛选出 list 中没有父部门（即 pid 不在其他部门 id 列表中的部门）的部门列表
   */
  private List<SystemDeptTb> deduplication(List<SystemDeptTb> list) {
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

  private List<SystemDeptTb> queryDeptByArgs(SystemQueryDeptArgs criteria) {
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
    return systemDeptMapper.selectList(wrapper);
  }

  /**
   * 查询所有数据
   *
   * @param criteria 条件
   * @param isQuery  /
   * @return /
   * @throws Exception /
   */
  public List<SystemDeptTb> queryAllDeptByArgs(SystemQueryDeptArgs criteria, Boolean isQuery) throws Exception {
    String dataScopeType = KitSecurityHelper.getDataScopeType();
    if (isQuery) {
      if (dataScopeType.equals(SystemDataScopeEnum.ALL.getValue())) {
        criteria.setPidIsNull(true);
      }
      List<Field> fields = KitClassUtil.getAllFields(criteria.getClass(), new ArrayList<>());
      List<String> fieldNames = new ArrayList<>() {{
        add("pidIsNull");
        add("enabled");
      }};
      for (Field field : fields) {
        // 设置对象的访问权限, 保证对private的属性的访问
        field.setAccessible(true);
        Object val = field.get(criteria);
        if (fieldNames.contains(field.getName())) {
          continue;
        }
        if (ObjectUtil.isNotNull(val)) {
          criteria.setPidIsNull(null);
          break;
        }
      }
    }
    // 数据权限
    criteria.setIds(KitSecurityHelper.getCurrentUserDataScope());
    List<SystemDeptTb> list = this.queryDeptByArgs(criteria);
    // 如果为空, 就代表为自定义权限或者本级权限, 就需要去重, 不理解可以注释掉，看查询结果
    if (StrUtil.isBlank(dataScopeType)) {
      return this.deduplication(list);
    }
    return list;
  }

  /**
   * 根据ID查询
   *
   * @param id /
   * @return /
   */
  public SystemDeptTb getDeptById(Long id) {
    return systemDeptMapper.selectById(id);
  }

  /**
   * 获取部门下所有关联的部门
   *
   * @param deptTbList /
   * @param depts      /
   * @return /
   */
  private Set<SystemDeptTb> queryRelationDeptByArgs(List<SystemDeptTb> deptTbList, Set<SystemDeptTb> depts) {
    for (SystemDeptTb dept : deptTbList) {
      depts.add(dept);
      List<SystemDeptTb> deptList = this.listDeptByPid(dept.getId());
      if (CollUtil.isNotEmpty(deptList)) {
        queryRelationDeptByArgs(deptList, depts);
      }
    }
    return depts;
  }

  public List<Long> queryChildDeptIdByDeptIds(List<SystemDeptTb> deptList) {
    List<Long> list = new ArrayList<>();
    for (SystemDeptTb systemDeptTb : deptList) {
      if (systemDeptTb != null && systemDeptTb.getEnabled()) {
        List<SystemDeptTb> deptList1 = this.listDeptByPid(systemDeptTb.getId());
        if (CollUtil.isNotEmpty(deptList1)) {
          list.addAll(queryChildDeptIdByDeptIds(deptList1));
        }
        list.add(systemDeptTb.getId());
      }
    }
    return list;
  }

  /**
   * 根据ID获取同级与上级数据
   *
   * @param dept     /
   * @param deptList /
   * @return /
   */
  private List<SystemDeptTb> querySuperiorDeptByPid(SystemDeptTb dept, List<SystemDeptTb> deptList) {
    if (dept.getPid() == null) {
      deptList.addAll(this.listRootDept());
      return deptList;
    }
    deptList.addAll(this.listDeptByPid(dept.getPid()));
    return querySuperiorDeptByPid(this.getDeptById(dept.getPid()), deptList);
  }

  private List<SystemDeptTb> listRootDept() {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.isNull(SystemDeptTb::getPid);
    return systemDeptMapper.selectList(wrapper);
  }

  /**
   * 构建树形数据
   *
   * @return /
   */
  public KitPageResult<SystemDeptTb> searchDeptTree(List<Long> ids, Boolean exclude) {
    Set<SystemDeptTb> deptSet1 = new LinkedHashSet<>();
    for (Long id : ids) {
      // 同级数据
      SystemDeptTb dept = this.getDeptById(id);
      // 上级数据
      List<SystemDeptTb> depts = this.querySuperiorDeptByPid(dept, new ArrayList<>());
      if (exclude) {
        for (SystemDeptTb data : depts) {
          if (data.getId().equals(dept.getPid())) {
            data.setSubCount(data.getSubCount() - 1);
          }
        }
        // 编辑部门时不显示自己以及自己下级的数据, 避免出现PID数据环形问题
        depts = depts.stream().filter(i -> !ids.contains(i.getId())).collect(Collectors.toList());
      }
      deptSet1.addAll(depts);
    }
    ArrayList<SystemDeptTb> deptList = new ArrayList<>(deptSet1);
    // 构建部门树
    Set<SystemDeptTb> trees = new LinkedHashSet<>();
    Set<SystemDeptTb> deptSet = new LinkedHashSet<>();
    List<String> deptNames = deptList.stream().map(SystemDeptTb::getName).collect(Collectors.toList());
    boolean isChild;
    for (SystemDeptTb dept : deptList) {
      isChild = false;
      if (dept.getPid() == null) {
        trees.add(dept);
      }
      for (SystemDeptTb it : deptList) {
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
      } else if (dept.getPid() != null && !deptNames.contains(this.getDeptById(dept.getPid()).getName())) {
        deptSet.add(dept);
      }
    }
    if (CollectionUtil.isEmpty(trees)) {
      trees = deptSet;
    }
    KitPageResult<SystemDeptTb> baseResult = new KitPageResult<>();
    baseResult.setContent(CollectionUtil.isEmpty(trees) ? new ArrayList<>(deptSet) : new ArrayList<>(trees));
    baseResult.setTotalElements(deptSet.size());
    return baseResult;
  }

  /**
   * 验证是否被角色或用户关联
   *
   * @param deptSet /
   */
  public void verifyBindRelationByIds(Set<SystemDeptTb> deptSet) {
    Set<Long> deptIds = deptSet.stream().map(SystemDeptTb::getId).collect(Collectors.toSet());
    if (systemUserService.countUserByDeptIds(deptIds) > 0) {
      throw new BadRequestException("所选部门存在用户关联，请解除后再试！");
    }
    if (systemRoleService.countRoleByDeptIds(deptIds) > 0) {
      throw new BadRequestException("所选部门存在角色关联，请解除后再试！");
    }
  }

  private List<SystemDeptTb> listEnabledDepts() {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getEnabled, 1);
    return systemDeptMapper.selectList(wrapper);
  }

  /**
   * 遍历所有部门和子部门
   *
   * @param ids /
   */
  public Set<SystemDeptTb> traverseDeptByIdWithPids(Set<Long> ids) {
    Set<SystemDeptTb> depts = new HashSet<>();
    for (Long id : ids) {
      // 根部门
      depts.add(this.getDeptById(id));
      // 子部门
      List<SystemDeptTb> deptList = listDeptByPid(id);
      if (CollectionUtil.isNotEmpty(deptList)) {
        depts = queryRelationDeptByArgs(deptList, depts);
      }
    }
    return depts;
  }

  public List<SystemProductLineVo> queryDeptSelectDataSource() {
    List<SystemDeptTb> depts = this.listEnabledDepts();
    return buildDeptSelectOptions(depts);
  }

  private List<SystemProductLineVo> buildDeptSelectOptions(List<SystemDeptTb> depts) {
    // 获取所有部门并按父子关系组织
    Map<Long, SystemDeptTb> deptMap =
        depts.stream().collect(Collectors.toMap(SystemDeptTb::getId, Function.identity()));
    List<SystemProductLineVo> options = new ArrayList<>();
    for (SystemDeptTb dept : depts) {
      // 构建部门ID路径
      List<Long> pathIds = new ArrayList<>();
      buildDeptIdPath(dept, deptMap, pathIds);
      // 反转路径，从顶级部门到当前部门
      Collections.reverse(pathIds);
      SystemProductLineVo dto = new SystemProductLineVo();
      dto.setValue(dept.getId());
      dto.setIdPath(pathIds.stream().map(String::valueOf).collect(Collectors.joining("-")));
      // 保留名称路径用于显示
      dto.setLabel(buildNamePath(dept, deptMap));
      options.add(dto);
    }
    // 按部门名称路径排序
    options.sort(Comparator.comparing(SystemProductLineVo::getLabel));
    return options;
  }

  private void buildDeptIdPath(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap, List<Long> pathIds) {
    pathIds.add(dept.getId());
    if (dept.getPid() != null && dept.getPid() != 0 && deptMap.containsKey(dept.getPid())) {
      buildDeptIdPath(deptMap.get(dept.getPid()), deptMap, pathIds);
    }
  }

  private String buildNamePath(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap) {
    List<String> pathNames = new ArrayList<>();
    buildDeptNamePath(dept, deptMap, pathNames);
    Collections.reverse(pathNames);
    return String.join(" / ", pathNames);
  }

  private void buildDeptNamePath(SystemDeptTb dept, Map<Long, SystemDeptTb> deptMap, List<String> pathNames) {
    pathNames.add(dept.getName());
    if (dept.getPid() != null && dept.getPid() != 0 && deptMap.containsKey(dept.getPid())) {
      buildDeptNamePath(deptMap.get(dept.getPid()), deptMap, pathNames);
    }
  }

  public List<SystemProductLineTreeVo> queryDeptSelectProDataSource() {
    List<SystemDeptTb> depts = this.listEnabledDepts();
    // 获取所有部门并按父子关系组织
    Map<Long, SystemDeptTb> deptMap =
        depts.stream().collect(Collectors.toMap(SystemDeptTb::getId, Function.identity()));
    List<SystemProductLineTreeVo> options = new ArrayList<>();
    // 构建树形结构
    for (SystemDeptTb dept : depts) {
      // 只处理顶级部门（pid为null或0的部门）
      if (dept.getPid() == null || dept.getPid() == 0) {
        SystemProductLineTreeVo vo = buildDeptTreePro(dept, deptMap);
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
        children.add(buildDeptTreePro(childDept, deptMap));
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

  public List<SystemDeptTb> listDeptByPid(long pid) {
    LambdaQueryWrapper<SystemDeptTb> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SystemDeptTb::getPid, pid);
    return systemDeptMapper.selectList(wrapper);
  }
}
