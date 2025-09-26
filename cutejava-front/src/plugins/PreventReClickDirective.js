import Vue from 'vue'

/**
 * 防止重复提交指令，需要在main.js中全局注册 Vue.directive('preventReClick', preventReClick)
 * 使用默认的 3000 毫秒防重复点击时间
 * 例子: <el-button v-prevent-re-click >提交</el-button>
 * 自定义防重复点击时间为 5000 毫秒
 * 例子: <el-button v-prevent-re-click >提交</el-button>
 */
export const preventReClick = Vue.directive('preventReClick', {
  inserted(el, binding) {
    el.addEventListener('click', () => {
      if (!el.disabled) {
        el.disabled = true
        setTimeout(() => {
          el.disabled = false
        }, binding.value || 3000)
      }
    })
  }
})
