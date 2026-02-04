import store from '@/store'
import dayjs from 'dayjs'
import { FormatDateTime } from '@/utils/filters'

/**
 * 权限校验
 * @param value
 * @returns {boolean|*}
 */
export function CheckPermissions(value) {
  if (value && value instanceof Array && value.length > 0) {
    // 拥有的权限
    const roles = store.getters && store.getters.roles
    // 需要的权限
    const permissionRoles = value
    return roles.some(role => {
      return permissionRoles.includes(role)
    })
  } else {
    console.error(`need roles! Like v-permission="['admin','editor']"`)
    return false
  }
}

/**
 * 字符串转数字
 * @param value
 * @returns {Number|*}
 */
export function StringToNumber(value) {
  try {
    return value ? Number(value) : value
  } catch (e) {
    return value
  }
}

/**
 * 浅拷贝
 * @param source 源对象
 * @param target 目标对象
 * @returns {*}
 */
export function CopyObject(source, target) {
  return Object.assign(target, source)
}

/**
 * 深拷贝对象
 * @param obj
 */
export function DeepCopyObject(obj) {
  return JSON.parse(JSON.stringify(obj))
}

/**
 * 统计对象属性等于targetValue的数量
 * @param arr 对象数组
 * @param propKey 属性名称
 * @param targetValue 目标值
 * @returns {*|number}
 */
export function CountArraysObjectByPropKey(arr, propKey, targetValue) {
  if (!Array.isArray(arr)) {
    return 0
  }
  let cnt = 0
  for (const element of arr) {
    if (element.hasOwnProperty(propKey)) {
      // console.error('that.pipelineInstance.propKey', propKey)
      // console.error('that.pipelineInstance.element[propKey]', element[propKey])
      // console.error('that.pipelineInstance.targetValue', targetValue)
      if (element[propKey] === targetValue) {
        cnt = cnt + 1
      }
    }
  }
  return cnt
}

/**
 * 格式化日期时间字符串，不带分割符号
 * @param originVal
 * @returns {string}
 * @constructor
 */
export function FormatDateTimeShortStr(originVal) {
  if (originVal) {
    return dayjs(originVal).format('YYYYMMDDHHmmss')
  }
  return ''
}

/**
 * 格式化表格行列中的日期时间字符串
 * @param row
 * @param column
 * @param cellValue
 * @param index
 * @returns {string}
 * @constructor
 */
export function FormatRowDateTimeStr(row, column, cellValue, index) {
  return FormatDateTime(cellValue)
}

/**
 * 获取变量类型
 * @param value
 * @returns {string}
 * @constructor
 */
export function GetValueType(value) {
  return Object.prototype.toString.call(value).slice(8, -1).toLowerCase()
}

/**
 * 数组去重
 * @param arr
 * @returns {any[]}
 * @constructor
 */
export function UniqueArrays(arr) {
  return [...new Set(arr)]
}

/**
 * 获取Url中的查询参数
 * @param {string} url
 * @returns {Object}
 */
export function GetUrlQueryArgs(url) {
  url = url == null ? window.location.href : url
  const search = url.substring(url.lastIndexOf('?') + 1)
  const obj = {}
  const reg = /([^?&=]+)=([^?&=]*)/g
  search.replace(reg, (rs, $1, $2) => {
    const name = decodeURIComponent($1)
    let val = decodeURIComponent($2)
    val = String(val)
    obj[name] = val
    return rs
  })
  return obj
}

/**
 * 将Url中的查询参数转成对象
 * @param {string} url
 * @returns {Object}
 */
export function ParseUrlQueryArgsToObject(url) {
  const search = url.split('?')[1]
  if (!search) {
    return {}
  }
  return JSON.parse(
    '{"' +
    decodeURIComponent(search)
      .replace(/"/g, '\\"')
      .replace(/&/g, '","')
      .replace(/=/g, '":"')
      .replace(/\+/g, ' ') +
    '"}'
  )
}

/**
 * 合并两个属性不一致的对象
 * @param {Object} target
 * @param {(Object|Array)} source
 * @returns {Object}
 */
export function MergeObject(target, source) {
  if (typeof target !== 'object') {
    target = {}
  }
  if (Array.isArray(source)) {
    return source.slice()
  }
  Object.keys(source).forEach(property => {
    const sourceProperty = source[property]
    if (typeof sourceProperty === 'object') {
      target[property] = MergeObject(target[property], sourceProperty)
    } else {
      target[property] = sourceProperty
    }
  })
  return target
}

/**
 * 防抖动
 * @param {Function} func
 * @param {number} wait
 * @param {boolean} immediate
 * @return {*}
 */
export function DeBounce(func, wait, immediate = false) {
  let timeout, args, context, timestamp, result
  const later = function() {
    // 据上一次触发时间间隔
    const last = +new Date() - timestamp
    // 上次被包装函数被调用时间间隔 last 小于设定时间间隔 wait
    if (last < wait && last > 0) {
      timeout = setTimeout(later, wait - last)
    } else {
      timeout = null
      // 如果设定为immediate===true，因为开始边界已经调用过了此处无需调用
      if (!immediate) {
        result = func.apply(context, args)
        context = args = null
      }
    }
  }
  return function(...args) {
    context = this
    timestamp = +new Date()
    const callNow = immediate && !timeout
    // 如果延时不存在，重新设定延时
    if (!timeout) timeout = setTimeout(later, wait)
    if (callNow) {
      result = func.apply(context, args)
      context = args = null
    }

    return result
  }
}

/**
 * 格式化表格行列中的环境字符串
 * @param row
 * @param column
 * @param cellValue
 * @param index
 * @returns {string}
 * @constructor
 */
export function FormatRowEnvStr(row, column, cellValue, index) {
  if (cellValue === undefined) {
    return ''
  }
  const data = {
    daily: '日常环境',
    stage: '预发环境',
    gray: '灰度环境',
    production: '生产环境'
  }
  const title = data[cellValue]
  if (title) {
    return title
  }
  return ''
}

export function IsEmpty(obj) {
  if (obj === undefined) {
    // console.error('is undefined')
    return true
  }
  if (obj === '') {
    // console.error('is ""')
    return true
  }
  if (obj === 'undefined') {
    // console.error('is "undefined"')
    return true
  }
  // console.error('is null')
  return obj === null
}
