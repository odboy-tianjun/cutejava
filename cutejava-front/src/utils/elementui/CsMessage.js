import Vue from 'vue'
/**
 * 消息提示
 */
export default {
  Info: function(content) {
    Vue.prototype.$message({
      message: content, type: 'info'
    })
  },
  Success: function(content) {
    Vue.prototype.$message({
      message: content, type: 'success'
    })
  },
  Warning: function(content) {
    Vue.prototype.$message({
      message: content, type: 'warning'
    })
  },
  Error: function(content) {
    Vue.prototype.$message({
      message: content, type: 'error'
    })
  }
}
