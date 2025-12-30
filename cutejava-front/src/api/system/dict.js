import request from '@/utils/request'
/**
 * 默认入口：add、del、edit、get
 */
export function add(data) {
  return request({
    url: 'api/dict/saveDict',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/dict/deleteDictByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/dict/updateDictById',
    method: 'post',
    data: data
  })
}

export function searchDict(data) {
  return request({
    url: 'api/dict/searchDict',
    method: 'post',
    data: data
  })
}

export default { add, edit, del, searchDict }
