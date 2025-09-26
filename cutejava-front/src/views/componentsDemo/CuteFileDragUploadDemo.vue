<template>
  <div class="app-container">
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
        <cute-file-drag-upload v-model="form" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm('form')">提 交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import CuteFileDragUpload from '@/views/components/business/CuteFileDragUpload'

export default {
  name: 'CuteFileDragUploadDemo',
  components: { CuteFileDragUpload },
  data() {
    return {
      form: {
        name: '',
        region: '',
        fileUrl: ''
      }
    }
  },
  methods: {
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

