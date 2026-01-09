import request from '@/utils/request'
/**
 * 默认入口：add、del、edit、get
 */
export function get(dictName) {
  const params = {
    page: 1,
    size: 9999999,
    args: {
      dictName: dictName
    }
  }
  return request({
    url: 'api/dictDetail/searchDictDetail',
    method: 'post',
    data: params
  })
}

export function add(data) {
  return request({
    url: 'api/dictDetail/saveDictDetail',
    method: 'post',
    data: data
  })
}

export function del(id) {
  return request({
    url: 'api/dictDetail/deleteDictDetailById',
    method: 'post',
    data: { id: id }
  })
}

export function edit(data) {
  return request({
    url: 'api/dictDetail/updateDictDetailById',
    method: 'post',
    data: data
  })
}

export default { add, edit, del }
