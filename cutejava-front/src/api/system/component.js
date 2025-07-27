import request from '@/utils/request'

export function queryDeptSelectDataSource() {
  return request({
    url: 'api/component/queryDeptSelectDataSource',
    method: 'post'
  })
}
