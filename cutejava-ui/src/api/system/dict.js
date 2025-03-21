import request from '@/utils/request'

export function getDicts() {
  return request({
    url: 'api/dict/all',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'api/dict/save',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/dict/remove',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/dict/modify',
    method: 'post',
    data: data
  })
}

export default { add, edit, del }
