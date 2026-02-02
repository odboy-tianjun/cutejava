<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>当需要展示空状态或无数据内容时</li>
      <li>需要统一空状态的视觉呈现时</li>
      <li>在数据加载失败或列表为空的场景</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-no-data />
  </div>
</template>
<script>
import CuteNoData from '@/views/components/dev/CuteNoData.vue'

export default {
  name: 'CuteNoDataDemo',
  components: { CuteNoData },
  data() {
    return {
      apiData: [
        { name: 'desc', remark: '提示信息', type: 'string', defaultValue: '暂无匹配数据', required: '否' }
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
