import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/dict/saveDict',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/dict/removeDictByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/dict/modifyDictById',
    method: 'post',
    data: data
  })
}

export function queryPageDict(data) {
  return request({
    url: 'api/dict',
    method: 'post',
    data: data
  })
}

export default { add, edit, del, queryPageDict }
