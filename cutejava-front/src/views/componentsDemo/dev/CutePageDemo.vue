<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>用于构建页面布局，提供统一的页面结构和样式</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-page>
      <cute-button>这里是按钮</cute-button>
    </cute-page>
  </div>
</template>
<script>
import CuteButton from '@/views/components/dev/CuteButton.vue'
import CutePage from '@/views/components/dev/CutePage.vue'

export default {
  name: 'CutePageDemo',
  components: { CutePage, CuteButton }
}
</script>
<style lang="scss" scoped>
ul {
  padding-left: 20px;
}
.description > li{
  font-size: 12px;
}
</style>
