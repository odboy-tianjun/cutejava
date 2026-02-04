<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>表单中出现可编辑列表时使用，目前限定在普通表格，不包含可编辑展开表格、可编辑树表等</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-edit-table v-model="dataSource" primary-key="id" :schema="schema" />
    <h4>与CuteFormDrawer集成 <el-button type="text" @click="showFormDrawer">显示抽屉</el-button></h4>
    <cute-form-drawer ref="formDrawer" title="与CuteFormDrawer集成" :model="form">
      <el-form-item label="可编辑表格" prop="editValues" label-width="100px">
        <cute-edit-table v-model="form.editValues" primary-key="id" :schema="schema" />
      </el-form-item>
    </cute-form-drawer>
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
import CuteEditTable from '@/views/components/advanced/CuteEditTable.vue'
import CuteFormDrawer from '@/views/components/advanced/CuteFormDrawer.vue'

export default {
  name: 'CuteEditTableDemo',
  components: { CuteFormDrawer, CuteEditTable },
  data() {
    return {
      schema: [
        { name: 'name', title: '姓名', type: 'input' },
        { name: 'sex', title: '性别', type: 'select', dataSource: [{ label: '男', value: 'nn' }, { label: '女', value: 'mm' }] },
        { name: 'description', title: '描述', type: 'input' }
      ],
      dataSource: [
        { id: 1, name: '小明', sex: 'mm', description: '小明牛P' },
        { id: 4, name: '小绿', sex: 'nn', description: '小绿牛P' },
        { id: 5, name: '小黄', sex: 'nn', description: '小黄牛P' },
        { id: 6, name: '小紫', sex: 'nn', description: '小紫牛P' },
        { id: 17, name: '小郑', sex: 'mm', description: '小郑牛P' },
        { id: 18, name: '小王', sex: 'nn', description: '小王牛P' }
      ],
      form: {
        editValues: [
          { id: 1, name: '小明', sex: 'mm', description: '小明牛P' },
          { id: 4, name: '小绿', sex: 'nn', description: '小绿牛P' },
          { id: 5, name: '小黄', sex: 'nn', description: '小黄牛P' },
          { id: 6, name: '小紫', sex: 'nn', description: '小紫牛P' },
          { id: 17, name: '小郑', sex: 'mm', description: '小郑牛P' },
          { id: 18, name: '小王', sex: 'nn', description: '小王牛P' }
        ]
      },
      apiData: [
        { name: 'value', remark: '绑定的值', type: 'array', defaultValue: '-', required: '否' },
        { name: 'primary-key', remark: '对象中的哪个字段设置为主键', type: 'string', defaultValue: 'id', required: '是' },
        { name: 'height', remark: '表格高度', type: 'number', defaultValue: 'height', required: '否' },
        { name: 'schema', remark: '表格定义', type: 'array', defaultValue: '[]', required: '是' }
      ],
      schemaData: [
        { name: 'name', remark: '表单项名称', type: 'string', defaultValue: '-', required: '是' },
        { name: 'title', remark: '表单项标题', type: 'string', defaultValue: '-', required: '是' },
        { name: 'type', remark: '表单项类型。input 输入框 | select 单选框', type: 'string', defaultValue: '-', required: '是' },
        { name: 'dataSource', remark: '当type=select时必填', type: 'array', defaultValue: '[]', required: '否' }
      ]
    }
  },
  methods: {
    showFormDrawer() {
      this.$refs.formDrawer.show()
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
