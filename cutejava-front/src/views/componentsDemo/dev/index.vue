<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="按钮 CuteButton" name="CuteButton">
      <CuteButtonDemo />
    </el-tab-pane>
    <el-tab-pane label="文字链接 CuteLink" name="CuteLink">
      <CuteLinkDemo />
    </el-tab-pane>
    <el-tab-pane label="页面 CutePage" name="CutePage">
      <CutePageDemo />
    </el-tab-pane>
    <el-tab-pane label="空状态 CuteNoData" name="CuteNoData">
      <CuteNoDataDemo />
    </el-tab-pane>
    <el-tab-pane label="多行输入文本 CuteTextAreaInput" name="CuteTextAreaInput">
      <CuteTextAreaInputDemo />
    </el-tab-pane>
    <el-tab-pane label="段落标题 CuteSectionTitle" name="CuteSectionTitle">
      <CuteSectionTitleDemo />
    </el-tab-pane>
    <el-tab-pane label="横向滚动区域 CuteHorizontalScrollBar" name="CuteHorizontalScrollBar">
      <CuteHorizontalScrollBarDemo />
    </el-tab-pane>
    <el-tab-pane label="纵向滚动区域 CuteVerticalScrollBar" name="CuteVerticalScrollBar">
      <CuteVerticalScrollBarDemo />
    </el-tab-pane>
    <el-tab-pane label="一键复制 CuteOneKeyCopy" name="CuteOneKeyCopy">
      <CuteOneKeyCopyDemo />
    </el-tab-pane>
    <el-tab-pane label="Markdown编辑框 CuteMarkdown" name="CuteMarkdown">
      <CuteMarkdownDemo />
    </el-tab-pane>
    <el-tab-pane label="代码编辑框 CuteCodeEditor" name="CuteCodeEditor">
      <CuteCodeEditorDemo />
    </el-tab-pane>
    <el-tab-pane label="富文本框 CuteRichTextEditor" name="CuteRichTextEditor">
      <CuteRichTextEditorDemo />
    </el-tab-pane>
    <el-tab-pane label="截图工具 CuteScreenShot" name="CuteScreenShot">
      <CuteScreenShotDemo />
    </el-tab-pane>
    <el-tab-pane label="预览抽屉 CutePreviewDrawer" name="CutePreviewDrawer">
      <CutePreviewDrawerDemo />
    </el-tab-pane>
    <el-tab-pane label="穿梭框 CuteTransfer" name="CuteTransfer">
      <CuteTransferDemo />
    </el-tab-pane>
  </el-tabs>
</template>
<script>

import CuteButtonDemo from '@/views/componentsDemo/dev/CuteButtonDemo.vue'
import CuteOneKeyCopyDemo from '@/views/componentsDemo/dev/CuteOneKeyCopyDemo.vue'
import CuteScreenShotDemo from '@/views/componentsDemo/dev/CuteScreenShotDemo.vue'
import CuteRichTextEditorDemo from '@/views/componentsDemo/dev/CuteRichTextEditorDemo.vue'
import CuteTransferDemo from '@/views/componentsDemo/dev/CuteTransferDemo.vue'
import CuteMarkdownDemo from '@/views/componentsDemo/dev/CuteMarkdownDemo.vue'
import CutePageDemo from '@/views/componentsDemo/dev/CutePageDemo.vue'
import CuteNoDataDemo from '@/views/componentsDemo/dev/CuteNoDataDemo.vue'
import CutePreviewDrawerDemo from '@/views/componentsDemo/dev/CutePreviewDrawerDemo.vue'
import CuteTextAreaInputDemo from '@/views/componentsDemo/dev/CuteTextAreaInputDemo.vue'
import CuteSectionTitleDemo from '@/views/componentsDemo/dev/CuteSectionTitleDemo.vue'
import CuteVerticalScrollBarDemo from '@/views/componentsDemo/dev/CuteVerticalScrollBarDemo.vue'
import CuteHorizontalScrollBarDemo from '@/views/componentsDemo/dev/CuteHorizontalScrollBarDemo.vue'
import CuteLinkDemo from '@/views/componentsDemo/dev/CuteLinkDemo.vue'
import CuteCodeEditorDemo from '@/views/componentsDemo/dev/CuteCodeEditorDemo.vue'

export default {
  name: 'ComponentsDemoDev',
  components: {
    CuteCodeEditorDemo,
    CuteLinkDemo,
    CuteHorizontalScrollBarDemo,
    CuteVerticalScrollBarDemo,
    CuteSectionTitleDemo,
    CuteTextAreaInputDemo,
    CutePreviewDrawerDemo,
    CuteNoDataDemo,
    CutePageDemo,
    CuteMarkdownDemo,
    CuteTransferDemo,
    CuteRichTextEditorDemo,
    CuteScreenShotDemo,
    CuteOneKeyCopyDemo,
    CuteButtonDemo
  },
  data() {
    return {
      activeName: 'CuteButton'
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
