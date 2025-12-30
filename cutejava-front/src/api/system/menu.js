import request from '@/utils/request'

/**
 * 默认入口：add、del、edit、get
 */
export function add(data) {
  return request({
    url: 'api/menu/saveMenu',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/menu/removeMenuByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/menu/modifyMenuById',
    method: 'post',
    data: data
  })
}

export function queryMenuListByPid(pid) {
  return request({
    url: 'api/menu/queryMenuListByPid?pid=' + pid,
    method: 'post'
  })
}

export function queryMenuByArgs(params) {
  return request({
    url: 'api/menu/queryMenuByArgs',
    method: 'post',
    data: params
  })
}

export function queryMenuSuperior(ids) {
  const data = Array.isArray(ids) ? ids : [ids]
  return request({
    url: 'api/menu/queryMenuSuperior',
    method: 'post',
    data: data
  })
}

export function queryChildMenuSet(id) {
  return request({
    url: 'api/menu/queryChildMenuSet?id=' + id,
    method: 'post'
  })
}

export function buildMenus() {
  return request({
    url: 'api/menu/buildMenus',
    method: 'post'
  })
}

export default { add, edit, del, queryMenuListByPid, queryMenuSuperior, queryMenuByArgs, queryChildMenuSet }
