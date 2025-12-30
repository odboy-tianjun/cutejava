import request from '@/utils/request'
/**
 * 默认入口：add、del、edit、get
 */
export function add(data) {
  return request({
    url: 'api/quartzJob/createQuartzJob',
    method: 'post',
    data: data
  })
}

export function del(ids) {
  return request({
    url: 'api/quartzJob/removeJobByIds',
    method: 'post',
    data: ids
  })
}

export function edit(data) {
  return request({
    url: 'api/quartzJob/updateQuartzJobResumeCron',
    method: 'post',
    data: data
  })
}

export function switchQuartzJobStatus(id) {
  return request({
    url: 'api/quartzJob/switchQuartzJobStatus/' + id,
    method: 'post'
  })
}

export function startQuartzJob(id) {
  return request({
    url: 'api/quartzJob/startQuartzJob/' + id,
    method: 'post'
  })
}

export default { add, edit, del, switchQuartzJobStatus, startQuartzJob }
