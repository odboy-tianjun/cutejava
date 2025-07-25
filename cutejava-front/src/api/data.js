import request from '@/utils/request'
import qs from 'qs'

// export function initData(url, params) {
//   return request({
//     url: url + '?' + qs.stringify(params, { indices: false }),
//     method: 'get'
//   })
// }

export function initData(url, params) {
  return request({
    url: url,
    method: 'post',
    data: params
  })
}

export function initDataByPost(url, params) {
  return request({
    url: url,
    method: 'post',
    data: params
  })
}

export function download(url, params) {
  return request({
    url: url + '?' + qs.stringify(params, { indices: false }),
    method: 'get',
    responseType: 'blob'
  })
}
