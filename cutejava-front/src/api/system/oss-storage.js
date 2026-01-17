import request from '@/utils/request'
/**
 * 默认入口：add、del、edit、get
 */
export function add(data) {
  return request({
    url: 'api/ossStorage/uploadFile',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/ossStorage/deleteFileByIds',
    method: 'post',
    data: ids
  })
}

export default { add, del }
