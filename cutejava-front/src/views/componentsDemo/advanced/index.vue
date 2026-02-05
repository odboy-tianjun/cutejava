<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="标准搜索区域 CuteSearchForm" name="CuteSearchForm">
      <CuteSearchFormDemo />
    </el-tab-pane>
    <el-tab-pane label="标准搜索区域(Schema版) CuteSearchFormPro" name="CuteSearchFormPro">
      <CuteSearchFormProDemo />
    </el-tab-pane>
    <el-tab-pane label="简单数据表格 CuteSimpleTable" name="CuteSimpleTable">
      <CuteSimpleTableDemo />
    </el-tab-pane>
    <el-tab-pane label="可拖拽排序的数据表格 CuteDragTable" name="CuteDragTable">
      <CuteDragTableDemo />
    </el-tab-pane>
    <el-tab-pane label="动态数据表格 CuteDynamicTable" name="CuteDynamicTable">
      <CuteDynamicTableDemo />
    </el-tab-pane>
    <el-tab-pane label="可编辑表格(Schema版) CuteEditTable" name="CuteEditTable">
      <CuteEditTableDemo />
    </el-tab-pane>
    <el-tab-pane label="表单弹窗 CuteFormDialog" name="CuteFormDialog">
      <CuteFormDialogDemo />
    </el-tab-pane>
    <el-tab-pane label="表单弹窗(Schema版) CuteFormDialogPro" name="CuteFormDialogPro">
      <CuteFormDialogProDemo />
    </el-tab-pane>
    <el-tab-pane label="表单抽屉 CuteFormDrawer" name="CuteFormDrawer">
      <CuteFormDrawerDemo />
    </el-tab-pane>
    <el-tab-pane label="表单抽屉(Schema版) CuteFormDrawerPro" name="CuteFormDrawerPro">
      <CuteFormDrawerProDemo />
    </el-tab-pane>
  </el-tabs>
</template>
<script>

import CuteDynamicTableDemo from '@/views/componentsDemo/advanced/CuteDynamicTableDemo.vue'
import CuteFormDrawerProDemo from '@/views/componentsDemo/advanced/CuteFormDrawerProDemo.vue'
import CuteSearchFormDemo from '@/views/componentsDemo/advanced/CuteSearchFormDemo.vue'
import CuteSimpleTableDemo from '@/views/componentsDemo/advanced/CuteSimpleTableDemo.vue'
import CuteSearchFormProDemo from '@/views/componentsDemo/advanced/CuteSearchFormProDemo.vue'
import CuteEditTableDemo from '@/views/componentsDemo/advanced/CuteEditTableDemo.vue'
import CuteFormDialogProDemo from '@/views/componentsDemo/advanced/CuteFormDialogProDemo.vue'
import CuteDragTableDemo from '@/views/componentsDemo/advanced/CuteDragTableDemo.vue'
import CuteFormDialogDemo from '@/views/componentsDemo/advanced/CuteFormDialogDemo.vue'
import CuteFormDrawerDemo from '@/views/componentsDemo/advanced/CuteFormDrawerDemo.vue'

export default {
  name: 'ComponentsDemoAdvanced',
  components: {
    CuteFormDrawerDemo,
    CuteFormDialogDemo,
    CuteDragTableDemo,
    CuteFormDialogProDemo,
    CuteEditTableDemo,
    CuteSearchFormProDemo,
    CuteSimpleTableDemo,
    CuteSearchFormDemo,
    CuteFormDrawerProDemo,
    CuteDynamicTableDemo
  },
  data() {
    return {
      activeName: 'CuteSearchForm'
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
