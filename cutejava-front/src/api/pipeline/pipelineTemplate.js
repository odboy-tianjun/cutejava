import request from '@/utils/request'

export function getPipelineTemplate(id) {
  return request({
    url: 'api/devops/pipelineTemplate/getPipelineTemplate',
    method: 'post',
    data: { id }
  })
}
