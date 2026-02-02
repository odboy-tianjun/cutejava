import Vue from 'vue'
/**
 * 消息提示
 */
export default {
  Info: function(content, duration = 3000) {
    Vue.prototype.$message({
      message: content,
      type: 'info',
      duration: duration,
      showClose: duration <= 0
    })
  },
  Success: function(content, duration = 3000) {
    Vue.prototype.$message({
      message: content,
      type: 'success',
      duration: duration,
      showClose: duration <= 0
    })
  },
  Warning: function(content, duration = 3000) {
    Vue.prototype.$message({
      message: content,
      type: 'warning',
      duration: duration,
      showClose: duration <= 0
    })
  },
  Error: function(content, duration = 3000) {
    Vue.prototype.$message({
      message: content,
      type: 'error',
      duration: duration,
      showClose: duration <= 0
    })
  }
}
