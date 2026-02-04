<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>实时与服务器进行数据交互</li>
      <li>服务器主动推送数据给客户端</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）@Odboy（后端）</li>
    </ul>
    <h4>基础用法</h4>
    <el-card>
      <div>
        <cute-button type="primary" @click="onConnectClick">连接</cute-button>
        <el-input v-model="sendVal" style="width: 300px" placeholder="请输入要发送的内容" />
        <cute-button type="secondary" @click="onSendClick">发送</cute-button>
        <cute-button type="primary" @click="onCloseClick">关闭</cute-button>
      </div>
      <el-divider>返回内容</el-divider>
      <div style="width: 300px;min-height: 200px">{{ responseTxt }}</div>
    </el-card>
    <h4>快捷调用</h4>
    <el-table :data="apiData">
      <el-table-column prop="name" label="参数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="defaultValue" label="默认值" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
  </div>
</template>
<script>
import KitWsClient from '@/utils/KitWsClient'
import { mapGetters } from 'vuex'
import CuteButton from '@/views/components/dev/CuteButton.vue'
export default {
  name: 'KitWsClientDemo',
  components: { CuteButton },
  data() {
    return {
      client: null,
      sendVal: '',
      responseTxt: '',
      apiData: [
        { name: 'username', remark: '用户名称。对应数据库中的唯一字段：username', type: 'string', defaultValue: '-', required: '是' }
      ]
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  destroyed() {
    if (this.client) {
      this.client.close()
    }
  },
  methods: {
    onConnectClick() {
      const that = this
      // console.error('userInfo', that.user)
      that.client = new KitWsClient(that.user.username)
      that.client.connect(function(event) {
        console.info('=============== KitWsClient:event', event)
      }, function(msgEvent) {
        console.info('=============== KitWsClient:msgEvent', msgEvent)
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
<style lang="scss" scoped>
ul {
  padding-left: 20px;
}
.description > li{
  font-size: 12px;
}
</style>
