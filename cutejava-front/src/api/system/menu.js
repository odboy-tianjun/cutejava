import request from '@/utils/request'

export function queryMenuListByPid(pid) {
  return request({
    url: 'api/menus/queryMenuListByPid?pid=' + pid,
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

export function queryMenuSuperior(ids) {
  const data = Array.isArray(ids) ? ids : [ids]
  return request({
    url: 'api/menus/queryMenuSuperior',
    method: 'post',
    data
  })
}

export function queryChildMenuSet(id) {
  return request({
    url: 'api/menus/queryChildMenuSet?id=' + id,
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
    url: 'api/menus/saveMenu',
    method: 'post',
    data
  })
}

export function del(ids) {
  return request({
    url: 'api/menus/removeMenuByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/menus/modifyMenuById',
    method: 'post',
    data
  })
}

export default { add, edit, del, queryMenuListByPid, queryMenuSuperior, getMenus, queryChildMenuSet }
