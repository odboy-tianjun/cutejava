<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="用户单选框 CuteSingleUserSelect" name="CuteSingleUserSelect">
      <CuteSingleUserSelectDemo />
    </el-tab-pane>
    <el-tab-pane label="用户多选框 CuteUserSelect" name="CuteUserSelect">
      <CuteUserSelectDemo />
    </el-tab-pane>
    <el-tab-pane label="部门树 CuteDeptTree" name="CuteDeptTree">
      <CuteDeptTreeDemo />
    </el-tab-pane>
    <el-tab-pane label="产品线选择框 CuteProductLineSelect" name="CuteProductLineSelect">
      <CuteProductLineSelectDemo />
    </el-tab-pane>
    <el-tab-pane label="产品线选择框Pro CuteProductLineSelectPro" name="CuteProductLineSelectPro">
      <CuteProductLineSelectProDemo />
    </el-tab-pane>
    <el-tab-pane label="文件上传 CuteFileUpload" name="CuteFileUpload">
      <CuteFileUploadDemo />
    </el-tab-pane>
    <el-tab-pane label="文件拖放上传 CuteFileDragUpload" name="CuteFileDragUpload">
      <CuteFileDragUploadDemo />
    </el-tab-pane>
  </el-tabs>
</template>
<script>

import CuteSingleUserSelectDemo from '@/views/componentsDemo/business/CuteSingleUserSelectDemo.vue'
import CuteUserSelectDemo from '@/views/componentsDemo/business/CuteUserSelectDemo.vue'
import CuteDeptTreeDemo from '@/views/componentsDemo/business/CuteDeptTreeDemo.vue'
import CuteProductLineSelectDemo from '@/views/componentsDemo/business/CuteProductLineSelectDemo.vue'
import CuteProductLineSelectProDemo from '@/views/componentsDemo/business/CuteProductLineSelectProDemo.vue'
import CuteFileUploadDemo from '@/views/componentsDemo/business/CuteFileUploadDemo.vue'
import CuteFileDragUploadDemo from '@/views/componentsDemo/business/CuteFileDragUploadDemo.vue'

export default {
  name: 'ComponentsDemoBusiness',
  components: {
    CuteFileDragUploadDemo,
    CuteFileUploadDemo,
    CuteProductLineSelectProDemo,
    CuteProductLineSelectDemo,
    CuteDeptTreeDemo,
    CuteUserSelectDemo,
    CuteSingleUserSelectDemo
  },
  data() {
    return {
      activeName: 'CuteSingleUserSelect'
    }
  }
}
</script>
<style lang="scss" scoped>
ul {
  padding-left: 20px;
}
.description > li{
  font-size: 12px;
}
</style>
