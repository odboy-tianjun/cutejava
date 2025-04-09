import request from '@/utils/request'

export function queryQiNiuConfig() {
  return request({
    url: 'api/qiNiuContent/queryQiNiuConfig',
    method: 'post'
  })
}

export function update(data) {
  return request({
    url: 'api/qiNiuContent/updateQiNiuConfig',
    method: 'post',
    data
  })
}

export function download(id) {
  return request({
    url: 'api/qiNiuContent/download/' + id,
    method: 'get'
  })
}

export function sync() {
  return request({
    url: 'api/qiNiuContent/synchronize',
    method: 'post'
  })
}

export function del(ids) {
  return request({
    url: 'api/qiNiuContent',
    method: 'delete',
    data: ids
  })
}

export default { del, download, sync }
