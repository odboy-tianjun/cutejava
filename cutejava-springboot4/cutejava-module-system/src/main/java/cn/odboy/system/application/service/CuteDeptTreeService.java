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

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.odboy.system.application.model.CuteDeptTreeVo;
import cn.odboy.system.constant.SystemDataScopeEnum;
import cn.odboy.system.dal.dataobject.SystemDeptTb;
import cn.odboy.system.dal.model.request.SystemQueryDeptArgs;
import cn.odboy.system.framework.permission.core.KitSecurityHelper;
import cn.odboy.system.service.SystemDeptService;
import cn.odboy.util.KitClassUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuteDeptTreeService {

  @Autowired
  private SystemDeptService systemDeptService;

  public List<CuteDeptTreeVo> listMetadata(SystemQueryDeptArgs args) throws Exception {
    String dataScopeType = KitSecurityHelper.getDataScopeType();
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
    // 数据权限
    args.setIds(KitSecurityHelper.getCurrentUserDataScope());
    List<SystemDeptTb> list = systemDeptService.queryDeptByArgs(args);
    // 如果为空, 就代表为自定义权限或者本级权限, 就需要去重, 不理解可以注释掉，看查询结果
    if (StrUtil.isBlank(dataScopeType)) {
      return systemDeptService.filterRootDeptList(list).stream().map(this::convertToVo).collect(Collectors.toList());
    }
    return list.stream().map(this::convertToVo).collect(Collectors.toList());
  }

  private CuteDeptTreeVo convertToVo(SystemDeptTb record) {
    CuteDeptTreeVo treeVo = new CuteDeptTreeVo();
    treeVo.setId(Long.toString(record.getId()));
    treeVo.setName(record.getName());
    treeVo.setPid(record.getPid() == null ? null : Long.toString(record.getPid()));
    treeVo.setSubCount(record.getSubCount());
    return treeVo;
  }
}
