import request from '@/utils/request'

export function getDepts(params) {
  return request({
    url: 'api/dept',
    method: 'get',
    params
  })
}

export function getDeptSuperior(ids, exclude) {
  exclude = exclude !== undefined ? exclude : false
  const data = Array.isArray(ids) ? ids : [ids]
  return request({
    url: 'api/dept/getDeptSuperior?exclude=' + exclude,
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: 'api/dept/createDept',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/dept/deleteDept',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/dept/updateDept',
    method: 'post',
    data
  })
}

export default { add, edit, del, getDepts, getDeptSuperior }
