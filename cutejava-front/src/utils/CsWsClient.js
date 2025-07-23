import store from '@/store'

/**
 * WebSocket客户端
 */
function CsWsClient(sid) {
  this.wsUri = store.getters && store.getters.websocketApi.replace('{sid}', sid)
  this.client = null
  /**
   * 初始化WebSocket连接
   * @param handleError -> function (e) { e是异常本身 }
   * @param handleMessage -> function (e) { e.data是message }
   */
  this.connect = function(handleError, handleMessage) {
    this.client = new WebSocket(this.wsUri)
    // 连接发生错误
    this.client.onerror = handleError
    // 收到消息
    this.client.onmessage = handleMessage
    return this
  }
  /**
   * 发送客户端数据
   * @param data 客户端数据
   */
  this.sendData = function(data) {
    this.client.send(JSON.stringify(data))
  }
  /**
   * 用于关闭ws连接
   */
  this.close = function() {
    console.info('关闭连接')
    if (this.client.readyState === 1) {
      this.sendData([{}])
      this.client.close()
    }
  }
}

export default CsWsClient
