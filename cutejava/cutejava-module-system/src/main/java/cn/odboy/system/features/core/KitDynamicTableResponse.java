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
package cn.odboy.system.features.core;

import cn.hutool.core.util.ReflectUtil;
import cn.odboy.base.KitObject;
import cn.odboy.base.KitPageArgs;
import cn.odboy.system.features.model.SystemMenuDynamicTableModel;
import cn.odboy.system.dal.dataobject.SystemMenuTb;
import cn.odboy.util.KitBeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 动态表格
 *
 * @param <E> 数据表对象
 * @param <T> 渲染对象
 * @author odboy
 * @date 2026-02-05
 */
@Getter
public class KitDynamicTableResponse<E, T> extends KitObject {

  /**
   * 主键
   */
  @Setter
  private String primaryKey = "id";
  /**
   * 表格列定义
   */
  private List<Column> columns = new ArrayList<>();
  /**
   * 数据总数
   */
  private Long totalElements;
  /**
   * 数据内容
   */
  private List<T> content;

  /**
   * @param clazz      渲染类型
   * @param pageResult 渲染类型分页参数
   */
  public KitDynamicTableResponse(Class<T> clazz, IPage<E> pageResult) {
    this.renderColumns(clazz);
    this.setContent(clazz, pageResult);
  }

  /**
   * @param clazz      渲染类型
   * @param pageResult 渲染类型分页参数
   * @param primaryKey 主键
   */
  public KitDynamicTableResponse(Class<T> clazz, IPage<E> pageResult, String primaryKey) {
    this.primaryKey = primaryKey;
    this.renderColumns(clazz);
    this.setContent(clazz, pageResult);
  }

  /**
   * @param clazz      渲染类型
   * @param pageArgs   分页参数
   * @param baseMapper 数据表Mapper
   * @param wrapper    查询条件
   */
  public KitDynamicTableResponse(Class<T> clazz, KitPageArgs<?> pageArgs, BaseMapper<E> baseMapper, LambdaQueryWrapper<E> wrapper) {
    this.renderColumns(clazz);
    Page<?> pageResult = baseMapper.selectPage(new Page<>(pageArgs.getPage(), pageArgs.getSize()), wrapper);
    this.setContent(clazz, pageResult);
  }

  /**
   * @param clazz      渲染类型
   * @param pageArgs   分页参数
   * @param primaryKey 主键
   * @param baseMapper 数据表Mapper
   * @param wrapper    查询条件
   */
  public KitDynamicTableResponse(Class<T> clazz, KitPageArgs<?> pageArgs, String primaryKey, BaseMapper<E> baseMapper, LambdaQueryWrapper<E> wrapper) {
    this.primaryKey = primaryKey;
    this.renderColumns(clazz);
    Page<?> pageResult = baseMapper.selectPage(new Page<>(pageArgs.getPage(), pageArgs.getSize()), wrapper);
    this.setContent(clazz, pageResult);
  }

  /**
   * 渲染表格列
   *
   * @param clazz 渲染类型
   */
  private void renderColumns(Class<T> clazz) {
    List<Column> columns = new ArrayList<>();
    if (clazz == null) {
      this.columns = columns;
      return;
    }
    Field[] fields = ReflectUtil.getFields(clazz);
    for (Field field : fields) {
      if ("serialVersionUID".equals(field.getName())) {
        continue;
      }
      Column column = new Column();
      column.setName(field.getName());
      KitDynamicTableColumn annotation = field.getAnnotation(KitDynamicTableColumn.class);
      if (annotation == null) {
        column.setTitle(field.getName());
        column.setWidth(null);
      } else {
        column.setTitle(annotation.value());
        column.setWidth(annotation.width() == 0 ? null : annotation.width());
        column.setSortable(annotation.sortable());
      }
      columns.add(column);
    }
    this.columns = columns;
  }

  /**
   * @param clazz      渲染类型
   * @param pageResult 渲染结果
   */
  public void setContent(Class<T> clazz, IPage<?> pageResult) {
    if (pageResult == null) {
      return;
    }
    this.totalElements = pageResult.getTotal();
    this.content = KitBeanUtil.copyToList(pageResult.getRecords(), clazz);
  }

  @Getter
  @Setter
  public static class Column {

    /**
     * 索引名称
     */
    private String name;
    /**
     * 索引标题
     */
    private String title;
    /**
     * 表格宽度
     */
    private Integer width;
    /**
     * 是否排序列
     */
    private Boolean sortable = false;
  }

  public static void main(String[] args) {
    KitDynamicTableResponse<SystemMenuTb, SystemMenuDynamicTableModel> response = new KitDynamicTableResponse<>(
        SystemMenuDynamicTableModel.class, new Page<>(), "id");
    System.err.println(response);
  }
}
