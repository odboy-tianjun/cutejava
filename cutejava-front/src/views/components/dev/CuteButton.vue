<!--
 * 统一按钮风格
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2026-01-30
 -->
<template>
  <el-button
    :type="type"
    size="mini"
    :disabled="disabled"
    :loading="loading"
    @click="handleClick"
  >
    <slot />
  </el-button>
</template>

<script>
export default {
  name: 'CuteButton',
  props: {
    type: {
      type: String,
      default: 'primary'
    },
    disabled: {
      type: Boolean,
      default: false
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    handleClick(event) {
      this.$emit('click', event)
    }
  }
}
</script>

