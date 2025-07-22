import request from '@/utils/request'

export function startPipeline() {
  return request({
    url: 'api/devops/pipeline/start',
    method: 'post'
  })
}

export function restartPipeline() {
  return request({
    url: 'api/devops/pipeline/restart',
    method: 'post'
  })
}

export function queryLastPipelineDetail(instanceId) {
  return request({
    url: 'api/devops/pipeline/last',
    method: 'post',
    data: {
      instanceId
    }
  })
}
