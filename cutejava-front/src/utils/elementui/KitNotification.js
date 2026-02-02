import Vue from 'vue'

/**
 * 通知
 */
export default {
  Info: function(content, duration = 3000) {
    Vue.prototype.$notify({
      message: content,
      type: 'info',
      duration: duration
    })
  }, Success: function(content, duration = 3000) {
    Vue.prototype.$notify({
      message: content,
      type: 'success',
      duration: duration
    })
  }, Warning: function(content, duration = 3000) {
    Vue.prototype.$notify({
      message: content,
      type: 'warning',
      duration: duration
    })
  }, Error: function(content, duration = 3000) {
    Vue.prototype.$notify({
      message: content,
      type: 'error',
      duration: duration
    })
  }
}
