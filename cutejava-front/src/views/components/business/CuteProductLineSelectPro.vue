<!--
 * 产品线级联选择组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 -->
<template>
  <el-cascader
    v-model="innerValue"
    :disabled="disabled"
    :options="deptOptions"
    style="width: 100%"
    placeholder="请选择产品线"
    @change="handleChange"
  />
</template>

<script>

import { listMetadata } from '@/api/application/cute-product-line-select-pro'
import KitMessage from '@/utils/elementui/KitMessage'

export default {
  name: 'CuteProductLineSelectPro',
  props: {
    value: {
      type: Array,
      required: false,
      default: null
    },
    disabled: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      deptOptions: [],
      innerValue: this.value
    }
  },
  watch: {
    value(newVal) {
      this.innerValue = newVal
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
      listMetadata().then(data => {
        this.deptOptions = this.cleanEmptyChildren(data)
        // console.error('deptOptions', this.deptOptions)
      }).catch(error => {
        console.error('获取部门数据失败:', error)
        KitMessage.Error('获取部门数据失败')
      })
    },
    handleChange(value) {
      this.innerValue = value
      // console.error('value', value)
      // 绑定change事件
      this.$emit('change', value)
      // 绑定form value
      this.$emit('input', value)
    },
    resetField() {
      this.innerValue = null
      this.$emit('change', null)
      this.$emit('input', null)
    }
  }
}
</script>

