import request from '@/utils/request'
import { encrypt } from '@/utils/CsRsaUtil'

/**
 * 默认入口：add、del、edit、get
 */
export function add(data) {
  return request({
    url: 'api/user/saveUser',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/user/deleteUserByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/user/updateUserById',
    method: 'post',
    data: data
  })
}

export function resetUserPasswordByIds(ids) {
  return request({
    url: 'api/user/resetUserPasswordByIds',
    method: 'post',
    data: ids
  })
}

export function updateUserCenterInfoById(data) {
  return request({
    url: 'api/user/updateUserCenterInfoById',
    method: 'post',
    data: data
  })
}

export function updateUserPasswordByUsername(user) {
  const data = {
    oldPass: encrypt(user.oldPass),
    newPass: encrypt(user.newPass)
  }
  return request({
    url: 'api/user/updateUserPasswordByUsername',
    method: 'post',
    data: data
  })
}

export function updateUserEmailByUsername(form) {
  const data = {
    password: encrypt(form.pass),
    email: form.email
  }
  return request({
    url: 'api/user/updateUserEmailByUsername/' + form.code,
    method: 'post',
    data: data
  })
}

export default { add, edit, del, resetUserPasswordByIds }

