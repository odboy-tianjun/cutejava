import request from '@/utils/request'

export function queryQiniuConfig() {
  return request({
    url: 'api/qiNiuContent/queryQiniuConfig',
    method: 'post'
  })
}

export function modifyQiniuConfig(data) {
  return request({
    url: 'api/qiNiuContent/modifyQiniuConfig',
    method: 'post',
    data: data
  })
}

export function createFilePreviewUrl(id) {
  return request({
    url: 'api/qiNiuContent/createFilePreviewUrl/' + id,
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
export default { del, createFilePreviewUrl, sync }
