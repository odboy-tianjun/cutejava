<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>列表上方的标准筛选栏</li>
      <li>在点击搜索和重置按钮时会触发 onSearch，参数为当前筛选值</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-search-form-pro :model="model" :schema="schema" @search="handleSearch" />
    <h4>API</h4>
    <el-table :data="apiData">
      <el-table-column prop="name" label="参数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="defaultValue" label="默认值" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
    <h4>Schema属性</h4>
    <el-table :data="schemaData">
      <el-table-column prop="name" label="属性名" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
  </div>
</template>
<script>
import CuteSearchFormPro from '@/views/components/advanced/CuteSearchFormPro.vue'

export default {
  name: 'CuteSearchFormProDemo',
  components: { CuteSearchFormPro },
  data() {
    return {
      model: {
        name: '',
        sex: ''
      },
      schema: [
        { name: 'name', title: '姓名', type: 'input' },
        { name: 'sex', title: '性别', type: 'select', dataSource: [{ label: '男', value: 'nn' }, { label: '女', value: 'mm' }] },
        { name: 'createDate', title: '创建日期', type: 'date' },
        { name: 'createTime', title: '创建时间', type: 'datetime' }
      ],
      apiData: [
        { name: 'model', remark: '绑定的表单对象', type: 'object', defaultValue: '-', required: '是' },
        { name: 'schema', remark: '表单定义', type: 'object', defaultValue: '-', required: '是' },
        { name: 'v-slot:operation', remark: '后置按钮区域', type: 'template', defaultValue: '-', required: '否' },
        { name: 'search', remark: '搜索或重置按钮被点击回调事件', type: '(values) => {}', defaultValue: '-', required: '否' }
      ],
      schemaData: [
        { name: 'name', remark: '表单项名称', type: 'string', defaultValue: '-', required: '是' },
        { name: 'title', remark: '表单项标题', type: 'string', defaultValue: '-', required: '是' },
        { name: 'type', remark: '表单项类型。input 输入框 | select 单选框 | date 日期选择框 | datetime 日期时间选择框', type: 'string', defaultValue: '-', required: '是' },
        { name: 'dataSource', remark: '数据源，当type=select时必填', type: 'array', defaultValue: '[]', required: '否' }
      ]
    }
  },
  methods: {
    handleSearch(values) {
      console.log('handleSearch', values)
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
