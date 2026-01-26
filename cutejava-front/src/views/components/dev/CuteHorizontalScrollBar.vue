<!--
 * 横向滚动条容器
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-20
 -->
<template>
  <div class="overflow-panel" :style="{width: width}">
    <slot />
  </div>
</template>

<script>

export default {
  name: 'CuteHorizontalScrollBar',
  props: {
    width: {
      type: String,
      required: false,
      default: '1280px'
    }
  }
}
</script>

<style scoped>
.overflow-panel {
  overflow-x: scroll; /* 允许水平滚动 */
  white-space: nowrap; /* 防止内容换行，使所有内容在一行显示 */
  padding: 30px 20px 30px 20px;
}

/* 滚动条宽度 */
.overflow-panel::-webkit-scrollbar {
  height: 5px;
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

