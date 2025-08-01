<template>
  <el-cascader
    v-model="value"
    :disabled="disabled"
    :options="deptOptions"
    style="width: 100%"
    @change="handleChange"
  />
</template>

<script>

import { queryDeptSelectProDataSource } from '@/api/system/component'
import CsMessage from '@/utils/elementui/CsMessage'

export default {
  name: 'CuteProductLineSelectPro',
  props: {
    value: {
      type: Array,
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
      selectedValue: this.value
    }
  },
  watch: {
    value(newVal) {
      this.selectedValue = newVal
    }
  },
  created() {
    this.fetchDeptData()
  },
  methods: {
    // 递归清理空的children属性
    cleanEmptyChildren(data) {
      return data.map(item => {
        const newItem = { ...item }
        if (newItem.children && newItem.children.length > 0) {
          // 递归处理子节点
          newItem.children = this.cleanEmptyChildren(newItem.children)
        } else {
          // 删除空的children属性
          delete newItem.children
        }
        return newItem
      })
    },
    fetchDeptData() {
      queryDeptSelectProDataSource().then(data => {
        this.deptOptions = this.cleanEmptyChildren(data)
        console.error('deptOptions', this.deptOptions)
      }).catch(error => {
        console.error('获取部门数据失败:', error)
        CsMessage.Error('获取部门数据失败')
      })
    },
    handleChange(value) {
      this.selectedValue = value
      // 绑定change事件
      this.$emit('change', value)
      // 绑定form value
      this.$emit('input', value)
    }
  }
}
</script>

