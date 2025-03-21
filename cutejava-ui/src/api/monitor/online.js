import request from '@/utils/request'

export function del(keys) {
  return request({
    url: 'auth/online/kickUser',
    method: 'post',
    data: keys
  })
}
