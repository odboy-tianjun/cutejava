import request from '@/utils/request'

export function searchMenu(query) {
  return request({
    url: 'api/features/CuteDynamicTable/searchMenu',
    method: 'post',
    data: query
  })
}

export default { searchMenu }

