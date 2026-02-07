import request from '@/utils/request'

export function searchMenu(query) {
  return request({
    url: 'api/features/CuteDynamicTable/searchMenu',
    method: 'post',
    data: query
  })
}

export function searchMenu2(query) {
  return request({
    url: 'api/features/CuteDynamicTable/searchMenu2',
    method: 'post',
    data: query
  })
}

export function searchMenu3(query) {
  return request({
    url: 'api/features/CuteDynamicTable/searchMenu3',
    method: 'post',
    data: query
  })
}

export default { searchMenu, searchMenu2, searchMenu3 }

