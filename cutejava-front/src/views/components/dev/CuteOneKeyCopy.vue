<!-- 一键复制组件 -->
<!-- 使用方式： <one-key-copy style="margin-left: 10pt" :content.sync="data" /> -->
<template>
  <el-button
    v-clipboard:copy="content"
    v-clipboard:success="onCopy"
    v-clipboard:error="onError"
    size="mini"
    icon="el-icon-document-copy"
  />
</template>

<script>
export default {
  name: 'CuteOneKeyCopy',
  props: {
    content: {
      type: String,
      default: ''
    }
  },
  methods: {
    onCopy(row) {
      const h = this.$createElement
      this.$notify({
        title: row.text + ' 复制成功',
        message: h('i', { style: 'color: teal' }, '已复制到剪贴板,请直接去 Ctrl+V 粘贴吧!')
      })
    },
    // 复制失败
    onError(row) {
      const h = this.$createElement
      this.$notify({
        title: row.text + ' 复制失败',
        message: h('i', { style: 'color: teal' }, '联系技术吧！')
      })
    }
  }
}
</script>

<style scoped>
</style>
