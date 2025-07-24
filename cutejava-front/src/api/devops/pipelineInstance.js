import request from '@/utils/request'

export function startPipeline(data) {
  return request({
    url: 'api/devops/pipelineInstance/start',
    method: 'post',
    data
  })
}

export function restartPipeline(data) {
  return request({
    url: 'api/devops/pipelineInstance/restart',
    method: 'post',
    data
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

export function queryLastPipelineDetailWs(instanceId) {
  return request({
    url: 'api/devops/pipelineInstance/lastWs',
    method: 'post',
    data: {
      instanceId
    }
  })
}
