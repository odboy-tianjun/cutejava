<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    :size="width"
    :before-close="beforeClose"
    :append-to-body="true"
    :close-on-press-escape="false"
    :destroy-on-close="true"
    :show-close="false"
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
  height: 88%;
  padding-right: 20px;
  -ms-overflow-style: none; /* Internet Explorer 10+ */
}

.container-form::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera*/
}
</style>

