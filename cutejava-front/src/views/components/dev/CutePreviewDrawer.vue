<!--
 * 预览抽屉：封装了常用的方法和属性
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 -->
<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    :size="width"
    :before-close="beforeClose"
    :append-to-body="true"
    :close-on-press-escape="false"
    :wrapper-closable="wrapperClosable"
    :destroy-on-close="true"
    :show-close="true"
    :modal="false"
  >
    <!-- 这里是插槽，用于渲染传入的组件 -->
    <slot />
  </el-drawer>
</template>

<script>

export default {
  name: 'CutePreviewDrawer',
  props: {
    title: {
      type: String,
      required: true,
      default: '默认标题'
    },
    width: {
      type: String,
      required: false,
      default: '40%'
    },
    wrapperClosable: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      visible: false
    }
  },
  methods: {
    show() {
      this.visible = true
    },
    hidden() {
      this.visible = false
    },
    beforeClose() {
      this.hidden()
    }
  }
}
</script>
<style scoped>
.container-form {
  overflow-y: scroll; /* 启用滚动 */
  scrollbar-width: none; /* Firefox */
  height: 85%;
  padding-right: 20px;
  -ms-overflow-style: none; /* Internet Explorer 10+ */
}

.container-form::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera*/
}
</style>

