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

export function resetPwd(ids) {
  return request({
    url: 'api/user/resetUserPasswordByIds',
    method: 'post',
    data: ids
  })
}

export function editUser(data) {
  return request({
    url: 'api/user/updateUserCenterInfoById',
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
    url: 'api/user/updateUserPasswordByUsername',
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
    url: 'api/user/updateUserEmailByUsername/' + form.code,
    method: 'post',
    data: data
  })
}

export function queryUserMetaPage(query) {
  const params = {
    page: 1,
    size: 50,
    args: {
      blurry: query
    }
  }
  return request({
    url: 'api/user/queryUserMetadataOptions',
    method: 'post',
    data: params
  })
}

export default { add, edit, del, resetPwd, queryUserMetaPage }

