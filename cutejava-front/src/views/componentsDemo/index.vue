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
          <el-collapse-item title="多行输入文本 CuteTextAreaInput" name="CuteTextAreaInput">
            <CuteTextAreaInputDemo />
          </el-collapse-item>
          <el-collapse-item title="段落标题 CuteSectionTitle" name="CuteSectionTitle">
            <CuteSectionTitleDemo />
          </el-collapse-item>
          <el-collapse-item title="横向滚动区域 CuteHorizontalScrollBar" name="CuteHorizontalScrollBar">
            <CuteHorizontalScrollBarDemo />
          </el-collapse-item>
          <el-collapse-item title="纵向滚动区域 CuteVerticalScrollBar" name="CuteVerticalScrollBar">
            <CuteVerticalScrollBarDemo />
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
          <el-collapse-item title="截图工具 CuteScreenShot" name="CuteScreenShot">
            <CuteScreenShotDemo />
          </el-collapse-item>
          <el-collapse-item title="预览抽屉 CutePreviewDrawer" name="CutePreviewDrawer">
            <CutePreviewDrawerDemo />
          </el-collapse-item>
          <el-collapse-item title="穿梭框 CuteTransfer" name="CuteTransfer">
            <CuteTransferDemo />
          </el-collapse-item>
        </el-collapse>
      </el-tab-pane>
      <el-tab-pane label="快捷组件" name="util">
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
      <el-tab-pane label="业务组件" name="business">
        <el-collapse accordion>
          <el-collapse-item title="用户单选框 CuteSingleUserSelect" name="CuteSingleUserSelect">
            <CuteSingleUserSelectDemo />
          </el-collapse-item>
          <el-collapse-item title="用户多选框 CuteUserSelect" name="CuteUserSelect">
            <CuteUserSelectDemo />
          </el-collapse-item>
          <el-collapse-item title="部门树 CuteDeptTree" name="CuteDeptTree">
            <CuteDeptTreeDemo />
          </el-collapse-item>
          <el-collapse-item title="产品线选择框 CuteProductLineSelect" name="CuteProductLineSelect">
            <CuteProductLineSelectDemo />
          </el-collapse-item>
          <el-collapse-item title="产品线选择框Pro CuteProductLineSelectPro" name="CuteProductLineSelectPro">
            <CuteProductLineSelectProDemo />
          </el-collapse-item>
          <el-collapse-item title="文件上传 CuteFileUpload" name="CuteFileUpload">
            <CuteFileUploadDemo />
          </el-collapse-item>
          <el-collapse-item title="文件拖放上传 CuteFileDragUpload" name="CuteFileDragUpload">
            <CuteFileDragUploadDemo />
          </el-collapse-item>
        </el-collapse>
      </el-tab-pane>
      <el-tab-pane label="高级组件" name="advanced">
        <el-collapse accordion>
          <el-collapse-item title="标准搜索区域 CuteSearchForm" name="CuteSearchForm">
            <CuteSearchFormDemo />
          </el-collapse-item>
          <el-collapse-item title="标准搜索区域(Schema版) CuteSearchFormPro" name="CuteSearchFormPro">
            <CuteSearchFormProDemo />
          </el-collapse-item>
          <el-collapse-item title="简单数据表格 CuteSimpleTable" name="CuteSimpleTable">
            <CuteSimpleTableDemo />
          </el-collapse-item>
          <el-collapse-item title="可拖拽排序的数据表格 CuteDragTable" name="CuteDragTable">
            <CuteDragTableDemo />
          </el-collapse-item>
          <el-collapse-item title="可编辑表格(Schema版) CuteEditTable" name="CuteEditTable">
            <CuteEditTableDemo />
          </el-collapse-item>
          <el-collapse-item title="表单弹窗 CuteFormDialog" name="CuteFormDialog">
            <CuteFormDialogDemo />
          </el-collapse-item>
          <el-collapse-item title="表单弹窗(Schema版) CuteFormDialogPro" name="CuteFormDialogPro">
            <CuteFormDialogProDemo />
          </el-collapse-item>
          <el-collapse-item title="表单抽屉 CuteFormDrawer" name="CuteFormDrawer">
            <CuteFormDrawerDemo />
          </el-collapse-item>
          <el-collapse-item title="表单抽屉(Schema版) CuteFormDrawerPro" name="CuteFormDrawerPro">
            <CuteFormDrawerProDemo />
          </el-collapse-item>
        </el-collapse>
      </el-tab-pane>
    </el-tabs>
  </cute-page>
</template>

<script>

import CutePage from '@/views/components/dev/CutePage.vue'
import CuteButtonDemo from '@/views/componentsDemo/dev/CuteButtonDemo.vue'
import KitMessageDemo from '@/views/componentsDemo/util/KitMessageDemo.vue'
import KitNotificationDemo from '@/views/componentsDemo/util/KitNotificationDemo.vue'
import KitDialogDemo from '@/views/componentsDemo/util/KitDialogDemo.vue'
import KitWsClientDemo from '@/views/componentsDemo/util/KitWsClientDemo.vue'
import CuteLinkDemo from '@/views/componentsDemo/dev/CuteLinkDemo.vue'
import CutePageDemo from '@/views/componentsDemo/dev/CutePageDemo.vue'
import CuteNoDataDemo from '@/views/componentsDemo/dev/CuteNoDataDemo.vue'
import CuteSectionTitleDemo from '@/views/componentsDemo/dev/CuteSectionTitleDemo.vue'
import CuteOneKeyCopyDemo from '@/views/componentsDemo/dev/CuteOneKeyCopyDemo.vue'
import CuteMarkdownDemo from '@/views/componentsDemo/dev/CuteMarkdownDemo.vue'
import CuteCodeEditorDemo from '@/views/componentsDemo/dev/CuteCodeEditorDemo.vue'
import CuteRichTextEditorDemo from '@/views/componentsDemo/dev/CuteRichTextEditorDemo.vue'
import CuteHorizontalScrollBarDemo from '@/views/componentsDemo/dev/CuteHorizontalScrollBarDemo.vue'
import CuteTextAreaInputDemo from '@/views/componentsDemo/dev/CuteTextAreaInputDemo.vue'
import CuteUserSelectDemo from '@/views/componentsDemo/business/CuteUserSelectDemo.vue'
import CuteSingleUserSelectDemo from '@/views/componentsDemo/business/CuteSingleUserSelectDemo.vue'
import CuteProductLineSelectDemo from '@/views/componentsDemo/business/CuteProductLineSelectDemo.vue'
import CuteProductLineSelectProDemo from '@/views/componentsDemo/business/CuteProductLineSelectProDemo.vue'
import CuteDeptTreeDemo from '@/views/componentsDemo/business/CuteDeptTreeDemo.vue'
import CuteScreenShotDemo from '@/views/componentsDemo/dev/CuteScreenShotDemo.vue'
import CutePreviewDrawerDemo from '@/views/componentsDemo/dev/CutePreviewDrawerDemo.vue'
import CuteTransferDemo from '@/views/componentsDemo/dev/CuteTransferDemo.vue'
import CuteFileUploadDemo from '@/views/componentsDemo/business/CuteFileUploadDemo.vue'
import CuteFileDragUploadDemo from '@/views/componentsDemo/business/CuteFileDragUploadDemo.vue'
import CuteDragTableDemo from '@/views/componentsDemo/advanced/CuteDragTableDemo.vue'
import CuteEditTableDemo from '@/views/componentsDemo/advanced/CuteEditTableDemo.vue'
import CuteFormDialogDemo from '@/views/componentsDemo/advanced/CuteFormDialogDemo.vue'
import CuteFormDialogProDemo from '@/views/componentsDemo/advanced/CuteFormDialogProDemo.vue'
import CuteFormDrawerDemo from '@/views/componentsDemo/advanced/CuteFormDrawerDemo.vue'
import CuteFormDrawerProDemo from '@/views/componentsDemo/advanced/CuteFormDrawerProDemo.vue'
import CuteSearchFormDemo from '@/views/componentsDemo/advanced/CuteSearchFormDemo.vue'
import CuteSearchFormProDemo from '@/views/componentsDemo/advanced/CuteSearchFormProDemo.vue'
import CuteSimpleTableDemo from '@/views/componentsDemo/advanced/CuteSimpleTableDemo.vue'
import CuteVerticalScrollBarDemo from '@/views/componentsDemo/dev/CuteVerticalScrollBarDemo.vue'

export default {
  name: 'SystemComponents',
  components: {
    CuteVerticalScrollBarDemo,
    CuteSimpleTableDemo,
    CuteSearchFormProDemo,
    CuteSearchFormDemo,
    CuteFormDrawerProDemo,
    CuteFormDrawerDemo,
    CuteFormDialogProDemo,
    CuteFormDialogDemo,
    CuteEditTableDemo,
    CuteDragTableDemo,
    CuteFileDragUploadDemo,
    CuteFileUploadDemo,
    CuteTransferDemo,
    CutePreviewDrawerDemo,
    CuteScreenShotDemo,
    CuteDeptTreeDemo,
    CuteProductLineSelectProDemo,
    CuteProductLineSelectDemo,
    CuteSingleUserSelectDemo,
    CuteUserSelectDemo,
    CuteTextAreaInputDemo,
    CuteHorizontalScrollBarDemo,
    CuteRichTextEditorDemo,
    CuteCodeEditorDemo,
    CuteMarkdownDemo,
    CuteOneKeyCopyDemo,
    CuteSectionTitleDemo,
    CuteNoDataDemo,
    CutePageDemo,
    CuteLinkDemo,
    KitWsClientDemo,
    KitDialogDemo,
    KitNotificationDemo,
    KitMessageDemo,
    CuteButtonDemo,
    CutePage
  },
  data() {
    return {
      tabKey: 'basic'
    }
  }
}
</script>

