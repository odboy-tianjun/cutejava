<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>文字链接等</li>
      <li>需要另起页面，不在当前页面打开</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <el-row>
      <cute-link title="百度一下" href="https://www.baidu.com" />
    </el-row>
    <h4>禁用状态</h4>
    <el-row>
      <cute-link title="百度一下" href="https://www.baidu.com" disabled />
    </el-row>
    <h4>API</h4>
    <el-table :data="apiData">
      <el-table-column prop="name" label="参数" />
      <el-table-column prop="remark" label="说明" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="defaultValue" label="默认值" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
  </div>
</template>
<script>
import CuteLink from '@/views/components/dev/CuteLink.vue'

export default {
  name: 'CuteLinkDemo',
  components: { CuteLink },
  data() {
    return {
      apiData: [
        { name: 'title', remark: '标题', type: 'string', defaultValue: '-', required: '是' },
        { name: 'href', remark: '任何有效文档的相对或绝对URL', type: 'string', defaultValue: '-', required: '是' }
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
