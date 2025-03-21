import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/localStorage/save',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/localStorage/remove',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/localStorage/modify',
    method: 'post',
    data: data
  })
}

export default { add, edit, del }
