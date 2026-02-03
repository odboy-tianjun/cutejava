<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>当需要在容器内展示大量内容，需要横向滚动查看时</li>
      <li>当希望在不同操作系统（Windows/Mac）上保持一致的滚动条样式时</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-horizontal-scroll-bar>
      <div style="width: 3000px;min-width: 3000px">模拟超长内容</div>
    </cute-horizontal-scroll-bar>
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
import CuteHorizontalScrollBar from '@/views/components/dev/CuteHorizontalScrollBar.vue'

export default {
  name: 'CuteHorizontalScrollBarDemo',
  components: { CuteHorizontalScrollBar },
  data() {
    return {
      apiData: [
        { name: 'width', remark: '内部容器宽度', type: 'string', defaultValue: '1280px', required: '否' }
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
