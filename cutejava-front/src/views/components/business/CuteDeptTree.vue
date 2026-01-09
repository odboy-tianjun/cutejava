<!--
 * 部门树组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-07-29
 -->
<template>
  <el-col :xs="9" :sm="6" :md="5" :lg="4" :xl="4">
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

import { searchDept } from '@/api/system/dept'

export default {
  name: 'CuteDeptTree',
  props: {
    value: {
      type: [Number, String],
      required: false,
      default: null
    }
  },
  data() {
    return {
      deptName: '',
      deptData: [],
      defaultProps: { children: 'children', label: 'name', isLeaf: 'leaf' },
      currentDept: null
    }
  },
  methods: {
    getDeptData(node, resolve) {
      const params = {}
      if (typeof node !== 'object') {
        if (node) {
          params['name'] = node
          this.$emit('search', node)
        }
      } else if (node.level !== 0) {
        params['pid'] = node.data.id
      }
      setTimeout(() => {
        searchDept({
          page: 1,
          size: 9999999,
          args: params
        }).then(res => {
          if (resolve) {
            resolve(res.content)
          } else {
            this.deptData = res.content
          }
        })
      }, 100)
    },
    handleNodeClick(value) {
      // 绑定node-click事件
      this.$emit('node-click', value)
      // 绑定form value
      this.$emit('input', value.id)
    }
  }
}
</script>

