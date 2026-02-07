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
package cn.odboy.system.features.model;

import cn.odboy.base.KitObject;
import cn.odboy.feature.KitDynamicTableColumn;
import lombok.Getter;
import lombok.Setter;

/**
 * 例子：渲染对象
 */
@Getter
@Setter
public class SystemMenuDynamicTableModel extends KitObject {

  @KitDynamicTableColumn(value = "ID")
  private Long id;
  @KitDynamicTableColumn(value = "菜单标题")
  private String title;
  @KitDynamicTableColumn(value = "菜单组件名称", width = 200)
  private String componentName;
  @KitDynamicTableColumn(value = "排序")
  private Integer menuSort = 999;
  @KitDynamicTableColumn(value = "组件路径", width = 200)
  private String component;
  @KitDynamicTableColumn(value = "路由地址", width = 120)
  private String path;
  @KitDynamicTableColumn(value = "菜单类型")
  private Integer type;
  @KitDynamicTableColumn(value = "权限标识")
  private String permission;
  @KitDynamicTableColumn(value = "菜单图标")
  private String icon;
  @KitDynamicTableColumn(value = "缓存")
  private Boolean cache;
  @KitDynamicTableColumn(value = "是否隐藏")
  private Boolean hidden;
  @KitDynamicTableColumn(value = "上级菜单")
  private Long pid;
  @KitDynamicTableColumn(value = "子节点数目", sortable = true, width = 200)
  private Integer subCount = 0;
  @KitDynamicTableColumn(value = "外链菜单")
  private Boolean iFrame;
}
