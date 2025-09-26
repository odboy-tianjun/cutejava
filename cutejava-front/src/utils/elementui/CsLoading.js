import Vue from 'vue'

/**
 * 全屏锁定加载动画
 */
// export default function CsLoading() {
//   this.instance = null
//   this.show = function() {
//     this.instance = Vue.prototype.$loading({
//       lock: true,
//       text: 'Loading',
//       spinner: 'el-icon-loading',
//       background: 'rgba(0, 0, 0, 0.7)'
//     })
//   }
//   this.close = function() {
//     if (this.instance) {
//       this.instance.close()
//     }
//   }
// }

export default class CsLoading {
  constructor() {
    this.instance = null
  }

  show() {
    this.instance = Vue.prototype.$loading({
      lock: true,
      text: 'Loading',
      spinner: 'el-icon-loading',
      background: 'rgba(0, 0, 0, 0.7)'
    })
  }

  close() {
    if (this.instance) {
      this.instance.close()
    }
  }
}

