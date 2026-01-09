import request from '@/utils/request'

/**
 * 默认入口：add、del、edit、get
 */
export function add(data) {
  return request({
    url: 'api/dept/saveDept',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/dept/deleteDeptByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/dept/updateDeptById',
    method: 'post',
    data: data
  })
}

export function searchDept(params) {
  return request({
    url: 'api/dept/searchDept',
    method: 'post',
    data: params
  })
}

export function searchDeptTree(ids, exclude) {
  exclude = exclude !== undefined ? exclude : false
  const data = Array.isArray(ids) ? ids : [ids]
  return request({
    url: 'api/dept/searchDeptTree?exclude=' + exclude,
    method: 'post',
    data: data
  })
}

export default { add, edit, del, searchDept, searchDeptTree }
