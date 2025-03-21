import request from '@/utils/request'

export function getAllJob() {
  const params = {
    page: 0,
    pageSize: 9999,
    args: {
      enabled: true
    }
  }
  return request({
    url: 'api/job/query',
    method: 'post',
    data: params
  })
}

export function add(data) {
  return request({
    url: 'api/job/save',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/job/remove',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/job/modify',
    method: 'post',
    data: data
  })
}

export default { add, edit, del }
