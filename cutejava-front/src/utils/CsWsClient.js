import store from '@/store'
import Vue from 'vue'

/**
 * WebSocket客户端
 */
export default function() {
  const user = store.getters && store.getters.user
  const webSocketApi = store.getters && store.getters.websocketApi
  this.context = Vue.prototype
  this.wsUri = webSocketApi.replace('{sid}', user.username)
  /**
   * 初始化WebSocket连接
   * @param handleError -> function (e) { e是异常本身 }
   * @param handleMessage -> function (e) { e.data是message }
   */
  this.connect = function(handleError, handleMessage) {
    this.wsClient = new WebSocket(this.wsUri)
    // 连接发生错误
    this.wsClient.onerror = handleError
    // 收到消息
    this.wsClient.onmessage = handleMessage
    return this
  }
  /**
   * 发送客户端数据
   * @param data 客户端数据
   */
  this.sendData = function(data) {
    this.wsClient.send(JSON.stringify(data))
  }
  /**
   * 用于关闭ws连接
   */
  this.close = function() {
    console.log('关闭连接')
    if (this.wsClient.readyState === 1) {
      this.sendData([{}])
      this.wsClient.close()
    }
  }
}
