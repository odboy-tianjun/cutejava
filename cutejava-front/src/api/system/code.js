import request from '@/utils/request'

export function sendResetEmailCaptcha(email) {
  return request({
    url: 'api/captcha/sendResetEmailCaptcha?email=' + email,
    method: 'post'
  })
}

export function sendResetPasswordCaptcha(email) {
  return request({
    url: 'api/captcha/sendResetPasswordCaptcha?email=' + email,
    method: 'post'
  })
}

export function checkEmailCaptcha(email, code, bizCode) {
  const params = {
    email: email,
    code: code,
    bizCode: bizCode
  }
  return request({
    url: 'api/captcha/checkEmailCaptcha',
    method: 'post',
    data: params
  })
}
