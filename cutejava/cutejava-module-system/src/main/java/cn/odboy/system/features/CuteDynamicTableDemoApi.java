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
package cn.odboy.system.features;

import cn.hutool.core.util.StrUtil;
import cn.odboy.base.KitPageArgs;
import cn.odboy.framework.mybatisplus.core.KitMpQUtil;
import cn.odboy.system.features.core.KitDynamicTableResponse;
import cn.odboy.system.features.model.SystemMenuDynamicTableModel;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.system.dal.model.request.SystemQueryMenuArgs;
import cn.odboy.system.dal.mysql.SystemMenuMapper;
import cn.odboy.system.features.util.KitDynamicTableUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "系统组件：CuteDynamicTable演示")
@RequestMapping("/api/features/CuteDynamicTable")
public class CuteDynamicTableDemoApi {

  @Autowired
  private SystemMenuMapper systemMenuMapper;

  @ApiOperation("写法1：查询动态数据")
  @PostMapping(value = "/searchMenu")
  @PreAuthorize("@el.check()")
  public ResponseEntity<KitDynamicTableResponse<SystemMenuTb, SystemMenuDynamicTableModel>> searchMenu(@Validated @RequestBody KitPageArgs<SystemQueryMenuArgs> pageArgs) {
    // 请求参数
    Integer page = pageArgs.getPage();
    Integer size = pageArgs.getSize();
    KitPageArgs.OrderBy orderBy = pageArgs.getOrderBy();
    SystemQueryMenuArgs args = pageArgs.getArgs();
    // 分页查询
    QueryWrapper<SystemMenuTb> wrapper = new QueryWrapper<>();
    if (orderBy != null) {
      orderBy.bindWrapper(wrapper);
    }
    IPage<SystemMenuTb> iPage = new Page<>(page, size);
    LambdaQueryWrapper<SystemMenuTb> lambda = wrapper.lambda();
    if (args != null) {
      lambda.like(StrUtil.isNotBlank(args.getBlurry()), SystemMenuTb::getTitle, args.getBlurry());
    }
    IPage<SystemMenuTb> pageResult = systemMenuMapper.selectPage(iPage, wrapper);
    // 响应结果
    return ResponseEntity.ok(new KitDynamicTableResponse<>(SystemMenuDynamicTableModel.class, pageResult, "id"));
  }

  @ApiOperation("写法2：查询动态数据")
  @PostMapping(value = "/searchMenu2")
  @PreAuthorize("@el.check()")
  public ResponseEntity<KitDynamicTableResponse<SystemMenuTb, SystemMenuDynamicTableModel>> searchMenu2(@Validated @RequestBody KitPageArgs<SystemQueryMenuArgs> pageArgs) {
    // 渲染排序（非必须）
    QueryWrapper<SystemMenuTb> wrapper = KitDynamicTableUtil.renderOrder(pageArgs);
    LambdaQueryWrapper<SystemMenuTb> lambda = wrapper.lambda();
    // 查询条件
    SystemQueryMenuArgs args = pageArgs.getArgs();
    if (args != null) {
      lambda.like(StrUtil.isNotBlank(args.getBlurry()), SystemMenuTb::getTitle, args.getBlurry());
    }
    // 响应结果
    return ResponseEntity.ok(new KitDynamicTableResponse<>(SystemMenuDynamicTableModel.class, pageArgs, "id", systemMenuMapper, lambda));
  }

  @ApiOperation("写法3：查询动态数据")
  @PostMapping(value = "/searchMenu3")
  @PreAuthorize("@el.check()")
  public ResponseEntity<KitDynamicTableResponse<SystemMenuTb, SystemMenuDynamicTableModel>> searchMenu3(@Validated @RequestBody KitPageArgs<SystemQueryMenuArgs> pageArgs) {
    // 渲染查询条件
    LambdaQueryWrapper<SystemMenuTb> wrapper = KitDynamicTableUtil.renderAll(pageArgs);
    // 响应结果
    return ResponseEntity.ok(new KitDynamicTableResponse<>(SystemMenuDynamicTableModel.class, pageArgs, "id", systemMenuMapper, wrapper));
  }
}
