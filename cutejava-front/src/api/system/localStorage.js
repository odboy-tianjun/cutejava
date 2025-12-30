import request from '@/utils/request'
/**
 * 默认入口：add、del、edit、get
 */
export function add(data) {
  return request({
    url: 'api/localStorage/uploadFile',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/localStorage/removeFileByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/localStorage/modifyLocalStorageById',
    method: 'post',
    data: data
  })
}

export default { add, edit, del }
