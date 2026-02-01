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
      :data="dataSource"
      style="width: 100%;"
      :height="height"
      :max-height="height"
      highlight-current-row
    >
      <el-table-column type="index" width="70" />
      <el-table-column v-for="item in schema" :key="item[primaryKey]" :prop="item.name" :label="item.title">
        <template slot-scope="scope">
          <el-input
            v-if="item.type === 'input'"
            v-model="scope.row[item.name]"
            :placeholder="`请输入${item.title}`"
            clearable
            @input="(val) => onRowChange(val, scope.$index, item.name)"
          />
          <el-select
            v-if="item.type === 'select'"
            v-model="scope.row[item.name]"
            :placeholder="`请选择${item.title}`"
            clearable
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
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="onRowRemove(scope.$index)">删除</el-button>
          <el-button type="text" size="small" :disabled="scope.$index < 1" @click="onRowUpMove(scope.$index)">上移
          </el-button>
          <el-button
            type="text"
            size="small"
            :disabled="scope.$index + 1 >= dataSource.length"
            @click="onRowDownMove(scope.$index)"
          >下移
          </el-button>
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
    },
    /**
     * 表格高度
     */
    height: {
      type: Number,
      required: false,
      default: 240
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
    },
    /**
     * 当删除按钮被点击
     * @param index
     */
    onRowRemove(index) {
      const dataSource = this.dataSource
      dataSource.splice(index, 1)
      this.dataSource = [...dataSource]
      this.$emit('input', this.dataSource)
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
      dataSource[index] = dataSource[index - 1]
      dataSource[index - 1] = temp
      this.dataSource = [...dataSource]
      this.$emit('input', this.dataSource)
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
      dataSource[index] = dataSource[index + 1]
      dataSource[index + 1] = temp
      this.dataSource = [...dataSource]
      this.$emit('input', this.dataSource)
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
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/assets/styles/variables.scss";

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

