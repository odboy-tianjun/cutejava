import request from '@/utils/request'

// 获取所有的Role
export function getAll() {
  return request({
    url: 'api/roles/queryAllRole',
    method: 'post'
  })
}

export function add(data) {
  return request({
    url: 'api/roles/createRole',
    method: 'post',
    data
  })
}

export function get(id) {
  return request({
    url: 'api/roles/getRoleById/' + id,
    method: 'post'
  })
}

export function getLevel() {
  return request({
    url: 'api/roles/getRoleLevel',
    method: 'post'
  })
}

export function del(ids) {
  return request({
    url: 'api/roles/deleteRole',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/roles/updateRole',
    method: 'post',
    data
  })
}

export function editMenu(data) {
  return request({
    url: 'api/roles/updateRoleMenu',
    method: 'post',
    data
  })
}

export default { add, edit, del, get, editMenu, getLevel }
