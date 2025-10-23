<!--
 * 无数据
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-20
 -->
<template>
  <el-empty :description="desc" />
</template>

<script>

export default {
  name: 'CuteNoData',
  props: {
    desc: {
      type: String,
      required: false,
      default: '暂无匹配数据'
    }
  }
}
</script>

