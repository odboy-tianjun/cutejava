<template>
  <div style="padding: 50px">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>表单值绑定</span>
      </div>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="用户" prop="user">
          <!-- v-model 本质上是 :value 和 @input 事件的语法糖 -->
          <cute-user-select v-model="form.user" />
        </el-form-item>
        <el-form-item label="部门" prop="dept">
          <cute-dept-tree v-model="form.dept" />
        </el-form-item>
        <el-form-item label="产品线" prop="productLine">
          <cute-product-line-select v-model="form.productLine" @detail="onProductLineDetailChange" />
        </el-form-item>
        <el-form-item label="产品线Pro" prop="productLinePro">
          <cute-product-line-select-pro v-model="form.productLinePro" @detail="onProductLineDetailProChange" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit('form')">提 交</el-button>
          <el-button type="danger" @click="onReset('form')">重 置</el-button>
          <el-button>取 消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card style="margin-top: 10px">
      <div slot="header" class="clearfix">
        <span>用户选择框</span>
      </div>
      <cute-user-select @change="onUserSelectChange" />
    </el-card>
    <el-card style="margin-top: 10px">
      <div slot="header" class="clearfix">
        <span>部门树</span>
      </div>
      <cute-dept-tree @node-click="onDeptTreeNodeClick" @search="onDeptTreeSearch" />
    </el-card>
    <el-card style="margin-top: 10px">
      <div slot="header" class="clearfix">
        <span>产品线选择框</span>
      </div>
      <cute-product-line-select @change="onProductLineChange" @detail="onProductLineDetailChange" />
    </el-card>
    <el-card style="margin-top: 10px">
      <div slot="header" class="clearfix">
        <span>产品线选择框Pro</span>
      </div>
      <cute-product-line-select-pro @change="onProductLineDetailProChange" />
    </el-card>
  </div>
</template>

<script>

import CuteUserSelect from '@/views/components/business/CuteUserSelect.vue'
import CuteDeptTree from '@/views/components/business/CuteDeptTree.vue'
import CuteProductLineSelect from '@/views/components/business/CuteProductLineSelect.vue'
import CuteProductLineSelectPro from '@/views/components/business/CuteProductLineSelectPro.vue'

export default {
  name: 'CuteBusinessDemo',
  components: { CuteProductLineSelectPro, CuteProductLineSelect, CuteDeptTree, CuteUserSelect },
  data() {
    return {
      form: {
        user: null,
        dept: null,
        productLine: null,
        productLinePro: null
      },
      searchFormModel: {}
    }
  },
  methods: {
    onUserSelectChange(data) {
      console.log('onUserSelectChange=', data)
    },
    onDeptTreeNodeClick(data) {
      console.log('onDeptTreeNodeClick=', data)
    },
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          console.log('this.form=', this.form)
        } else {
          console.error('error submit!!')
          return false
        }
      })
    },
    onReset(formName) {
      this.$refs[formName].resetFields()
    },
    onDeptTreeSearch(key) {
      console.log('onDeptTreeSearch:key', key)
      console.log('onDeptTreeSearch:form.dept', this.form.dept)
    },
    onProductLineChange(data) {
      console.log('onProductLineChange', data)
    },
    onProductLineDetailChange(data) {
      console.log('onProductLineDetailChange', data)
    },
    onProductLineDetailProChange(data) {
      console.log('onProductLineDetailProChange', data)
    }
  }
}
</script>

