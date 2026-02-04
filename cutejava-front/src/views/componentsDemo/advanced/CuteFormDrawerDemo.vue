<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>不离开当前场景完成增删改操作</li>
      <li>作为配置型功能及信息的容器</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-button type="primary" @click="showDialogForm">显示对话框表单</cute-button>
    <cute-form-drawer
      ref="formDrawer"
      :model="model"
      :rules="rules"
      title="表单抽屉"
      @submit="onSubmit"
    >
      <el-form-item label="活动名称" prop="name" label-width="100px">
        <el-input v-model="model.name" placeholder="请输入" style="width: 100%" />
      </el-form-item>
      <el-form-item label="活动区域" prop="region" label-width="100px">
        <el-select v-model="model.region" placeholder="请选择" style="width: 100%">
          <el-option label="区域一" value="shanghai" />
          <el-option label="区域二" value="beijing" />
        </el-select>
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
import CuteButton from '@/views/components/dev/CuteButton.vue'
import CuteFormDrawer from '@/views/components/advanced/CuteFormDrawer.vue'

export default {
  name: 'CuteFormDrawerDemo',
  components: { CuteFormDrawer, CuteButton },
  data() {
    return {
      model: {
        name: '',
        region: '',
        modeList: []
      },
      rules: {
        name: [
          { required: true, message: '请输入活动名称', trigger: 'blur' },
          { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
        region: [
          { required: true, message: '请选择活动区域', trigger: 'change' }
        ]
      },
      apiData: [
        { name: 'title', remark: '弹窗的标题', type: 'string', defaultValue: '默认标题', required: '否' },
        { name: 'width', remark: '弹窗的宽度', type: 'string', defaultValue: '40%', required: '否' },
        { name: 'model', remark: '绑定的表单对象', type: 'object', defaultValue: '-', required: '是' },
        { name: 'rules', remark: '表单校验规则', type: 'object', defaultValue: '-', required: '否' },
        { name: 'inline', remark: '是否平铺显示。默认是垂直排列', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'showReset', remark: '是否显示重置按钮', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'submit', remark: '提交按钮被点击回调事件', type: '(values) => {}', defaultValue: '-', required: '否' },
        { name: 'cancel', remark: '取消按钮被点击回调事件', type: '() => {}', defaultValue: '-', required: '否' },
        { name: 'reset', remark: '重置按钮被点击回调事件', type: '() => {}', defaultValue: '-', required: '否' }
      ],
      methodData: [
        { name: 'show', remark: '显示对话框', inputArgs: '-', outArgs: '-' },
        { name: 'hidden', remark: '隐藏对话框', inputArgs: '-', outArgs: '-' }
      ]
    }
  },
  methods: {
    showDialogForm() {
      this.$refs.formDrawer.show()
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
