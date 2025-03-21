import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/jobs/save',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/jobs/remove',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/jobs/modify',
    method: 'post',
    data: data
  })
}

export function updateIsPause(id) {
  return request({
    url: 'api/jobs/' + id,
    method: 'post'
  })
}

export function execution(id) {
  return request({
    url: 'api/jobs/exec/' + id,
    method: 'post'
  })
}

export default { del, updateIsPause, execution, add, edit }
