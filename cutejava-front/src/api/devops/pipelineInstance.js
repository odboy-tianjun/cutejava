import request from '@/utils/request'

export function startPipeline() {
  return request({
    url: 'api/devops/pipelineInstance/start',
    method: 'post'
  })
}

export function restartPipeline() {
  return request({
    url: 'api/devops/pipelineInstance/restart',
    method: 'post'
  })
}

export function queryLastPipelineDetail(instanceId) {
  return request({
    url: 'api/devops/pipelineInstance/last',
    method: 'post',
    data: {
      instanceId
    }
  })
}
