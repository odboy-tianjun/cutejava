<template>
  <cute-page>
    <el-tabs v-model="tabKey">
      <el-tab-pane label="基础组件" name="basic">
        <el-collapse accordion>
          <el-collapse-item title="按钮 CuteButton" name="CuteButton">
            <CuteButtonDemo />
          </el-collapse-item>
          <el-collapse-item title="文字链接 CuteLink" name="CuteLink">
            <CuteLinkDemo />
          </el-collapse-item>
          <el-collapse-item title="页面 CutePage" name="CutePage">
            <CutePageDemo />
          </el-collapse-item>
          <el-collapse-item title="空状态 CuteNoData" name="CuteNoData">
            <CuteNoDataDemo />
          </el-collapse-item>
          <el-collapse-item title="段落标题 CuteSectionTitle" name="CuteSectionTitle">
            <CuteSectionTitleDemo />
          </el-collapse-item>
          <el-collapse-item title="横向滚动区域 CuteHorizontalScrollBar" name="CuteHorizontalScrollBar">
            <CuteSectionTitleDemo />
          </el-collapse-item>
          <el-collapse-item title="纵向滚动区域 CuteVerticalScrollBar" name="CuteVerticalScrollBar">
            <CuteSectionTitleDemo />
          </el-collapse-item>
          <el-collapse-item title="一键复制 CuteOneKeyCopy" name="CuteOneKeyCopy">
            <CuteOneKeyCopyDemo />
          </el-collapse-item>
          <el-collapse-item title="Markdown编辑框 CuteMarkdown" name="CuteMarkdown">
            <CuteMarkdownDemo />
          </el-collapse-item>
          <el-collapse-item title="代码编辑框 CuteCodeEditor" name="CuteCodeEditor">
            <CuteCodeEditorDemo />
          </el-collapse-item>
          <el-collapse-item title="富文本框 CuteRichTextEditor" name="CuteRichTextEditor">
            <CuteRichTextEditorDemo />
          </el-collapse-item>
        </el-collapse>
      </el-tab-pane>
      <el-tab-pane label="快捷组件" name="kit">
        <el-collapse accordion>
          <el-collapse-item title="信息提示 KitMessage" name="KitMessage">
            <KitMessageDemo />
          </el-collapse-item>
          <el-collapse-item title="通知提醒框 KitNotification" name="KitNotification">
            <KitNotificationDemo />
          </el-collapse-item>
          <el-collapse-item title="快捷对话框 KitDialog" name="KitDialog">
            <KitDialogDemo />
          </el-collapse-item>
          <el-collapse-item title="WebSocket客户端 KitWsClient" name="KitWsClient">
            <KitWsClientDemo />
          </el-collapse-item>
        </el-collapse>
      </el-tab-pane>
      <el-tab-pane label="业务组件" name="business" />
      <el-tab-pane label="高级组件" name="advanced" />
    </el-tabs>
  </cute-page>
</template>

<script>

import CutePage from '@/views/components/dev/CutePage.vue'
import CuteButtonDemo from '@/views/componentsDemo/dev/CuteButtonDemo.vue'
import KitMessageDemo from '@/views/componentsDemo/dev/KitMessageDemo.vue'
import KitNotificationDemo from '@/views/componentsDemo/dev/KitNotificationDemo.vue'
import KitDialogDemo from '@/views/componentsDemo/dev/KitDialogDemo.vue'
import KitWsClientDemo from '@/views/componentsDemo/dev/KitWsClientDemo.vue'
import CuteLinkDemo from '@/views/componentsDemo/dev/CuteLinkDemo.vue'
import CutePageDemo from '@/views/componentsDemo/dev/CutePageDemo.vue'
import CuteNoDataDemo from '@/views/componentsDemo/dev/CuteNoDataDemo.vue'
import CuteSectionTitleDemo from '@/views/componentsDemo/dev/CuteSectionTitleDemo.vue'
import CuteOneKeyCopyDemo from '@/views/componentsDemo/dev/CuteOneKeyCopyDemo.vue'
import CuteMarkdownDemo from '@/views/componentsDemo/dev/CuteMarkdownDemo.vue'
import CuteCodeEditorDemo from '@/views/componentsDemo/dev/CuteCodeEditorDemo.vue'
import CuteRichTextEditorDemo from '@/views/componentsDemo/dev/CuteRichTextEditorDemo.vue'

export default {
  name: 'SystemComponents',
  components: {
    CuteRichTextEditorDemo,
    CuteCodeEditorDemo,
    CuteMarkdownDemo,
    CuteOneKeyCopyDemo,
    CuteSectionTitleDemo,
    CuteNoDataDemo,
    CutePageDemo,
    CuteLinkDemo, KitWsClientDemo, KitDialogDemo, KitNotificationDemo, KitMessageDemo, CuteButtonDemo, CutePage },
  props: {
    value: {
      type: Object,
      required: true,
      default: null
    }
  },
  data() {
    return {
      tabKey: 'basic'
    }
  }
}
</script>

