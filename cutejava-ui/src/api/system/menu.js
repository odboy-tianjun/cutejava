import request from '@/utils/request'

export function getMenusTree(pid) {
  return request({
    url: 'api/menus/queryAllMenu?pid=' + pid,
    method: 'post'
  })
}

export function getMenus(params) {
  return request({
    url: 'api/menus',
    method: 'get',
    params
  })
}

export function getMenuSuperior(ids) {
  const data = Array.isArray(ids) ? ids : [ids]
  return request({
    url: 'api/menus/queryMenuSuperior',
    method: 'post',
    data
  })
}

export function getChild(id) {
  return request({
    url: 'api/menus/queryChildMenu?id=' + id,
    method: 'post'
  })
}

export function buildMenus() {
  return request({
    url: 'api/menus/buildMenus',
    method: 'post'
  })
}

export function add(data) {
  return request({
    url: 'api/menus/createMenu',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/menus/deleteMenu',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/menus/updateMenu',
    method: 'post',
    data
  })
}

export default { add, edit, del, getMenusTree, getMenuSuperior, getMenus, getChild }
