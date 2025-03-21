import request from '@/utils/request'

export function getDepts(params) {
  return request({
    url: 'api/dept/query',
    method: 'post',
    data: params
  })
}

export function getDeptSuperior(ids, exclude) {
  exclude = exclude !== undefined ? exclude : false
  const data = Array.isArray(ids) ? ids : [ids]
  return request({
    url: 'api/dept/superior?exclude=' + exclude,
    method: 'post',
    data: data
  })
}

export function add(data) {
  return request({
    url: 'api/dept/save',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/dept/remove',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/dept/modify',
    method: 'post',
    data: data
  })
}

export default { add, edit, del, getDepts, getDeptSuperior }
