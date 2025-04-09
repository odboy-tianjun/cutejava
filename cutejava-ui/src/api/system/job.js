import request from '@/utils/request'

export function getAllJob() {
  const params = {
    page: 0,
    size: 9999,
    enabled: true
  }
  return request({
    url: 'api/job',
    method: 'get',
    params
  })
}

export function add(data) {
  return request({
    url: 'api/job/createJob',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/job/deleteJob',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/job/updateJob',
    method: 'post',
    data
  })
}

export default { add, edit, del }
