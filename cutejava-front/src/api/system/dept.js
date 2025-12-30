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
    url: 'api/dept/removeDeptByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/dept/modifyDept',
    method: 'post',
    data: data
  })
}

export function queryDeptList(params) {
  return request({
    url: 'api/dept/queryAllDeptByArgs',
    method: 'post',
    data: params
  })
}

export function queryDeptSuperiorTree(ids, exclude) {
  exclude = exclude !== undefined ? exclude : false
  const data = Array.isArray(ids) ? ids : [ids]
  return request({
    url: 'api/dept/queryDeptSuperiorTree?exclude=' + exclude,
    method: 'post',
    data: data
  })
}

export default { add, edit, del, queryDeptList, queryDeptSuperiorTree }
