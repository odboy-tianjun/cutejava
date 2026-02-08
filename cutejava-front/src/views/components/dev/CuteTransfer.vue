<!--
 * 数据穿梭组件（不适合大数据量）
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2026-01-30
 -->
<template>
  <el-row>
    <el-col :span="11">
      <el-input
        v-model="searchValue"
        size="small"
        placeholder="输入关键字搜索"
        style="width: 100%;"
        @input="onSearchValueChange"
      />
      <el-table
        ref="leftTableRef"
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
        <el-table-column type="selection" width="55" :selectable="onTableSelectionRender" />
        <slot />
      </el-table>
      <el-col :span="24" class="selection-box">
        共计 <font class="selection-count">{{ leftDataSource && leftDataSource.length }}</font> 条，已选 <font
          class="selection-count"
        >{{ leftSelection && leftSelection.length }}</font> 条
      </el-col>
    </el-col>
    <el-col :span="2" style="text-align: center">
      <el-row style="margin-top: 130px">
        <el-button
          type="primary"
          icon="el-icon-d-arrow-right"
          size="small"
          :disabled="disabled || leftSelection.length === 0"
          @click="onTransferRight"
        />
      </el-row>
      <el-row style="margin-top: 20px">
        <el-button
          type="primary"
          icon="el-icon-d-arrow-left"
          size="small"
          :disabled="disabled || rightSelection.length === 0"
          @click="onTransferLeft"
        />
      </el-row>
    </el-col>
    <el-col :span="11">
      <el-table
        ref="rightTableRef"
        stripe
        border
        empty-text="暂无数据"
        :row-key="primaryKey"
        fit
        :data="rightDataSource"
        style="width: 100%;margin-top: 32px"
        :height="height"
        :max-height="height"
        highlight-selection-row
        @selection-change="onRightTableSelectionChange"
      >
        <el-table-column type="selection" width="55" :selectable="onTableSelectionRender" />
        <slot />
      </el-table>
      <el-col :span="24" class="selection-box">
        共计 <font class="selection-count">{{ rightDataSource && rightDataSource.length }}</font> 条，已选 <font
          class="selection-count"
        >{{ rightSelection && rightSelection.length }}</font> 条
      </el-col>
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
      required: false,
      default: function() {
        return []
      }
    },
    /**
     * 左侧表格数据
     */
    dataSource: {
      type: Array,
      required: true,
      default: function() {
        return []
      }
    },
    /**
     * 取哪个值作为value
     */
    primaryKey: {
      type: String,
      required: true,
      default: 'id'
    },
    /**
     * 是否加载中
     */
    loading: {
      type: Boolean,
      required: false,
      default: false
    },
    /**
     * 是否禁用
     */
    disabled: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      height: 350,
      searchValue: '',
      leftDataSource: [],
      leftSelection: [],
      rightDataSource: [],
      rightSelection: [],
      rightValues: [],
      searchCacheDS: []
    }
  },
  watch: {
    value(newVal) {
      this.initData(newVal)
    }
  },
  created() {
    this.initData(this.value)
  },
  methods: {
    initData(newVal) {
      this.rightValues = newVal
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
    onSearchValueChange(value) {
      if (this.searchCacheDS.length === 0) {
        this.searchCacheDS = this.leftDataSource
      }
      if (value) {
        this.leftDataSource = this.searchCacheDS.filter(data => !value || JSON.stringify(data).includes(value))
      } else {
        this.leftDataSource = this.searchCacheDS
        this.searchCacheDS = []
      }
    },
    onTableSelectionRender(row, index) {
      return !(row.disabled && row.disabled === true)
    },
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
    },
    /**
     * el-form重置联动
     */
    resetField() {
      const searchCacheDS = this.searchCacheDS
      if (searchCacheDS && searchCacheDS.length > 0) {
        this.leftDataSource = [...searchCacheDS, ...this.rightDataSource]
        this.searchCacheDS = []
      } else {
        this.leftDataSource = [...this.leftDataSource, ...this.rightDataSource]
      }
      this.searchValue = ''
      this.leftSelection = []
      this.rightSelection = []
      this.rightDataSource = []
      this.rightValues = []
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
  border-left: 1px solid #cccccc;
  border-bottom: 1px solid #cccccc;
  border-right: 1px solid #cccccc;
}

.selection-count {
  color: $menuActiveText;
}

// 搜索框边框剔除弧度
::v-deep .el-input .el-input__inner {
  border-radius: 1px;
}
</style>
