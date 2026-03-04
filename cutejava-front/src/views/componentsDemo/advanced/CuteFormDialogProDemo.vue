<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>用于创建一个实体或收集信息</li>
      <li>需要对输入的数据类型进行校验时</li>
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
        { name: 'sex', title: '性别', type: 'select', required: true, sOpts: [{ label: '男', value: 'nn' }, { label: '女', value: 'mm' }] },
        { name: 'createDate', title: '创建日期', type: 'date', required: false },
        { name: 'createTime', title: '创建时间', type: 'datetime', required: false },
        { name: 'price', title: '价格', type: 'number', required: true, nMin: 0, nMax: 100, nStep: 10.5, nPrecision: 1 },
        { name: 'extraFile', title: '附件', type: 'upload', required: true }
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
        {
          name: 'type',
          remark: '表单项类型。input 输入框 | number 输入框(仅数字) | select 单选框 | date 日期选择框 | datetime 日期时间选择框 | upload 文件上传',
          type: 'string',
          defaultValue: '-',
          required: '是'
        },
        { name: 'required', remark: '是否必填', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'disabled', remark: '是否禁用', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'pattern', remark: '正则表达式，用于匹配输入值', type: 'string', defaultValue: '-', required: '否' },
        {
          name: 'validator',
          remark: '自定义验证函数',
          type: '(rule, value, callback) => { // 验证失败 callback(new Error(\'请输入密码\')); // 验证通过，无错误信息返回 callback() }',
          defaultValue: '-',
          required: '否'
        },
        { name: 'sOpts', remark: '数据源，当type=select时必填', type: 'array', defaultValue: '[]', required: '否' },
        { name: 'sMultiple', remark: '是否多选，当type=select时有效', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'nMin', remark: '最小值，当type=number时有效', type: 'number', defaultValue: '-100000000', required: '否' },
        { name: 'nMax', remark: '最大值，当type=number时有效', type: 'number', defaultValue: '100000000', required: '否' },
        { name: 'nStep', remark: '步长，当type=number时有效', type: 'number', defaultValue: '10', required: '否' },
        { name: 'nPrecision', remark: '精度，当type=number时有效', type: 'number', defaultValue: '0', required: '否' }
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
