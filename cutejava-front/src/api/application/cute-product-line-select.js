import request from '@/utils/request'

export function listMetadata() {
  return request({
    url: 'api/component/CuteProductLineSelect/listMetadata',
    method: 'post'
  })
}
