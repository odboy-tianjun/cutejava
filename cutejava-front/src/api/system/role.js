import request from '@/utils/request'

// 获取所有的Role
export function queryRoleList() {
  return request({
    url: 'api/role/queryRoleList',
    method: 'post'
  })
}

export function add(data) {
  return request({
    url: 'api/role/saveRole',
    method: 'post',
    data: data
  })
}

export function get(id) {
  return request({
    url: 'api/role/queryRoleById',
    method: 'post',
    data: { id: id }
  })
}

export function getLevel() {
  return request({
    url: 'api/role/queryRoleLevel',
    method: 'post'
  })
}

export function del(ids) {
  return request({
    url: 'api/role/removeRoleByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/role/modifyRoleById',
    method: 'post',
    data: data
  })
}

export function editMenu(data) {
  return request({
    url: 'api/role/modifyBindMenuById',
    method: 'post',
    data
  })
}

export default { add, edit, del, get, editMenu, getLevel }
