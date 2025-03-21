import request from '@/utils/request'
import { encrypt } from '@/utils/rsaEncrypt'

export function add(data) {
  return request({
    url: 'api/users/save',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/users/remove',
    method: 'post',
    data: ids
  })
}

export function resetPwd(ids) {
  return request({
    url: 'api/users/resetPwd',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/users/modify',
    method: 'post',
    data: data
  })
}

export function editUser(data) {
  return request({
    url: 'api/users/center',
    method: 'post',
    data: data
  })
}

export function updatePass(user) {
  const data = {
    oldPass: encrypt(user.oldPass),
    newPass: encrypt(user.newPass)
  }
  return request({
    url: 'api/users/updatePass/',
    method: 'post',
    data: data
  })
}

export function updateEmail(form) {
  const data = {
    password: encrypt(form.pass),
    email: form.email
  }
  return request({
    url: 'api/users/updateEmail/' + form.code,
    method: 'post',
    data: data
  })
}

export default { add, edit, del, resetPwd }

