import request from '@/utils/request'

export function listMetadata(query) {
  const params = {
    blurry: query
  }
  return request({
    url: 'api/component/CuteUserSelect/listMetadata',
    method: 'post',
    data: params
  })
}

export default { listMetadata }

