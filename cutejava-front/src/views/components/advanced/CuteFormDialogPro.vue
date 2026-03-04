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
      <el-form ref="form" :model="model" :rules="dynamicRules" :inline="false" size="small" label-position="left" label-width="100px">
        <el-form-item v-for="item in schema" :key="item.name" :prop="item.name" :label="item.title" :required="item.required === true">
          <!-- 输入框 -->
          <el-input
            v-if="item.type === 'input'"
            v-model="model[item.name]"
            :placeholder="`请输入${item.title}`"
            :disabled="schema.disabled === true"
            clearable
            style="width: 100%"
          />
          <!-- 输入框(仅数字) -->
          <el-input-number
            v-if="item.type === 'number'"
            v-model="model[item.name]"
            :placeholder="`请输入${item.title}`"
            :disabled="schema.disabled === true"
            style="width: 100%"
            :min="item.nMin ? item.nMin : -100000000"
            :max="item.nMax ? item.nMax : 100000000"
            :step="item.nStep ? item.nStep : 10"
            :precision="item.nPrecision ? item.nPrecision : 0"
          />
          <!-- 选择框 -->
          <el-select
            v-if="item.type === 'select'"
            v-model="model[item.name]"
            clearable
            :placeholder="`请选择${item.title}`"
            :disabled="schema.disabled === true"
            style="width: 100%"
            :multiple="schema.multiple ? schema.multiple : false"
            filterable
          >
            <el-option
              v-for="option in (item.sOpts || [])"
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
            :disabled="schema.disabled === true"
            clearable
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
            style="width: 100%"
            :picker-options="pickerOptions"
          />
          <!-- 日期时间选择 -->
          <el-date-picker
            v-if="item.type === 'datetime'"
            v-model="model[item.name]"
            type="datetimerange"
            align="right"
            clearable
            :disabled="schema.disabled === true"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            :default-time="['00:00:00', '23:59:59']"
            style="width: 100%"
            :picker-options="pickerOptions"
          />
          <!-- 文件上传 -->
          <cute-file-drag-upload
            v-if="item.type === 'upload'"
            v-model="model[item.name]"
            mode="local"
          />
        </el-form-item>
      </el-form>
    </div>
    <el-divider />
    <div class="dialog-footer">
      <el-button type="danger" @click="hidden">取 消</el-button>
      <el-button v-if="showReset" @click="resetForm('form')">重 置</el-button>
      <el-button v-prevent-re-click type="primary" @click="submitForm('form')">提 交</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { IsNotBlank } from '@/utils/KitUtil'
import CuteFileDragUpload from '@/views/components/business/CuteFileDragUpload.vue'
export default {
  name: 'CuteFormDialogPro',
  components: { CuteFileDragUpload },
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
    },
    showReset: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      visible: false,
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
      for (const element of schema) {
        const rule1 = {}
        // 布尔值，表示该字段是否必填
        rule1.required = element.required === true
        // 字符串，触发验证的时机，如 blur（失去焦点时），change（数据改变时），input（输入时）等
        rule1.trigger = (element.type === 'input' || element.type === 'number') ? 'blur' : 'change'
        // 字符串，验证失败时显示的错误信息
        rule1.message = (element.type === 'input' || element.type === 'number') ? '请输入' + element.title : '请选择' + element.title
        // 验证的类型，如 string, number, date, array, object 等。其中array适用于checkbox，date类型会触发日期值对象校验
        let ruleType = null
        if (element.type === 'input') {
          ruleType = 'string'
        } else if (element.type === 'number') {
          ruleType = 'number'
        } else if (element.type === 'select') {
          ruleType = null
        } else if (element.type === 'date') {
          ruleType = null
        } else if (element.type === 'datetime') {
          ruleType = null
        }
        if (ruleType != null) {
          rule1.type = ruleType
        }
        // 当类型为number时的限制
        if (element.type === 'number') {
          rule1.min = element.nMin ? element.nMin : -100000000
          rule1.max = element.nMax ? element.nMax : 100000000
        }
        // 当存在正则表达式时，用于匹配输入值
        if (IsNotBlank(element.pattern)) {
          rule1.pattern = element.pattern
        }
        // 当存在自定义验证器时，自定义验证函数。该函数接收两个参数：rule 和 value，返回一个 Promise 或一个布尔值
        if (element.validator) {
          rule1.validator = element.validator
        }
        formRules[element.name] = [rule1]
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
        if (element.type === 'input' || element.type === 'number' || element.type === 'select' || element.type === 'upload') {
          model[element.name] = null
          continue
        }
        if (element.type === 'date' || element.type === 'datetime') {
          model[element.name] = []
        }
      }
      this.model = { ...model }
      this.$emit('reset')
    },
    beforeClose() {
      this.hidden()
      this.$emit('cancel')
    }
  }
}
</script>
<style lang="scss" scoped>
.container-form {
  overflow-y: scroll; /* 启用滚动 */
  scrollbar-width: none; /* Firefox */
  padding-top: 10px;
  padding-left: 15px;
  padding-right: 15px;
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
  padding: 10px 10px !important;
  height: 90%;
  display: flex;
  flex-direction: column;
}

::v-deep(.el-divider--horizontal) {
  margin: 0 !important;
}
</style>

