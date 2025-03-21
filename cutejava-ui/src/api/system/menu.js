import request from '@/utils/request'

export function getMenusTree(pid) {
  return request({
    url: 'api/menus/lazy?pid=' + pid,
    method: 'get'
  })
}

export function getMenus(params) {
  return request({
    url: 'api/menus/query',
    method: 'post',
    data: params
  })
}

export function getMenuSuperior(ids) {
  const data = Array.isArray(ids) ? ids : [ids]
  return request({
    url: 'api/menus/superior',
    method: 'post',
    data: data
  })
}

export function getChild(id) {
  return request({
    url: 'api/menus/child?id=' + id,
    method: 'get'
  })
}

export function buildMenus() {
  return request({
    url: 'api/menus/build',
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'api/menus/save',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/menus/remove',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/menus/modify',
    method: 'post',
    data: data
  })
}

export default { add, edit, del, getMenusTree, getMenuSuperior, getMenus, getChild }
