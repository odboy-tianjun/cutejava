import request from '@/utils/request'
/**
 * 默认入口：add、del、edit、get
 */
export function add(data) {
  return request({
    url: 'api/role/saveRole',
    method: 'post',
    data: data
  })
}

export function get(id) {
  return request({
    url: 'api/role/getRoleById',
    method: 'post',
    data: { id: id }
  })
}

export function del(ids) {
  return request({
    url: 'api/role/deleteRoleByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/role/updateRoleById',
    method: 'post',
    data: data
  })
}

// 获取所有的Role
export function queryRoleList() {
  return request({
    url: 'api/role/listAllRole',
    method: 'post'
  })
}

export function getLevel() {
  return request({
    url: 'api/role/getCurrentUserRoleLevel',
    method: 'post'
  })
}

export function editMenu(data) {
  return request({
    url: 'api/role/updateBindMenuById',
    method: 'post',
    data
  })
}

export default { add, edit, del, get, editMenu, getLevel }
