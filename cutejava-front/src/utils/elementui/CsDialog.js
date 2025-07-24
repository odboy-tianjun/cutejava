import Vue from 'vue'
/**
 * 消息提示
 */
export default {
  /**
   * 确认消息对话框<br/>
   * 提示用户确认其已经触发的动作, 并询问是否进行此操作时会用到此对话框
   * @param questionText 提示语
   * @param msgType 消息类型(w、i、e、s)
   * @param confirmCallBack 确认回调
   * @param cancelCallBack 取消回调
   */
  FullConfirm: function(questionText, msgType, confirmCallBack, cancelCallBack) {
    let currentType = 'info'
    if (msgType === 's') {
      currentType = 'success'
    } else if (msgType === 'e') {
      currentType = 'error'
    } else if (msgType === 'i') {
      currentType = 'info'
    } else if (msgType === 'w') {
      currentType = 'warning'
    }
    Vue.prototype.$confirm(questionText, '提示', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: currentType
    }).then(() => {
      if (confirmCallBack != null) {
        confirmCallBack()
      }
    }).catch(() => {
      if (cancelCallBack != null) {
        cancelCallBack()
      }
    })
  }, /**
   * 数据删除确认对话框
   * @param confirmCallBack 确认回调
   * @param cancelCallBack 取消回调
   */
  DeleteConfirm: function(confirmCallBack, cancelCallBack) {
    this.FullConfirm('确定删除本条数据吗？', 'w', confirmCallBack, cancelCallBack)
  }, /**
   * 数据删除确认对话框(自定义提示内容)
   * @param message 提示内容
   * @param confirmCallBack 确认回调
   * @param cancelCallBack 取消回调
   */
  DeleteMessageConfirm: function(message, confirmCallBack, cancelCallBack) {
    this.FullConfirm(message, 'w', confirmCallBack, cancelCallBack)
  }, /**
   * 数据输入对话框
   * @param title 标题提示
   * @param patternText 正则表达式
   * @param errorMsg 校验失败消息内容
   * @param confirmCallBack 校验通过回调
   * @param cancelCallBack 取消回调
   */
  TextInputConfirm: function(title, patternText, errorMsg, confirmCallBack, cancelCallBack) {
    Vue.prototype.$prompt('', title, {
      confirmButtonText: '确定', cancelButtonText: '取消', inputPattern: patternText, inputErrorMessage: errorMsg
    }).then(({ value }) => {
      if (confirmCallBack != null) {
        confirmCallBack(value)
      }
    }).catch(() => {
      if (cancelCallBack != null) {
        cancelCallBack()
      }
    })
  }, /**
   * 确认内容消息框
   * @param title 标题
   * @param content 内容
   */
  ShowConfirm: function(title, content) {
    Vue.prototype.$alert(content, title, {
      confirmButtonText: '关闭'
    })
  }
}

