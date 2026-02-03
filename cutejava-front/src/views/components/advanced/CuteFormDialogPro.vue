<!--
 * 表单弹窗Pro
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2026-02-01
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
      <el-form ref="form" :model="model" :rule="dynamicRules" :inline="false" size="small" label-position="left" label-width="100px">
        <el-form-item v-for="item in schema" :key="item.name" :prop="item.name" :label="item.title" :required="item.required === true">
          <!-- 输入框 -->
          <el-input
            v-if="item.type === 'input'"
            v-model="model[item.name]"
            :placeholder="`请输入${item.title}`"
            clearable
            style="width: 100%"
          />
          <!-- 选择框 -->
          <el-select
            v-if="item.type === 'select'"
            v-model="model[item.name]"
            clearable
            :placeholder="`请选择${item.title}`"
            style="width: 100%"
          >
            <el-option
              v-for="option in (item.dataSource || [])"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
          <!-- 日期选择 -->
          <el-date-picker
            v-if="item.type === 'date'"
            v-model="model[item.name]"
            type="daterange"
            align="right"
            unlink-panels
            clearable
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            :picker-options="pickerOptions"
            style="width: 100%"
          />
          <!-- 日期时间选择 -->
          <el-date-picker
            v-if="item.type === 'datetime'"
            v-model="model[item.name]"
            type="datetimerange"
            align="right"
            clearable
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            :default-time="['00:00:00', '23:59:59']"
            :picker-options="pickerOptions"
            style="width: 100%"
          />
        </el-form-item>
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
  name: 'CuteFormProDialog',
  props: {
    /**
     * 弹窗标题
     */
    title: {
      type: String,
      required: true,
      default: '默认标题'
    },
    /**
     * 弹窗宽度
     */
    width: {
      type: String,
      required: false,
      default: '40%'
    },
    /**
     * 是否全屏
     */
    fullscreen: {
      type: Boolean,
      required: false,
      default: false
    },
    /**
     * 表单定义
     */
    schema: {
      type: Array,
      required: true,
      default: function() {
        return []
      }
    },
    /**
     * 绑定值
     */
    model: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      visible: false,
      rules: {},
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date()
            const start = new Date()
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
            picker.$emit('pick', [start, end])
          }
        }]
      }
    }
  },
  computed: {
    dynamicRules() {
      const schema = this.schema
      const formRules = {}
      let trigger = 'blur'
      for (const element of schema) {
        if (element.type === 'input') {
          trigger = 'blur'
        } else {
          trigger = 'change'
        }
        formRules[element.name] = [
          { required: element.required === true, message: '请输入' + element.title, trigger: trigger }
        ]
      }
      return formRules
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
      // 调用默认的重置
      this.$refs[formName].resetFields()
      // 根据schema配置重置顽固选项
      const schema = this.schema
      const model = this.model
      for (const element of schema) {
        if (element.type === 'input' || element.type === 'select') {
          model[element.name] = null
          continue
        }
        if (element.type === 'date' || element.type === 'datetime') {
          model[element.name] = []
        }
      }
      this.model = { ...model }
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

