<!--
 * 可编辑表格组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2026-02-01
 -->
<template>
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
        <el-input v-if="item.type === 'input'" v-model="scope.row[item.name]" @input="(val) => onRowChange(val, scope.$index, item.name)" />
        <el-select v-if="item.type === 'select'" v-model="scope.row[item.name]" @change="(val) => onRowChange(val, scope.$index, item.name)">
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
        <el-button type="text" size="small" :disabled="scope.$index < 1" @click="onRowUpMove(scope.$index)">上移</el-button>
        <el-button type="text" size="small" :disabled="scope.$index + 1 >= dataSource.length" @click="onRowDownMove(scope.$index)">下移</el-button>
      </template>
    </el-table-column>
  </el-table>
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
      require: true
    },
    /**
     * 自定义高度
     */
    customHeight: {
      type: Number,
      required: false,
      default: null
    }
  },
  data() {
    return {
      dataSource: []
    }
  },
  computed: {
    height() {
      if (this.customHeight) {
        return this.customHeight
      }
      return document.documentElement.clientHeight - 300
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
      console.log('=====onRowDownMove1', index)
      if (index >= this.dataSource.length - 1) {
        return
      }
      const dataSource = this.dataSource
      const temp = dataSource[index]
      dataSource[index] = dataSource[index + 1]
      dataSource[index + 1] = temp
      this.dataSource = [...dataSource]
      this.$emit('input', this.dataSource)
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

