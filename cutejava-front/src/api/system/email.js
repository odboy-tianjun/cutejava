import request from '@/utils/request'

export function getLastEmailConfig() {
  return request({
    url: 'api/email/getLastEmailConfig',
    method: 'post'
  })
}

export function modifyEmailConfig(data) {
  return request({
    url: 'api/email/modifyEmailConfig',
    method: 'post',
    data: data
  })
}

export function sendEmail(data) {
  return request({
    url: 'api/email/sendEmail',
    method: 'post',
    data: data
  })
}
