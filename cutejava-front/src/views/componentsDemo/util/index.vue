<template>
  <el-tabs v-model="activeName" type="border-card">
    <el-tab-pane label="信息提示 KitMessage" name="KitMessage">
      <KitMessageDemo />
    </el-tab-pane>
    <el-tab-pane label="通知提醒框 KitNotification" name="KitNotification">
      <KitNotificationDemo />
    </el-tab-pane>
    <el-tab-pane label="快捷对话框 KitDialog" name="KitDialog">
      <KitDialogDemo />
    </el-tab-pane>
    <el-tab-pane label="WebSocket客户端 KitWsClient" name="KitWsClient">
      <KitWsClientDemo />
    </el-tab-pane>
  </el-tabs>
</template>
<script>

import KitNotificationDemo from '@/views/componentsDemo/util/KitNotificationDemo.vue'
import KitMessageDemo from '@/views/componentsDemo/util/KitMessageDemo.vue'
import KitDialogDemo from '@/views/componentsDemo/util/KitDialogDemo.vue'
import KitWsClientDemo from '@/views/componentsDemo/util/KitWsClientDemo.vue'

export default {
  name: 'ComponentsDemoBusiness',
  components: {
    KitWsClientDemo,
    KitDialogDemo,
    KitMessageDemo,
    KitNotificationDemo
  },
  data() {
    return {
      activeName: 'KitMessage'
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
