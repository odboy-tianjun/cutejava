import request from '@/utils/request'

export function listMetadata(query) {
  return request({
    url: 'api/component/CuteDeptTree/listMetadata',
    method: 'post',
    data: query
  })
}

export default { listMetadata }

