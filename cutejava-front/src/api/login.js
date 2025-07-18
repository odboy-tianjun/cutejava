import request from '@/utils/request'

export function login(username, password, code, uuid) {
  return request({
    url: 'auth/login',
    method: 'post',
    data: {
      username,
      password,
      code,
      uuid
    }
  })
}

export function getInfo() {
  return request({
    url: 'auth/info',
    method: 'post'
  })
}

export function getCodeImg() {
  return request({
    url: 'auth/captcha/getImage',
    method: 'post'
  })
}

export function logout() {
  return request({
    url: 'auth/logout',
    method: 'post'
  })
}
