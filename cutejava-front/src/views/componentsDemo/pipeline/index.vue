<template>
  <CutePage>
    <Pipeline instance-id="instanceId1" :task-nodes="task1Nodes" />
    <Pipeline instance-id="instanceId2" :task-nodes="task2Nodes" />
    <Pipeline instance-id="instanceId3" :task-nodes="task3Nodes" />
    <Pipeline instance-id="instanceId4" :task-nodes="task4Nodes" />
  </CutePage>
</template>
<script>

import Pipeline from '@/views/components/devops/pipeline/Pipeline.vue'
import CutePage from '@/views/components/dev/CutePage.vue'

export default {
  name: 'ComponentsDemoPipeline',
  components: { CutePage, Pipeline },
  data() {
    return {
      task1Nodes: [
        {
          click: false,
          code: 'release',
          desc: '执行成功',
          durationMillis: 7446,
          name: '环境准备',
          startTimeMillis: 1652343103245,
          status: 'success',
          statusDesc: '成功'
        },
        {
          click: true,
          retry: true,
          code: 'merge',
          desc: '执行成功',
          detailType: 'gitlab',
          durationMillis: 50331,
          name: '合并代码',
          startTimeMillis: 1652343110691,
          status: 'success',
          statusDesc: '成功'
        },
        {
          click: false,
          code: 'stc_upload',
          desc: '代码上传成功',
          durationMillis: 16123,
          name: 'STC扫描(代码上传)',
          startTimeMillis: 1652343161022,
          status: 'success',
          statusDesc: '成功'
        },
        {
          click: true,
          retry: true,
          code: 'build',
          desc: '执行成功',
          detailType: 'gitlab',
          durationMillis: 104692,
          name: '构建',
          startTimeMillis: 1652343177145,
          status: 'success',
          statusDesc: '成功'
        },
        {
          buttonList: [
            {
              condition: 'all',
              blank: true,
              linkUrl: 'https://devops.odboy.cn/depolyHistory/release?appName=kenaito-pilot&id=1',
              text: '查看扫描结果',
              type: 'link'
            }
          ],
          click: false,
          code: 'stc_result',
          desc: 'STC扫描通过',
          durationMillis: 18971,
          name: 'STC扫描(扫描结果)',
          startTimeMillis: 1652343281837,
          status: 'success',
          statusDesc: '成功'
        },
        {
          click: false,
          code: 'doPreCheck',
          desc: '【田俊】确认通过',
          durationMillis: 69937,
          name: '发布前确认',
          startTimeMillis: 1652343300808,
          status: 'success',
          statusDesc: '成功'
        },
        {
          buttonList: [
            {
              condition: 'success',
              blank: true,
              linkUrl: 'https://devops.odboy.cn/depolyHistory/release?appName=kenaito-pilot&id=1',
              text: '查看部署详情',
              type: 'link'
            }
          ],
          click: false,
          code: 'deploy',
          desc: '成功',
          durationMillis: 161975,
          name: '部署',
          startTimeMillis: 1652343370745,
          status: 'success',
          statusDesc: '成功'
        },
        {
          buttonList: [
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/doCheck?id=60012',
              text: '确认合并',
              type: 'execute'
            }
          ],
          click: false,
          code: 'mergeMaster',
          detailType: 'gitlab',
          durationMillis: 0,
          name: '合并主干',
          desc: '合并master确认',
          startTimeMillis: 0,
          status: 'running',
          statusDesc: '执行中'
        }
      ],
      task2Nodes: [
        {
          click: false,
          code: 'release',
          desc: '执行成功',
          durationMillis: 7446,
          name: '环境准备',
          startTimeMillis: 1652343103245,
          status: 'success',
          statusDesc: '成功'
        },
        {
          click: true,
          retry: true,
          code: 'merge',
          desc: '执行成功',
          detailType: 'gitlab',
          durationMillis: 50331,
          name: '合并代码',
          startTimeMillis: 1652343110691,
          status: 'success',
          statusDesc: '成功'
        },
        {
          click: true,
          retry: true,
          code: 'build',
          desc: '执行成功',
          detailType: 'gitlab',
          durationMillis: 104692,
          name: '构建',
          startTimeMillis: 1652343177145,
          status: 'success',
          statusDesc: '成功'
        },
        {
          buttonList: [
            {
              condition: 'success',
              blank: true,
              linkUrl: 'https://devops.odboy.cn/depolyHistory/release?appName=kenaito-pilot&id=1',
              text: '查看部署详情',
              type: 'link'
            },
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/apply/doAgree?id=60012',
              text: '同意',
              type: 'execute'
            },
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/apply/doRefuse?id=60012',
              text: '拒绝',
              color: '#ff0000',
              type: 'execute'
            }
          ],
          click: false,
          code: 'deploy',
          desc: '等待部署审批',
          durationMillis: 161975,
          name: '部署',
          startTimeMillis: 1652343370745,
          status: 'running',
          statusDesc: '执行中'
        },
        {
          buttonList: [
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/doCheck?id=60012',
              text: '确认合并',
              type: 'execute'
            }
          ],
          click: false,
          code: 'mergeMaster',
          detailType: 'gitlab',
          durationMillis: 7446,
          name: '合并主干',
          desc: '合并master确认',
          startTimeMillis: 1652343103245,
          status: 'success',
          statusDesc: '成功'
        }
      ],
      task3Nodes: [
        {
          click: false,
          code: 'release',
          desc: '执行成功',
          durationMillis: 7446,
          name: '环境准备',
          startTimeMillis: 1652343103245,
          status: 'success',
          statusDesc: '成功'
        },
        {
          click: true,
          retry: true,
          code: 'merge',
          desc: '执行失败',
          detailType: 'gitlab',
          durationMillis: 50331,
          name: '合并代码',
          startTimeMillis: 1652343110691,
          status: 'failed',
          statusDesc: '失败'
        },
        {
          click: true,
          retry: true,
          code: 'build',
          desc: '待执行',
          detailType: 'gitlab',
          durationMillis: 0,
          name: '构建',
          startTimeMillis: 0,
          status: 'pending',
          statusDesc: '待执行'
        },
        {
          buttonList: [
            {
              condition: 'success',
              blank: true,
              linkUrl: 'https://devops.odboy.cn/depolyHistory/release?appName=kenaito-pilot&id=1',
              text: '查看部署详情',
              type: 'link'
            },
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/apply/doAgree?id=60012',
              text: '同意',
              type: 'execute'
            },
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/apply/doRefuse?id=60012',
              text: '拒绝',
              color: '#ff0000',
              type: 'execute'
            }
          ],
          click: false,
          code: 'deploy',
          desc: '待执行',
          durationMillis: 0,
          name: '部署',
          startTimeMillis: 0,
          status: 'pending',
          statusDesc: '待执行'
        },
        {
          buttonList: [
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/doCheck?id=60012',
              text: '确认合并',
              type: 'execute'
            }
          ],
          click: false,
          code: 'mergeMaster',
          detailType: 'gitlab',
          durationMillis: 0,
          name: '合并主干',
          desc: '待执行',
          startTimeMillis: 0,
          status: 'pending',
          statusDesc: '待执行'
        }
      ],
      task4Nodes: [
        {
          click: false,
          code: 'release',
          desc: '执行成功',
          durationMillis: 7446,
          name: '环境准备',
          startTimeMillis: 1652343103245,
          status: 'success',
          statusDesc: '成功'
        },
        {
          click: true,
          retry: true,
          code: 'merge',
          desc: '执行成功',
          detailType: 'gitlab',
          durationMillis: 50331,
          name: '合并代码',
          startTimeMillis: 1652343110691,
          status: 'success',
          statusDesc: '成功'
        },
        {
          click: true,
          retry: true,
          code: 'build',
          desc: '执行失败',
          detailType: 'gitlab',
          durationMillis: 50331,
          name: '构建',
          startTimeMillis: 1652343110691,
          status: 'failed',
          statusDesc: '失败'
        },
        {
          buttonList: [
            {
              condition: 'success',
              blank: true,
              linkUrl: 'https://devops.odboy.cn/depolyHistory/release?appName=kenaito-pilot&id=1',
              text: '查看部署详情',
              type: 'link'
            },
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/apply/doAgree?id=60012',
              text: '同意',
              type: 'execute'
            },
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/apply/doRefuse?id=60012',
              text: '拒绝',
              color: '#ff0000',
              type: 'execute'
            }
          ],
          click: false,
          code: 'deploy',
          desc: '待执行',
          durationMillis: 0,
          name: '部署',
          startTimeMillis: 0,
          status: 'pending',
          statusDesc: '待执行'
        },
        {
          buttonList: [
            {
              condition: 'running',
              method: 'get',
              requestUrl: 'api/devops/ci/doCheck?id=60012',
              text: '确认合并',
              type: 'execute'
            }
          ],
          click: false,
          code: 'mergeMaster',
          detailType: 'gitlab',
          durationMillis: 0,
          name: '合并主干',
          desc: '待执行',
          startTimeMillis: 0,
          status: 'pending',
          statusDesc: '待执行'
        }
      ]
    }
  }
}
</script>
<style lang="scss" scoped>
ul {
  padding-left: 20px;
}

.description > li {
  font-size: 12px;
}
</style>
