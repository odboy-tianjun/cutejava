<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>不离开当前场景完成增删改操作</li>
      <li>需要确认用户的操作，明确操作会产生的结果，提醒告知用户</li>
      <li>作为配置型功能及信息的容器</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-button type="primary" @click="showDialogForm">显示</cute-button>
    <cute-form-dialog-pro
      ref="formDialogPro"
      :model="model"
      :schema="schema"
      title="表单对话框Pro"
      @submit="onSubmit"
    />

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
    <h4>方法</h4>
    <el-table :data="methodData">
      <el-table-column prop="name" label="函数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="inputArgs" label="入参" />
      <el-table-column prop="outArgs" label="出参" />
    </el-table>
  </div>
</template>
<script>
import CuteFormDialogPro from '@/views/components/advanced/CuteFormDialogPro.vue'
import CuteButton from '@/views/components/dev/CuteButton.vue'

export default {
  name: 'CuteFormDialogProDemo',
  components: { CuteButton, CuteFormDialogPro },
  data() {
    return {
      model: {},
      schema: [
        { name: 'name', title: '姓名', type: 'input', required: true },
        { name: 'sex', title: '性别', type: 'select', required: true, dataSource: [{ label: '男', value: 'nn' }, { label: '女', value: 'mm' }] },
        { name: 'createDate', title: '创建日期', type: 'date', required: false },
        { name: 'createTime', title: '创建时间', type: 'datetime', required: false }
      ],
      apiData: [
        { name: 'title', remark: '弹窗的标题', type: 'string', defaultValue: '默认标题', required: '否' },
        { name: 'width', remark: '弹窗的宽度', type: 'string', defaultValue: '40%', required: '否' },
        { name: 'fullscreen', remark: '是否全屏', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'model', remark: '绑定的表单对象', type: 'object', defaultValue: '-', required: '是' },
        { name: 'schema', remark: '表单定义', type: 'object', defaultValue: '-', required: '是' },
        { name: 'showReset', remark: '是否显示重置按钮', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'submit', remark: '提交按钮被点击回调事件', type: '(values) => {}', defaultValue: '-', required: '否' },
        { name: 'cancel', remark: '取消按钮被点击回调事件', type: '() => {}', defaultValue: '-', required: '否' },
        { name: 'reset', remark: '重置按钮被点击回调事件', type: '() => {}', defaultValue: '-', required: '否' }
      ],
      schemaData: [
        { name: 'name', remark: '表单项名称', type: 'string', defaultValue: '-', required: '是' },
        { name: 'title', remark: '表单项标题', type: 'string', defaultValue: '-', required: '是' },
        { name: 'type', remark: '表单项类型。input 输入框 | select 单选框 | date 日期选择框 | datetime 日期时间选择框', type: 'string', defaultValue: '-', required: '是' },
        { name: 'required', remark: '是否必填', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'dataSource', remark: '数据源，当type=select时必填', type: 'array', defaultValue: '[]', required: '否' }
      ],
      methodData: [
        { name: 'show', remark: '显示对话框', inputArgs: '-', outArgs: '-' },
        { name: 'hidden', remark: '隐藏对话框', inputArgs: '-', outArgs: '-' }
      ]
    }
  },
  methods: {
    showDialogForm() {
      this.$refs.formDialogPro.show()
    },
    onSubmit(values) {
      console.log('onSubmit', values)
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
