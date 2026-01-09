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
    url: 'api/user/getCurrentUserInfo',
    method: 'post'
  })
}

export function getCodeImg() {
  return request({
    url: 'captcha/getCode',
    method: 'post'
  })
}

export function logout() {
  return request({
    url: 'auth/logout',
    method: 'post'
  })
}
