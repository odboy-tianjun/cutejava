<template>
  <el-upload
    ref="upload"
    :limit="1"
    :before-upload="beforeUpload"
    :auto-upload="true"
    :headers="headers"
    :on-success="handleSuccess"
    :on-error="handleError"
    :action="ossUploadApi"
  >
    <el-button size="small" type="primary">点击上传</el-button>
    <div slot="tip" class="el-upload__tip">只能上传不超过100MB的文件</div>
  </el-upload>
</template>

<script>
import { Message } from 'element-ui'
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'

export default {
  name: 'CuteFileUpload',
  props: {
    value: {
      type: Object,
      required: true,
      default: null
    }
  },
  data() {
    return {
      headers: { 'Authorization': getToken() }
    }
  },
  computed: {
    ...mapGetters([
      'baseApi',
      'ossUploadApi'
    ])
  },
  methods: {
    beforeUpload(file) {
      let isLt2M = true
      isLt2M = file.size / 1024 / 1024 < 100
      if (!isLt2M) {
        this.$message.error('上传文件大小不能超过 100MB')
        return false
      }
      return isLt2M
    },
    // 监听上传失成功
    handleSuccess(response, file, fileList) {
      // Message.success('上传成功')
      // this.$refs.upload.clearFiles()
      console.error('response', response)
      console.error('file', file)
      console.error('fileList', fileList)
      this.value.fileUrl = response
    },
    // 监听上传失败
    handleError(e, file, fileList) {
      const msg = JSON.parse(e.message)
      Message.error(msg.message)
    }
  }
}
</script>
