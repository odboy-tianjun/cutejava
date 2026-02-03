<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>输入备注、说明等大量普通文本时</li>
      <li>需要限制输入文本的长度</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-text-area-input v-model="content" />
    <h4>禁用状态</h4>
    <cute-text-area-input v-model="content" disabled />
    <h4>只读状态</h4>
    <cute-text-area-input v-model="content" readonly />
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
import CuteTextAreaInput from '@/views/components/dev/CuteTextAreaInput.vue'

export default {
  name: 'CuteTextAreaInputDemo',
  components: { CuteTextAreaInput },
  data() {
    return {
      content: '这里是很多很多的文本这里是很多很多的文本这里是很多很多的文本这里是很多很多的文本这里是很多很多的文本这里是很多很多的文本这里是很多很多的文本这里是很多很多的文本',
      apiData: [
        { name: 'disabled', remark: '是否禁用', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'readonly', remark: '是否只读', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'maxlength', remark: '最大长度', type: 'number', defaultValue: '120', required: '否' },
        { name: 'value', remark: '绑定的值', type: 'string', defaultValue: '-', required: '是' }
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
