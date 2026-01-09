import request from '@/utils/request'

export function listDeptSelectDataSource() {
  return request({
    url: 'api/component/listDeptSelectDataSource',
    method: 'post'
  })
}

export function listDeptSelectProDataSource() {
  return request({
    url: 'api/component/listDeptSelectProDataSource',
    method: 'post'
  })
}
