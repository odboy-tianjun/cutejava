<!--
 * 段落标题组件：占一行，支持嵌套按钮
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-13
 -->
<template>
  <el-row>
    <el-col :span="12">
      <h4>
        <i class="el-icon-s-help" style="color: #409EFF;font-size: 18px" />
        <i style="color: #303133;">{{ title }}</i>
      </h4>
    </el-col>
    <el-col :span="12" style="text-align: right;line-height: 64px;height: 64px;padding-right: 30px">
      <slot />
    </el-col>
  </el-row>
</template>

<script>

export default {
  name: 'CuteSectionTitle',
  props: {
    title: {
      type: String,
      required: true,
      default: '默认标题'
    }
  }
}
</script>
