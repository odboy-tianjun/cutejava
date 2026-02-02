<!--
 * 按钮
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
      default: 'normal'
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
  data() {
    return {
      innerType: null,
      innerPlain: false
    }
  },
  created() {
    if (this.type) {
      if (this.type === 'primary') {
        this.innerType = 'primary'
        this.innerPlain = false
      } else if (this.type === 'secondary') {
        this.innerType = 'primary'
        this.innerPlain = true
      } else if (this.type === 'normal') {
        this.innerType = null
        this.innerPlain = false
      } else if (this.type === 'text') {
        this.innerType = 'text'
        this.innerPlain = false
      } else {
        this.innerType = null
        this.innerPlain = false
      }
    } else {
      this.innerType = null
      this.innerPlain = false
    }
  },
  methods: {
    handleClick(event) {
      this.$emit('click', event)
    }
  }
}
</script>

