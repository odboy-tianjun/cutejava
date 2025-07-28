import request from '@/utils/request'

export function queryDeptSelectDataSource() {
  return request({
    url: 'api/component/queryDeptSelectDataSource',
    method: 'post'
  })
}

export function queryDeptSelectProDataSource() {
  return request({
    url: 'api/component/queryDeptSelectProDataSource',
    method: 'post'
  })
}
