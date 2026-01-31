<!--
 * 简单表单弹窗：封装了常用方法和参数
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 -->
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
      <el-form ref="form" :model="model" :rules="rules" :inline="inline" size="small">
        <!-- 这里是插槽，用于渲染传入的 el-form-item -->
        <slot />
      </el-form>
    </div>
    <el-divider />
    <div class="dialog-footer">
      <el-button type="danger" @click="hidden">取 消</el-button>
      <!--<el-button @click="resetForm('form')">重 置</el-button>-->
      <el-button v-prevent-re-click type="primary" @click="submitForm('form')">提 交</el-button>
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
    model: {
      type: Object,
      required: true,
      default: null
    },
    rules: {
      type: Object,
      required: true,
      default: null
    },
    // 是否平铺表单
    inline: {
      type: Boolean,
      required: false,
      default: false
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
          this.$emit('submit', this.model)
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
<style lang="scss" scoped>
.container-form {
  overflow-y: scroll; /* 启用滚动 */
  scrollbar-width: none; /* Firefox */
  padding-right: 20px;
  -ms-overflow-style: none; /* Internet Explorer 10+ */
  max-height: calc(100vh - 200px); /* 调整高度以适应页头和底部按钮 */
}

.container-form::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera*/
}

.dialog-content-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.dialog-footer {
  text-align: right;
  border-top: 1px solid #ebeef5;
  padding: 16px 0 0 0;
  background-color: white;
  margin-top: auto;
}

::v-deep(.el-dialog__body) {
  padding: 20px 20px !important;
  height: 90%;
  display: flex;
  flex-direction: column;
}

::v-deep(.el-divider--horizontal) {
  margin: 0 !important;
}
</style>

