<!--
 * 查询组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-20
 -->
<template>
  <el-form
    ref="searchForm"
    :inline="true"
    :model="model"
    label-position="right"
    label-width="90px"
    size="small"
  >
    <!-- 通过  <template v-slot:suffix> 插槽前置查询条件 -->
    <slot name="suffix" />
    <el-form-item>
      <el-button type="primary" @click="onFormSubmit('searchForm')">查询</el-button>
      <el-button @click="onFormReset('searchForm')">重置</el-button>
    </el-form-item>
    <!-- 通过  <template v-slot:operation> 插槽后置操作按钮 -->
    <slot name="operation" />
  </el-form>
</template>

<script>

export default {
  name: 'CuteSearchForm',
  components: {},
  props: {
    model: {
      type: Object,
      required: true
    }
  },
  methods: {
    onFormSubmit(formName) {
      const that = this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          that.$emit('search', that.model)
        } else {
          return false
        }
      })
    },
    onFormReset(formName) {
      try {
        this.$refs[formName].resetFields()
        this.onFormSubmit(formName)
      } catch (e) {
        console.error(e)
      }
    }
  }
}
</script>

