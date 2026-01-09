import request from '@/utils/request'

export function sendResetEmailCode(email) {
  return request({
    url: 'api/captcha/sendResetEmailCode?email=' + email,
    method: 'post'
  })
}

export function sendResetPasswordCode(email) {
  return request({
    url: 'api/captcha/sendResetPasswordCode?email=' + email,
    method: 'post'
  })
}

export function verifyCode(email, code, bizCode) {
  const params = {
    email: email,
    code: code,
    bizCode: bizCode
  }
  return request({
    url: 'api/captcha/verifyCode',
    method: 'post',
    data: params
  })
}
