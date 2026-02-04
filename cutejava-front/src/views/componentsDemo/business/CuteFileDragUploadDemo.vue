<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>需要用户上传文件时</li>
      <li>当需要与表单组件（如 CuteFormDialog、CuteFormDrawer）集成时</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端） @Odboy（后端）</li>
    </ul>
    <h4>基础用法</h4>
    <el-form ref="form" :model="form" label-width="100px">
      <el-form-item label="活动名称" prop="name">
        <el-input v-model="form.name" style="width: 100%" />
      </el-form-item>
      <el-form-item label="活动区域" prop="region">
        <el-select v-model="form.region" placeholder="请选择活动区域" style="width: 100%">
          <el-option label="区域一" value="shanghai" />
          <el-option label="区域二" value="beijing" />
        </el-select>
      </el-form-item>
      <el-form-item label="文件上传" prop="fileUrl">
        <cute-file-drag-upload v-model="form" mode="local" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('form')">提 交</el-button>
      </el-form-item>
    </el-form>
    <h4>与CuteFormDialog集成</h4>
    <cute-button type="primary" @click="showFormDialog">显示</cute-button>
    <cute-form-dialog ref="formDialog" title="与CuteFormDialog集成" :model="form">
      <el-form-item label="活动名称" prop="name" label-width="80px">
        <el-input v-model="form.name" style="width: 100%" />
      </el-form-item>
      <el-form-item label="活动区域" prop="region" label-width="80px">
        <el-select v-model="form.region" placeholder="请选择活动区域" style="width: 100%">
          <el-option label="区域一" value="shanghai" />
          <el-option label="区域二" value="beijing" />
        </el-select>
      </el-form-item>
      <el-form-item label="文件上传" prop="fileUrl" label-width="80px">
        <cute-file-drag-upload v-model="form" mode="local" />
      </el-form-item>
    </cute-form-dialog>
    <h4>与CuteFormDrawer集成</h4>
    <cute-button type="primary" @click="showFormDrawer">显示</cute-button>
    <cute-form-drawer ref="formDrawer" title="与CuteFormDrawer集成" :model="form">
      <el-form-item label="活动名称" prop="name" label-width="80px">
        <el-input v-model="form.name" style="width: 100%" />
      </el-form-item>
      <el-form-item label="活动区域" prop="region" label-width="80px">
        <el-select v-model="form.region" placeholder="请选择活动区域" style="width: 100%">
          <el-option label="区域一" value="shanghai" />
          <el-option label="区域二" value="beijing" />
        </el-select>
      </el-form-item>
      <el-form-item label="文件上传" prop="fileUrl" label-width="80px">
        <cute-file-drag-upload v-model="form" mode="local" />
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
  </div>
</template>
<script>
import CuteFormDialog from '@/views/components/advanced/CuteFormDialog.vue'
import CuteFileDragUpload from '@/views/components/business/CuteFileDragUpload.vue'
import CuteFormDrawer from '@/views/components/advanced/CuteFormDrawer.vue'
import CuteButton from '@/views/components/dev/CuteButton.vue'

export default {
  name: 'CuteFileDragUploadDemo',
  components: { CuteButton, CuteFormDrawer, CuteFormDialog, CuteFileDragUpload },
  data() {
    return {
      form: {
        name: '',
        region: '',
        fileUrl: ''
      },
      apiData: [
        { name: 'value', remark: '上传的文件路径', type: 'string', defaultValue: '-', required: '是' },
        { name: 'mode', remark: '上传方式。local本地、oss对象存储', type: 'string', defaultValue: 'local', required: '否' }
      ]
    }
  },
  methods: {
    showFormDialog() {
      this.$refs.formDialog.show()
    },
    showFormDrawer() {
      this.$refs.formDrawer.show()
    },
    submitForm(formName) {
      const that = this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          console.log('submitForm:form', that.form)
        } else {
          return false
        }
      })
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
