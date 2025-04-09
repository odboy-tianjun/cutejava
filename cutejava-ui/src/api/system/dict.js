import request from '@/utils/request'

export function getDicts() {
  return request({
    url: 'api/dict/queryAllDict',
    method: 'post'
  })
}

export function add(data) {
  return request({
    url: 'api/dict/createDict',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/dict/deleteDict',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/dict/updateDict',
    method: 'post',
    data
  })
}

export default { add, edit, del }
