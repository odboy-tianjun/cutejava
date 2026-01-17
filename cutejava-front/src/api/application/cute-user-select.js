import request from '@/utils/request'

export function listMetadata(query) {
  return request({
    url: 'api/component/CuteUserSelect/listMetadata',
    method: 'post',
    data: {
      blurry: query
    }
  })
}

export function listMetadataByUsernames(usernameList = []) {
  return request({
    url: 'api/component/CuteUserSelect/listMetadataByUsernames',
    method: 'post',
    data: usernameList
  })
}

export default { listMetadata, listMetadataByUsernames }

