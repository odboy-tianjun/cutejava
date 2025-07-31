<template>
  <el-dialog
    :title="title"
    :visible.sync="visible"
    :width="width"
    :fullscreen="fullscreen"
    :before-close="beforeClose"
    :modal="false"
  >
    <div class="container-form">
      <el-form ref="form" :model="form.model" :rules="form.rules">
        <!-- 这里是插槽，用于渲染传入的 el-form-item -->
        <slot />
      </el-form>
    </div>
    <el-divider />
    <div class="dialog-footer" style="padding-right: 30px;text-align: right">
      <el-button type="danger" @click="hidden">取 消</el-button>
      <!--<el-button @click="resetForm('form')">重 置</el-button>-->
      <el-button type="primary" @click="submitForm('form')">提 交</el-button>
    </div>
  </el-dialog>
</template>

<script>

export default {
  name: 'CuteFormDialog',
  props: {
    title: {
      type: String,
      required: true,
      default: '默认标题'
    },
    width: {
      type: String,
      required: false,
      default: '40%'
    },
    fullscreen: {
      type: Boolean,
      required: false,
      default: false
    },
    form: {
      type: Object,
      required: true,
      default: null
    }
  },
  data() {
    return {
      visible: false
    }
  },
  methods: {
    show() {
      this.visible = true
    },
    hidden() {
      this.resetForm('form')
      this.visible = false
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$emit('submit', this.form.model)
        } else {
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    beforeClose() {
      this.hidden()
    }
  }
}
</script>
<style scoped>
.container-form {
  overflow-y: scroll; /* 启用滚动 */
  scrollbar-width: none; /* Firefox */
  padding-right: 20px;
  -ms-overflow-style: none; /* Internet Explorer 10+ */
}

.container-form::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera*/
}
</style>

