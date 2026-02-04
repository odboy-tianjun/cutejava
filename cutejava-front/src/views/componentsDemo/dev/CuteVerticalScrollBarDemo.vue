<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>当需要在容器内展示大量内容，需要纵向滚动查看时</li>
      <li>当希望在不同操作系统（Windows/Mac）上保持一致的滚动条样式时</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-vertical-scroll-bar>
      <div style="height: 3000px;min-height: 3000px">模拟超高内容</div>
    </cute-vertical-scroll-bar>
    <h4>API</h4>
    <el-table :data="apiData">
      <el-table-column prop="name" label="参数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="defaultValue" label="默认值" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
  </div>
</template>
<script>
import CuteVerticalScrollBar from '@/views/components/dev/CuteVerticalScrollBar.vue'

export default {
  name: 'CuteVerticalScrollBarDemo',
  components: { CuteVerticalScrollBar },
  data() {
    return {
      apiData: [
        { name: 'height', remark: '内部容器高度', type: 'string', defaultValue: '100px', required: '否' }
      ]
    }
  }
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
