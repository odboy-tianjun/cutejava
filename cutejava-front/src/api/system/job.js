import request from '@/utils/request'

export function queryAllEnableJob() {
  const params = {
    page: 1,
    size: 9999999,
    args: {
      enabled: true
    }
  }
  return request({
    url: 'api/job/queryAllEnableJob',
    method: 'post',
    data: params
  })
}

export function add(data) {
  return request({
    url: 'api/job/saveJob',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/job/removeJobByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/job/modifyJobById',
    method: 'post',
    data: data
  })
}

export default { add, edit, del }
