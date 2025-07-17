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
        <el-form-item>
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button>取消</el-button>
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
  </div>
</template>

<script>

import CuteUserSelect from '@/views/components/business/CuteUserSelect.vue'
import CuteDeptTree from '@/views/components/business/CuteDeptTree.vue'

export default {
  name: 'CuteBusinessDemo',
  components: { CuteDeptTree, CuteUserSelect },
  data() {
    return {
      form: {
        user: null,
        dept: null
      }
    }
  },
  methods: {
    onUserSelectChange(data) {
      console.error('onUserSelectChange=', data)
    },
    onDeptTreeNodeClick(data) {
      console.error('onDeptTreeNodeClick=', data)
    },
    onSubmit() {
      console.error('this.form=', this.form)
    },
    onDeptTreeSearch(key) {
      console.error('onDeptTreeSearch:key', key)
      console.error('onDeptTreeSearch:form.dept', this.form.dept)
    }
  }
}
</script>

