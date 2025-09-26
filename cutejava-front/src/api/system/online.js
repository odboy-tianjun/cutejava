import request from '@/utils/request'

export function kickOutUser(keys) {
  return request({
    url: 'api/user/online/kickOutUser',
    method: 'post',
    data: keys
  })
}
