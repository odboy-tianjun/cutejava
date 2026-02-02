<!--
 * 文字链接
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-26
 -->
<template>
  <el-link
    type="primary"
    :underline="false"
    :href="href"
    target="_blank"
    :disabled="disabled"
  >{{ title }}</el-link>
</template>

<script>

export default {
  name: 'CuteLink',
  props: {
    title: {
      type: String,
      required: true,
      default: '默认标题'
    },
    href: {
      type: String,
      required: true,
      default: 'https://gitee.com/odboy-tianjun'
    },
    disabled: {
      type: Boolean,
      required: false,
      default: false
    }
  }
}
</script>

