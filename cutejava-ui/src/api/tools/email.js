import request from '@/utils/request'

export function get() {
  return request({
    url: 'api/email',
    method: 'get'
  })
}

export function update(data) {
  return request({
    url: 'api/email/modify',
    method: 'post',
    data: data
  })
}

export function send(data) {
  return request({
    url: 'api/email/send',
    method: 'post',
    data: data
  })
}
