<template>
  <div class="app-container">
    <el-alert :closable="false" title="三级菜单1" type="success" />
    <el-form label-width="170px" style="margin-top: 20px">
      <el-form-item label="三级菜单缓存功能测试区">
        <el-input v-model="input" placeholder="请输入内容" style="width: 360px;" />
      </el-form-item>
    </el-form>
    <div>
      <blockquote class="my-blockquote"> 三级菜单缓存配置教程</blockquote>
      <pre class="my-code">
       1、将 二级菜单 的 菜单类型 设置为 目录 级别，并且原有的 组件路径 需要清空
       2、将 三级菜单 的 菜单缓存 设置为 是，最后将 组件名称 填写正确
       3、具体设置可参考 菜单管理 的 多级菜单 配置进行进行相应的修改
       </pre>
    </div>
  </div>
</template>
<script>
export default {
  name: 'Test',
  data() {
    return {
      input: ''
    }
  }
}
</script>
<style scoped>
.my-code a {
  color: #009688;
}
</style>
