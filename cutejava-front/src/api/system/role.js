import request from '@/utils/request'

// 获取所有的Role
export function queryRoleList() {
  return request({
    url: 'api/roles/queryRoleList',
    method: 'post'
  })
}

export function add(data) {
  return request({
    url: 'api/roles/saveRole',
    method: 'post',
    data
  })
}

export function get(id) {
  return request({
    url: 'api/roles/queryRoleById',
    method: 'post',
    data: { id: id }
  })
}

export function getLevel() {
  return request({
    url: 'api/roles/queryRoleLevel',
    method: 'post'
  })
}

export function del(ids) {
  return request({
    url: 'api/roles/removeRoleByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/roles/modifyRoleById',
    method: 'post',
    data
  })
}

export function editMenu(data) {
  return request({
    url: 'api/roles/modifyBindMenuById',
    method: 'post',
    data
  })
}

export default { add, edit, del, get, editMenu, getLevel }
