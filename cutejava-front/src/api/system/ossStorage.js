import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/ossStorage/uploadFile',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/ossStorage/removeFileByIds',
    method: 'post',
    data: ids
  })
}

export default { add, del }
