<!--
 * 简单表单抽屉：封装了常用方法和参数
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-01
 -->
<template>
  <el-drawer
    :title="title"
    :visible.sync="visible"
    :size="width"
    :before-close="beforeClose"
    :append-to-body="true"
    :close-on-press-escape="false"
    :wrapper-closable="false"
    :destroy-on-close="true"
    :show-close="true"
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
      <el-button v-prevent-re-click type="primary" @click="submitForm('form')">提 交</el-button>
    </div>
  </el-drawer>
</template>

<script>

export default {
  name: 'CuteFormDrawer',
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
    model: {
      type: Object,
      required: true,
      default: null
    },
    rules: {
      type: Object,
      required: false,
      default: function() {
        return {}
      }
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
  height: calc(100% - 60px); /* 预留出底部按钮的空间 */
  padding-right: 20px;
  -ms-overflow-style: none; /* Internet Explorer 10+ */
}

.container-form::-webkit-scrollbar {
  display: none; /* Chrome, Safari, Opera*/
}

.drawer-content-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
}

.dialog-footer{
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: right;
  background-color: white;
  border-top: 1px solid #ebeef5;
  padding: 16px 20px;
}

::v-deep(.el-drawer__header) {
  padding: 10px;
  border-bottom: 1px solid #DCDFE6;
}

::v-deep(.el-drawer__body) {
  padding: 10px 10px !important;
}

::v-deep(.el-divider--horizontal) {
  margin: 14px 0 !important;
}
</style>

