import request from '@/utils/request'

export function listMetadata() {
  return request({
    url: 'api/component/CuteProductLineSelectPro/listMetadata',
    method: 'post'
  })
}
