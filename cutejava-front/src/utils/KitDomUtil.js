import { FormatDateTimeShortStr } from '@/utils/KitUtil'
import { getToken } from '@/utils/auth'
import axios from 'axios'

/**
 * Check if an element has a class
 * @param {HTMLElement} elm
 * @param {string} cls
 * @returns {boolean}
 */
export function HasClass(ele, cls) {
  return !!ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'))
}

/**
 * Add class to element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function AddClass(ele, cls) {
  if (!HasClass(ele, cls)) ele.className += ' ' + cls
}
/**
 * Remove class from element
 * @param {HTMLElement} elm
 * @param {string} cls
 */
export function RemoveClass(ele, cls) {
  if (HasClass(ele, cls)) {
    const reg = new RegExp('(\\s|^)' + cls + '(\\s|$)')
    ele.className = ele.className.replace(reg, ' ')
  }
}

/**
 * @param {HTMLElement} element
 * @param {string} className
 */
export function ToggleClass(element, className) {
  if (!element || !className) {
    return
  }
  let classString = element.className
  const nameIndex = classString.indexOf(className)
  if (nameIndex === -1) {
    classString += '' + className
  } else {
    classString =
      classString.substr(0, nameIndex) +
      classString.substr(nameIndex + className.length)
  }
  element.className = classString
}

/**
 * 下载文件
 * @param obj 后端返回的数据
 * @param name 文件名
 * @param suffix 文件后缀
 * @constructor
 */
export function DownloadFile(obj, name, suffix) {
  const url = window.URL.createObjectURL(new Blob([obj]))
  const link = document.createElement('a')
  link.style.display = 'none'
  link.href = url
  const fileName = FormatDateTimeShortStr(new Date()) + '-' + name + '.' + suffix
  link.setAttribute('download', fileName)
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
}

/**
 * 上传文件
 * @param api 上传地址
 * @param file 上传的文件
 * @returns {Promise<axios.AxiosResponse<any>>}
 */
export function UploadFile(api, file) {
  const data = new FormData()
  data.append('file', file)
  const config = {
    headers: { 'Authorization': getToken() }
  }
  return axios.post(api, data, config)
}

