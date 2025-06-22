import request from '@/utils/request'

export function sendResetEmailCaptcha(data) {
  return request({
    url: 'api/code/sendResetEmailCaptcha?email=' + data,
    method: 'post'
  })
}

export function sendResetPasswordCaptcha(data) {
  return request({
    url: 'api/code/sendResetPasswordCaptcha?email=' + data,
    method: 'post'
  })
}

export function checkEmailCaptcha(email, code, codeBi) {
  return request({
    url: `api/code/checkEmailCaptcha?email=${email}&code=${code}&codeBi=${codeBi}`,
    method: 'post'
  })
}
