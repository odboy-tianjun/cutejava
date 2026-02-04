import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css'

import Element from 'element-ui'

// 数据字典
import dict from './components/Dict'

// 防止二次提交
import { preventReClick } from '@/plugins/PreventReClickDirective'

// 一键复制插件
import VueClipboard from 'vue-clipboard2'

// 窗口分割组件
import SplitPane from 'vue-splitpane'

// 权限指令
import checkPer from '@/plugins/CheckPermPlugin'
import permission from './components/Permission'
import './assets/styles/element-variables.scss'

// global css
import './assets/styles/index.scss'

import App from './App'
import store from './store'
import router from './router/routers'

import './assets/icons' // icon
import './router/index'

// 过滤器
import * as filters from './utils/filters'

Vue.use(checkPer)
Vue.use(permission)
Vue.directive(preventReClick)
Vue.use(dict)
Vue.use(VueClipboard)
Vue.use(Element, {
  size: Cookies.get('size') || 'small' // set element-ui default size
})

Vue.component('split-pane', SplitPane)

// 注册全局过滤器
// 使用方法：{{ value | filter-key }}
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

Vue.config.errorHandler = function(err, vm, info) {
  // err: 错误对象
  // vm: 发生错误的 Vue 实例
  // info: Vue 特定的错误信息，如生命周期钩子、事件等

  console.error('Global Vue error:', err)
  console.log('Component:', vm)
  console.log('Error info:', info)

  // 可以在这里发送错误日志到服务器
  // logErrorToServer(err, vm, info);
}

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
