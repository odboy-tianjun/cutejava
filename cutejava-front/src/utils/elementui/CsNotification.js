import Vue from 'vue'

/**
 * 通知
 */
export default {
  Info(content) {
    Vue.prototype.$notify({ message: content, type: 'info' })
  }, Success(content) {
    Vue.prototype.$notify({ message: content, type: 'success' })
  }, SuccessDuration(content, durationTime) {
    Vue.prototype.$notify({ message: content, type: 'success', duration: durationTime })
  }, Warning(content) {
    Vue.prototype.$notify({ message: content, type: 'warning' })
  }, Error(content) {
    Vue.prototype.$notify({ message: content, type: 'error' })
  }, ErrorDuration(content, durationTime) {
    Vue.prototype.$notify({ message: content, type: 'error', duration: durationTime })
  }
}
