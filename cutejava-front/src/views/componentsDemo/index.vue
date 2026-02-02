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

export default {
  name: 'SystemComponents',
  components: {
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

