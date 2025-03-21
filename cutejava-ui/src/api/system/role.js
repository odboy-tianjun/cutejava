import request from '@/utils/request'

// 获取所有的Role
export function getAll() {
  return request({
    url: 'api/roles/all',
    method: 'post'
  })
}

export function add(data) {
  return request({
    url: 'api/roles',
    method: 'post',
    data: data
  })
}

export function get(id) {
  return request({
    url: 'api/roles/' + id,
    method: 'post'
  })
}

export function getLevel() {
  return request({
    url: 'api/roles/level',
    method: 'post'
  })
}

export function del(ids) {
  return request({
    url: 'api/roles/remove',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/roles/modify',
    method: 'post',
    data: data
  })
}

export function editMenu(data) {
  return request({
    url: 'api/roles/menu',
    method: 'post',
    data: data
  })
}

export default { add, edit, del, get, editMenu, getLevel }
