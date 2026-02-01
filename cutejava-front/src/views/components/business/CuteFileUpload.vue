<!--
 * 文件上传组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-07-29
 -->
<template>
  <el-upload
    ref="upload"
    :limit="1"
    :before-upload="beforeUpload"
    :auto-upload="true"
    :headers="headers"
    :on-success="handleSuccess"
    :on-error="handleError"
    :action="mode === 'oss' ? ossUploadApplicationApi: fileUploadApplicationApi"
  >
    <el-button size="small" type="primary">点击上传</el-button>
    <div slot="tip" class="el-upload__tip">只能上传不超过100MB的文件</div>
  </el-upload>
</template>

<script>
import { mapGetters } from 'vuex'
import { getToken } from '@/utils/auth'
import CsMessage from '@/utils/elementui/KitMessage'

export default {
  name: 'CuteFileUpload',
  props: {
    value: {
      type: Object,
      required: true,
      default: null
    },
    /**
     * 上传方式：local、oss
     */
    mode: {
      type: String,
      required: true,
      default: 'oss'
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
      'fileUploadApplicationApi',
      'ossUploadApplicationApi'
    ])
  },
  methods: {
    beforeUpload(file) {
      let isLt2M = true
      isLt2M = file.size / 1024 ** 2 < 100
      if (!isLt2M) {
        this.$message.error('上传文件大小不能超过 100MB')
        return false
      }
      return isLt2M
    },
    // 监听上传失成功
    handleSuccess(response, file, fileList) {
      // this.$refs.upload.clearFiles()
      // console.error('response', response)
      // console.error('file', file)
      // console.error('fileList', fileList)
      this.value.fileUrl = response
      // 绑定on-success事件
      this.$emit('on-success', response, file, fileList)
    },
    // 监听上传失败
    handleError(e, file, fileList) {
      const msg = JSON.parse(e.message)
      CsMessage.Error(msg.message)
      // 绑定on-error事件
      this.$emit('on-error', e, file, fileList)
    }
  }
}
</script>
