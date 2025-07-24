import Vue from 'vue'

/**
 * 通知
 */
export default {
  Info: function(content) {
    Vue.prototype.$notify({ message: content, type: 'info' })
  }, Success: function(content) {
    Vue.prototype.$notify({ message: content, type: 'success' })
  }, SuccessDuration: function(content, durationTime) {
    Vue.prototype.$notify({ message: content, type: 'success', duration: durationTime })
  }, Warning: function(content) {
    Vue.prototype.$notify({ message: content, type: 'warning' })
  }, Error: function(content) {
    Vue.prototype.$notify({ message: content, type: 'error' })
  }, ErrorDuration: function(content, durationTime) {
    Vue.prototype.$notify({ message: content, type: 'error', duration: durationTime })
  }
}
