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

export function validatedCaptcha(email, code, codeBi) {
  return request({
    url: `api/code/validatedCaptcha?email=${email}&code=${code}&codeBi=${codeBi}`,
    method: 'post'
  })
}
