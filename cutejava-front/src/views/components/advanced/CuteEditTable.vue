<!--
 * 可编辑表格组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2026-02-01
 -->
<template>
  <div>
    <el-table
      ref="table"
      stripe
      empty-text="暂无数据"
      :row-key="primaryKey"
      fit
      border
      :data="dataSource"
      style="width: 100%;"
      highlight-current-row
    >
      <el-table-column v-for="item in schema" :key="item[primaryKey]" :prop="item.name" :label="item.title">
        <template v-slot="scope">
          <el-input
            v-if="item.type === 'input'"
            v-model="scope.row[item.name]"
            style="width: 100%"
            :placeholder="`请输入${item.title}`"
            clearable
            :disabled="scope.row.disabled === true"
            @input="(val) => onRowChange(val, scope.$index, item.name)"
          />
          <el-select
            v-if="item.type === 'select'"
            v-model="scope.row[item.name]"
            style="width: 100%"
            :placeholder="`请选择${item.title}`"
            clearable
            :disabled="scope.row.disabled === true"
            @change="(val) => onRowChange(val, scope.$index, item.name)"
          >
            <el-option
              v-for="option in (item.dataSource || [])"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column>
        <template v-slot="scope">
          <el-button type="text" size="small" :disabled="scope.row.disabled === true" @click="onRowDelete(scope.$index)">删除</el-button>
          <el-button type="text" size="small" :disabled="scope.$index < 1 || scope.row.disabled === true" @click="onRowUpMove(scope.$index)">上移</el-button>
          <el-button type="text" size="small" :disabled="scope.$index + 1 >= dataSource.length || scope.row.disabled === true" @click="onRowDownMove(scope.$index)">下移</el-button>
          <el-button v-if="scope.$index === dataSource.length - 1" type="text" size="small" :disabled="scope.row.disabled === true" @click="onRowForceUp(scope.$index)">置顶</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button size="small" plain icon="el-icon-plus" style="width: 100%" @click="onRowAddClick">新增一行数据</el-button>
  </div>
</template>

<script>

export default {
  name: 'CuteEditTable',
  props: {
    /**
     * 绑定的值
     */
    value: {
      type: Array,
      required: false,
      default: function() {
        return []
      }
    },
    /**
     * 主键：列表排序用
     */
    primaryKey: {
      type: String,
      required: true
    },
    /**
     * 对表格字段的定义
     */
    schema: {
      type: Array,
      require: true,
      default: function() {
        return []
      }
    }
  },
  data() {
    return {
      dataSource: []
    }
  },
  mounted() {
    if (this.value && this.value.length > 0) {
      this.dataSource = this.value
    }
  },
  methods: {
    /**
     * 当行内数据变更
     * @param val 当前值
     * @param index
     * @param name
     */
    onRowChange(val, index, name) {
      const dataSource = this.dataSource
      dataSource[index][name] = val
      this.dataSource = [...dataSource]
      this.$emit('input', this.dataSource)
      this.$emit('row-change', index, name, val)
    },
    /**
     * 当删除按钮被点击
     * @param index
     */
    onRowDelete(index) {
      const dataSource = this.dataSource
      dataSource.splice(index, 1)
      this.dataSource = [...dataSource]
      this.$emit('input', this.dataSource)
      this.$emit('row-delete', index)
    },
    /**
     * 当上移按钮被点击
     * @param index
     */
    onRowUpMove(index) {
      if (index <= 0) {
        return
      }
      const dataSource = this.dataSource
      const temp = dataSource[index]
      const newIndex = index - 1
      dataSource[index] = dataSource[newIndex]
      dataSource[newIndex] = temp
      this.dataSource = [...dataSource]
      this.$emit('input', this.dataSource)
      this.$emit('row-up', index, newIndex, temp)
    },
    /**
     * 当下移按钮被点击
     * @param index
     */
    onRowDownMove(index) {
      if (index >= this.dataSource.length - 1) {
        return
      }
      const dataSource = this.dataSource
      const temp = dataSource[index]
      const newIndex = index + 1
      dataSource[index] = dataSource[newIndex]
      dataSource[newIndex] = temp
      this.dataSource = [...dataSource]
      this.$emit('input', this.dataSource)
      this.$emit('row-down', index, newIndex, temp)
    },
    /**
     * 当置顶按钮被点击
     * @param index
     */
    onRowForceUp(index) {
      const dataSource = this.dataSource
      // 校验是否为最后一行
      if (index !== dataSource.length - 1) {
        return
      }
      // 获取需要置顶的数据项
      const targetRow = dataSource[index]
      // 删除当前索引数据
      dataSource.splice(index, 1)
      // 插入到数据开头
      dataSource.unshift(targetRow)
      this.dataSource = [...dataSource]
      this.$emit('input', this.dataSource)
      this.$emit('row-force-up', targetRow)
    },
    /**
     * 当添加数据按钮被点击
     */
    onRowAddClick() {
      const dataSource = this.dataSource
      const schema = this.schema
      const newObj = {}
      for (const element of schema) {
        newObj[element.name] = null
      }
      dataSource.push(newObj)
      this.dataSource = [...dataSource]
    },
    /**
     * el-form重置联动
     */
    resetField() {
      this.dataSource = []
      this.$emit('input', this.dataSource)
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/variables";

.selection-box {
  text-align: left;
  font-size: 12px;
  padding-left: 10px;
  min-height: 26.7px;
  max-height: 26.7px;
  padding-top: 7px;
}

.selection-count {
  color: $menuActiveText;
}
</style>

