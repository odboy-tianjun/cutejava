<!--
 * 查询组件Pro版
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2026-02-01
 -->
<template>
  <el-form
    ref="searchForm"
    :inline="true"
    :model.sync="model"
    label-position="right"
    label-width="90px"
    :disabled="disabled"
    size="small"
  >
    <!--    <el-form-item v-for="item in schema" :key="item.name" :prop="item.name" :label="item.title" :required="item.required === true">-->
    <el-form-item v-for="item in schema" :key="item.name" :prop="item.name" :label="item.title">
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
      />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onFormSubmit('searchForm')">查询</el-button>
      <el-button @click="onFormReset('searchForm')">重置</el-button>
    </el-form-item>
    <!-- 通过  <template v-slot:operation> 插槽后置操作按钮 -->
    <slot name="operation" />
  </el-form>
</template>

<script>

export default {
  name: 'CuteSearchFormPro',
  components: {},
  props: {
    schema: {
      type: Array,
      required: true,
      default: function() {
        return []
      }
    },
    model: {
      type: Object,
      required: true
    },
    disabled: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      // formRules: {},
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
  // mounted() {
  //   const schema = this.schema
  //   const formRules = this.formRules
  //   let trigger = 'blur'
  //   for (const element of schema) {
  //     if (element.type === 'input') {
  //       trigger = 'blur'
  //     } else {
  //       trigger = 'change'
  //     }
  //     formRules[element.name] = [
  //       { required: element.required === true, message: '请输入' + element.title, trigger: trigger }
  //     ]
  //   }
  //   this.formRules = formRules
  // },
  methods: {
    onFormSubmit(formName) {
      const that = this
      that.$refs[formName].validate((valid) => {
        if (valid) {
          that.$emit('submit', that.model)
        } else {
          return false
        }
      })
    },
    onFormReset(formName) {
      try {
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
        this.onFormSubmit(formName)
        // this.$emit('reset')
      } catch (e) {
        console.error(e)
      }
    }
  }
}
</script>

