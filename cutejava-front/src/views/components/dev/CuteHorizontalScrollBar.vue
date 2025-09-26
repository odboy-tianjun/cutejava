<!--
 * 横向滚动条容器
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-20
 -->
<template>
  <div class="overflow-panel">
    <slot />
  </div>
</template>

<script>

export default {
  name: 'CuteHorizontalScrollBar'
}
</script>

<style scoped>
.overflow-panel {
  width: 1500px; /* 固定宽度 */
  overflow-x: auto; /* 允许水平滚动 */
  white-space: nowrap; /* 防止内容换行，使所有内容在一行显示 */
  padding: 30px 20px 30px 20px;
}

.overflow-panel::-webkit-scrollbar {
  height: 5px; /* 设置水平滚动条高度 */
}

.overflow-panel::-webkit-scrollbar-track {
  background: #f1f1f1; /* 滚动条底色 */
}

.overflow-panel::-webkit-scrollbar-thumb {
  background: #DCDFE6; /* 滚动条颜色 */
}
</style>

