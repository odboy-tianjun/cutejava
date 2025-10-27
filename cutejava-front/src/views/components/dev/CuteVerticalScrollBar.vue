<!--
 * 纵向滚动条容器
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-20
 -->
<template>
  <div class="overflow-panel" :style="{height: height}">
    <slot />
  </div>
</template>

<script>

export default {
  name: 'CuteVerticalScrollBar',
  props: {
    height: {
      type: String,
      required: true,
      default: '100px'
    }
  }
}
</script>

<style scoped>
.overflow-panel {
  overflow-y: scroll;
}

/* 滚动条宽度 */
.overflow-panel::-webkit-scrollbar {
  width: 5px;
}

/* 滚动条轨道 */
.overflow-panel::-webkit-scrollbar-track {
  background: #f1f1f1;
}

/* 滚动条滑块 */
.overflow-panel::-webkit-scrollbar-thumb {
  background: #409EFF;
  border-radius: 6px;
}

/* 鼠标悬停在滑块上时 */
.overflow-panel::-webkit-scrollbar-thumb:hover {
  background: #F56C6C;
}
</style>

