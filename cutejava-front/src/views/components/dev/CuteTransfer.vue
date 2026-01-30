<!--
 * 数据穿梭组件（不适合大数据量）
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2026-01-30
 -->
<template>
  <el-row>
    <!--    <el-button @click="onTestValue">测试值</el-button>-->
    <el-col :span="11">
      <el-table
        v-loading="loading"
        stripe
        border
        empty-text="暂无数据"
        :row-key="primaryKey"
        fit
        :data="leftDataSource"
        style="width: 100%;"
        :height="height"
        :max-height="height"
        highlight-selection-row
        @selection-change="onLeftTableSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <slot />
      </el-table>
    </el-col>
    <el-col :span="2" style="text-align: center">
      <el-row style="margin-top: 130px">
        <el-button
          type="primary"
          icon="el-icon-d-arrow-right"
          size="mini"
          :disabled="leftSelection.length === 0"
          @click="onTransferRight"
        />
      </el-row>
      <el-row style="margin-top: 20px">
        <el-button
          type="primary"
          icon="el-icon-d-arrow-left"
          size="mini"
          :disabled="rightSelection.length === 0"
          @click="onTransferLeft"
        />
      </el-row>
    </el-col>
    <el-col :span="11">
      <el-table
        ref="selectedTable"
        stripe
        border
        empty-text="暂无数据"
        :row-key="primaryKey"
        fit
        :data="rightDataSource"
        style="width: 100%;"
        :height="height"
        :max-height="height"
        highlight-selection-row
        @selection-change="onRightTableSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <slot />
      </el-table>
    </el-col>
  </el-row>
</template>

<script>

export default {
  name: 'CuteTransfer',
  props: {
    /**
     * 已选中的值
     */
    value: {
      type: Array,
      default: function() {
        return []
      }
    },
    /**
     * 左侧表格数据
     */
    dataSource: {
      type: Array,
      require: true,
      default: function() {
        return []
      }
    },
    /**
     * 取哪个值作为value
     */
    primaryKey: {
      type: String,
      require: true,
      default: 'id'
    },
    /**
     * 是否禁用
     */
    disabled: {
      type: Boolean,
      default: false
    },
    /**
     * 是否加载中
     */
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      height: 350,
      leftDataSource: [],
      leftSelection: [],
      rightDataSource: [],
      rightSelection: [],
      rightValues: []
    }
  },
  mounted() {
    this.rightValues = this.value
    this.leftDataSource = this.dataSource

    const key = this.primaryKey
    const leftDataSource = this.leftDataSource || []
    const rightValues = this.rightValues

    if (rightValues && rightValues.length > 0) {
      const rightDataSource = []
      for (const element of leftDataSource) {
        if (rightValues.indexOf(element[key]) !== -1) {
          rightDataSource.push(element)
        }
      }
      this.rightDataSource = rightDataSource
      this.leftDataSource = leftDataSource.filter(item => !rightValues.includes(item[key]))
    } else {
      this.leftDataSource = leftDataSource
    }
  },
  methods: {
    onLeftTableSelectionChange(selection) {
      this.leftSelection = selection
    },
    onRightTableSelectionChange(selection) {
      this.rightSelection = selection
    },
    /**
     * 左侧转右侧
     */
    onTransferRight() {
      const key = this.primaryKey
      const leftDataSource = this.leftDataSource
      const leftSelection = this.leftSelection
      const rightDataSource = this.rightDataSource
      const rightValues = this.rightValues

      if (leftSelection && leftSelection.length > 0) {
        for (const element of leftSelection) {
          rightDataSource.push(element)
        }
        this.rightDataSource = rightDataSource
        for (const element of leftSelection) {
          rightValues.push(element[key])
        }
        this.rightValues = rightValues
        // 清空左侧已选中
        this.leftSelection = []
        // 过滤左侧选中的数据源
        this.leftDataSource = leftDataSource.filter(item => !rightValues.includes(item[key]))
        // 兼容表单
        this.$emit('change', this.rightValues)
        this.$emit('input', this.rightValues)
      }
    },
    /**
     * 右侧转左侧
     */
    onTransferLeft() {
      const key = this.primaryKey
      const leftDataSource = this.leftDataSource
      const rightDataSource = this.rightDataSource
      const rightSelection = this.rightSelection
      const rightValues = this.rightValues

      if (rightSelection && rightSelection.length > 0) {
        for (const element of rightSelection) {
          leftDataSource.push(element)
        }
        this.leftDataSource = leftDataSource
        // 从右侧数据源中移除选中的项
        const selectedKeys = rightSelection.map(item => item[key])
        this.rightDataSource = rightDataSource.filter(item => !selectedKeys.includes(item[key]))
        this.rightValues = rightValues.filter(value => !selectedKeys.includes(value))
        // 清空右侧已选中
        this.rightSelection = []
        // 兼容表单
        this.$emit('change', this.rightValues)
        this.$emit('input', this.rightValues)
      }
    }
    // onTestValue() {
    //   console.error('==============leftDataSource', this.leftDataSource)
    //   console.error('==============leftSelection', this.leftSelection)
    //   console.error('==============rightDataSource', this.rightDataSource)
    //   console.error('==============rightSelection', this.rightSelection)
    //   console.error('==============rightValues', this.rightValues)
    // }
  }
}
</script>

