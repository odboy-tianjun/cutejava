import request from '@/utils/request'

export function queryEmailConfig() {
  return request({
    url: 'api/email/queryEmailConfig',
    method: 'post'
  })
}

export function updateEmailConfig(data) {
  return request({
    url: 'api/email/updateEmailConfig',
    method: 'post',
    data
  })
}

export function sendEmail(data) {
  return request({
    url: 'api/email/sendEmail',
    method: 'post',
    data
  })
}
