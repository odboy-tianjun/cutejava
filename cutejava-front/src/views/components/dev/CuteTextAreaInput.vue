<!--
 * 多行文本输入组件
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2026-02-01
 -->
<template>
  <el-input
    v-model="content"
    type="textarea"
    :autosize="{ minRows: 1, maxRows: 5}"
    placeholder="请输入内容"
    :maxlength="maxlength"
    :disabled="disabled"
    show-word-limit
    :clearable="true"
    resize="vertical"
    autocomplete="false"
    auto-complete="false"
    :readonly="readonly"
    style="width: 100%"
    @input="handleInput"
  />
</template>

<script>
export default {
  name: 'CuteTextAreaInput',
  props: {
    disabled: {
      type: Boolean,
      required: false,
      default: false
    },
    readonly: {
      type: Boolean,
      required: false,
      default: false
    },
    maxlength: {
      type: Number,
      required: false,
      default: 120
    },
    value: {
      type: String,
      required: true,
      default: ''
    }
  },
  data() {
    return {
      content: this.value
    }
  },
  methods: {
    handleInput(value) {
      this.$emit('input', value)
    }
  }
}
</script>

