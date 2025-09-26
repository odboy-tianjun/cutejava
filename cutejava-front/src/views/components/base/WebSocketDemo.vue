<template>
  <el-card>
    <div>
      <el-input v-model="sendVal" style="width: 300px" />
      <el-button @click="onConnectClick">连接</el-button>
      <el-button @click="onSendClick">发送</el-button>
      <el-button @click="onCloseClick">关闭</el-button>
    </div>
    <el-divider>返回内容</el-divider>
    <div style="width: 300px;min-height: 200px">{{ responseTxt }}</div>
  </el-card>
</template>
<script>
import CsWsClient from '@/utils/CsWsClient'

export default {
  data() {
    return {
      client: null,
      sendVal: '',
      responseTxt: ''
    }
  },
  // mounted() {},
  destroyed() {
    if (this.client) {
      this.client.close()
    }
  },
  methods: {
    onConnectClick() {
      // 获取mapGetters中的值
      // console.error('userInfo', this.user)
      const that = this
      that.client = new CsWsClient(that)
      that.client.connect(function(event) {
        console.info('=============== MyWebSocket:event', event)
      }, function(msgEvent) {
        console.info('=============== MyWebSocket:msgEvent', msgEvent)
        that.responseTxt = that.responseTxt + msgEvent.data + ''
      })
    },
    onSendClick() {
      if (this.client) {
        this.client.sendData({
          msgType: 'sc',
          userIds: ['123456'],
          content: this.sendVal
        })
        this.client.sendData({
          msgType: 'gc',
          content: this.sendVal
        })
      }
    },
    onCloseClick() {
      if (this.client) {
        this.client.close()
      }
    }
  }
}
</script>
<style scoped>
#btn {
  display: inline-block;
  padding: 3px 12px;
  border-radius: 6px;
  background-color: #eee;
  color: #333;
  cursor: pointer;
}

#btn:active {
  background-color: #ddd;
  color: #666;
}
</style>
