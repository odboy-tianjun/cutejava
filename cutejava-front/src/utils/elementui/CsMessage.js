import Vue from 'vue'
/**
 * 消息提示
 */
export default {
  Info(content) {
    Vue.prototype.$message({
      message: content, type: 'info'
    })
  },
  Success(content) {
    Vue.prototype.$message({
      message: content, type: 'success'
    })
  },
  Warning(content) {
    Vue.prototype.$message({
      message: content, type: 'warning'
    })
  },
  Error(content) {
    Vue.prototype.$message({
      message: content, type: 'error'
    })
  }
}
