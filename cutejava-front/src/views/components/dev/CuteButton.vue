<!--
 * 统一按钮风格
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2026-01-30
 -->
<template>
  <el-button
    :type="innerType"
    :plain="innerPlain"
    size="mini"
    :disabled="disabled"
    :loading="loading"
    :icon="icon"
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
    icon: {
      type: String,
      default: ''
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
  computed: {
    innerType() {
      if (this.type) {
        if (this.type === 'primary') {
          return 'primary'
        }
        if (this.type === 'secondary') {
          return 'primary'
        }
        if (this.type === 'normal') {
          return null
        }
        if (this.type === 'text') {
          return 'text'
        }
      }
      return null
    },
    innerPlain() {
      if (this.type) {
        if (this.type === 'primary') {
          return false
        }
        if (this.type === 'secondary') {
          return true
        }
        if (this.type === 'normal') {
          return false
        }
        if (this.type === 'text') {
          return false
        }
      }
      return false
    }
  },
  methods: {
    handleClick(event) {
      this.$emit('click', event)
    }
  }
}
</script>

