import request from '@/utils/request'

export function sendCaptcha(data) {
  return request({
    url: 'api/code/sendCaptcha?email=' + data,
    method: 'post'
  })
}

export function updatePass(pass) {
  return request({
    url: 'api/users/updatePass/' + pass,
    method: 'get'
  })
}
