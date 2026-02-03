<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>选择、绑定部门明细信息，需要根部门和子部门以及路径的信息等</li>
      <li>统一部门数据来源</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端） @Odboy（设计）@Odboy（后端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-product-line-select-pro @change="onProductLineDetailProChange" />
    <h4>绑定表单</h4>
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="产品线Pro" prop="productLine">
        <cute-product-line-select v-model="form.productLine" @detail="onProductLineDetailProChange" />
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
import CuteProductLineSelect from '@/views/components/business/CuteProductLineSelect.vue'
import CuteProductLineSelectPro from '@/views/components/business/CuteProductLineSelectPro.vue'

export default {
  name: 'CuteProductLineSelectProDemo',
  components: { CuteProductLineSelectPro, CuteProductLineSelect },
  data() {
    return {
      form: {
        productLine: null
      },
      apiData: [
        { name: 'value', remark: '绑定的值', type: 'string', defaultValue: '-', required: '否' }
      ]
    }
  },
  methods: {
    onProductLineDetailProChange(data) {
      console.log('onProductLineDetailProChange', data)
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
