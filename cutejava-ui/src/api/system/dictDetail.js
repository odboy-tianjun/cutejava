import request from '@/utils/request'

export function get(dictName) {
  const params = {
    page: 1,
    pageSize: 9999,
    args: {
      dictName: dictName
    }
  }
  return request({
    url: 'api/dictDetail/query',
    method: 'post',
    data: params
  })
}

export function getDictMap(dictName) {
  const params = {
    page: 1,
    pageSize: 9999,
    args: {
      dictName: dictName
    }
  }
  return request({
    url: 'api/dictDetail/map',
    method: 'post',
    data: params
  })
}

export function add(data) {
  return request({
    url: 'api/dictDetail/save',
    method: 'post',
    data: data
  })
}

export function del(id) {
  return request({
    url: 'api/dictDetail/' + id,
    method: 'post'
  })
}

export function edit(data) {
  return request({
    url: 'api/dictDetail/modify',
    method: 'post',
    data: data
  })
}

export default { add, edit, del }
