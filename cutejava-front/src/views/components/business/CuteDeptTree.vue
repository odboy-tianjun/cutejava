<!--
 * 部门树组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-07-29
 -->
<template>
  <el-col :span="6">
    <div class="head-container">
      <el-input
        v-model="deptName"
        clearable
        size="small"
        placeholder="输入部门名称搜索"
        prefix-icon="el-icon-search"
        class="filter-item"
        @input="getDeptData"
      />
    </div>
    <el-tree
      ref="deptTree"
      :data="deptData"
      :load="getDeptData"
      :props="defaultProps"
      :expand-on-click-node="false"
      lazy
      @node-click="handleNodeClick"
    />
  </el-col>
</template>

<script>

import CuteDeptTreeApi from '@/api/application/cute-dept-tree'

export default {
  name: 'CuteDeptTree',
  props: {
    value: {
      type: String,
      required: false,
      default: null
    }
  },
  data() {
    return {
      deptName: '',
      deptData: [],
      defaultProps: { children: 'children', label: 'name', isLeaf: 'leaf' }
    }
  },
  methods: {
    getDeptData(node, resolve) {
      this.$emit('search', node)
      const params = {}
      if (typeof node !== 'object') {
        if (node) {
          params['name'] = node
        }
      } else if (node.level !== 0) {
        params['pid'] = node.data.id
      }
      setTimeout(() => {
        CuteDeptTreeApi.listMetadata(params).then(res => {
          if (resolve) {
            resolve(res)
          } else {
            this.deptData = res
          }
        })
      }, 100)
    },
    handleNodeClick(value) {
      // 绑定node-click事件
      this.$emit('node-click', value)
      // 绑定form value
      this.$emit('change', value ? value.id : null)
      this.$emit('input', value ? value.id : null)
    },
    resetField() {
      this.$refs.deptTree.setCurrentNode(null)
      this.$emit('change', null)
      this.$emit('input', null)
    }
  }
}
</script>

