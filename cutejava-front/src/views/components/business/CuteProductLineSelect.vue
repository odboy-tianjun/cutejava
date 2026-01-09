<!--
 * 产品线选择组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 -->
<template>
  <el-select
    v-model="productLine"
    filterable
    clearable
    placeholder="请选择产品线"
    :disabled="disabled"
    style="width: 100%"
    @change="handleChange"
  >
    <el-option
      v-for="item in deptOptions"
      :key="item.value"
      :label="item.label"
      :value="item.value"
    />
  </el-select>
</template>

<script>

import { listDeptSelectDataSource } from '@/api/system/component'
import CsMessage from '@/utils/elementui/CsMessage'

export default {
  name: 'CuteProductLineSelect',
  props: {
    value: {
      type: [Number, String],
      default: null
    },
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      deptOptions: [],
      productLine: this.value
    }
  },
  watch: {
    value(newVal) {
      this.productLine = newVal
    }
  },
  created() {
    this.fetchDeptData()
  },
  methods: {
    fetchDeptData() {
      listDeptSelectDataSource().then(data => {
        this.deptOptions = data
      }).catch(error => {
        console.error('获取部门数据失败:', error)
        CsMessage.Error('获取部门数据失败')
      })
    },
    handleChange(value) {
      this.productLine = value
      for (const item of this.deptOptions) {
        if (item.value === value) {
          this.$emit('detail', item)
          break
        }
      }
      // 绑定change事件
      this.$emit('change', value)
      // 绑定form value
      this.$emit('input', value)
    }
  }
}
</script>

