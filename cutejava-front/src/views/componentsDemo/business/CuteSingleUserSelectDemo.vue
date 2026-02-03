<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>选择单个用户信息</li>
      <li>支持用户姓名、手机号、邮箱、用户名查询</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端） @Odboy（设计）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-single-user-select v-model="user" @change="onUserSelectChange" />
    <h4>绑定表单</h4>
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="用户" prop="user">
        <cute-single-user-select v-model="form.user" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit('form')">提 交</el-button>
        <el-button type="danger" @click="onReset('form')">重 置</el-button>
      </el-form-item>
    </el-form>
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
import CuteSingleUserSelect from '@/views/components/business/CuteSingleUserSelect.vue'

export default {
  name: 'CuteSingleUserSelectDemo',
  components: { CuteSingleUserSelect },
  data() {
    return {
      user: '',
      form: {
        user: ''
      },
      apiData: [
        { name: 'value', remark: '绑定的值', type: 'string', defaultValue: '-', required: '否' },
        { name: 'change', remark: '选中用户发生改变的时候触发的回调', type: '(value) => {}', defaultValue: '-', required: '否' }
      ]
    }
  },
  methods: {
    onUserSelectChange(data) {
      console.log('onUserSelectChange', data)
    },
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log('onSubmit:form', this.form)
        } else {
          console.error('onSubmit:error submit!!')
          return false
        }
      })
    },
    onReset(formName) {
      this.$refs[formName].resetFields()
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
