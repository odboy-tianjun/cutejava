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
import dayjs from 'dayjs' // permission control

Vue.use(checkPer)
Vue.use(permission)
Vue.directive(preventReClick)
Vue.use(dict)
Vue.use(VueClipboard)
Vue.use(Element, {
  size: Cookies.get('size') || 'small' // set element-ui default size
})

Vue.component('split-pane', SplitPane)

/**
 * 日期格式化<br/>
 * 例子: <el-tag>{{ scope.row.createTime | formatDate }}</el-tag>
 */
Vue.filter('formatDate', function(originVal) {
  if (originVal) {
    return dayjs(originVal).format('YY-MM-DD')
  } else {
    return '-'
  }
  // const dt = new Date(originVal)
  // const y = dt.getFullYear()
  // const m = (dt.getMonth() + 1 + '').padStart(2, '0')
  // const d = (dt.getDate() + '').padStart(2, '0')
  // return `${y}-${m}-${d}`
})

/**
 * 日期时间格式化<br/>
 * 例子: <el-tag>{{ scope.row.createTime | formatDateTime }}</el-tag>
 */
Vue.filter('formatDateTime', function(originVal) {
  if (originVal) {
    return dayjs(originVal).format('YY-MM-DD HH:mm:ss')
  } else {
    return '-'
  }
  // const dt = new Date(originVal)
  // const y = dt.getFullYear()
  // const m = (dt.getMonth() + 1 + '').padStart(2, '0')
  // const d = (dt.getDate() + '').padStart(2, '0')
  // const hh = (dt.getHours() + '').padStart(2, '0')
  // const mm = (dt.getMinutes() + '').padStart(2, '0')
  // const ss = (dt.getSeconds() + '').padStart(2, '0')
  // return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
})

/**
 * 金额格式化，并保留2位小数<br/>
 * 例子: <el-tag>{{ scope.row.totalAmount | formatAmount2 }}</el-tag>
 */
Vue.filter('formatAmount2', function(data) {
  if (data) {
    return data.toFixed(2)
  }
  return '-'
})

/**
 * 金额格式化，并保留1位小数<br/>
 * 例子: <el-tag>{{ scope.row.totalAmount | formatAmount1 }}</el-tag>
 */
Vue.filter('formatAmount1', function(data) {
  if (data) {
    return data.toFixed(1)
  }
  return '-'
})

/**
 * 千分位数值格式化，并保留dot位小数<br/>
 * data是value, dot是param1
 * 例子: <el-tag>{{ scope.row.totalAmount | formatNumber(2) }}</el-tag>
 */
Vue.filter('formatNumber', function(data, dot) {
  if (data) {
    const formatter = new Intl.NumberFormat('zh-CN', {
      minimumFractionDigits: dot || 0,
      maximumFractionDigits: dot || 0
    })
    return formatter.format(Number(data))
  }
  return '-'
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
