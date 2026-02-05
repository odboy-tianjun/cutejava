<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>保存区域内已生成的结果为图片</li>
      <li>需要页面截图，或者需要将表格滚动部分展开后截图，且页面不能有明显变化的时候</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-screen-shot>
      <cute-page>
        测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试测试
      </cute-page>
    </cute-screen-shot>
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
import CuteScreenShot from '@/views/components/dev/CuteScreenShot.vue'
import CutePage from '@/views/components/dev/CutePage.vue'

export default {
  name: 'CuteScreenShotDemo',
  components: { CutePage, CuteScreenShot },
  data() {
    return {
      apiData: [
        { name: 'key', remark: '全局唯一key', type: 'string', defaultValue: 'elementIdSelector', required: '是' }
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
