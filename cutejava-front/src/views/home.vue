<template>
  <div class="dashboard-editor-container">
    <github-corner class="github-corner" />

    <el-row :gutter="40" style="background-color: white;margin-left: 2px;margin-right: 2px;padding: 6px">
      <el-col>系统API文档：
        <el-link type="primary" :href="swaggerApi" target="_blank">{{ swaggerApi }}</el-link>
      </el-col>
    </el-row>

    <panel-group @handleSetLineChartData="handleSetLineChartData" />

    <div style="border: 1px solid #E4E7ED;margin-bottom: 10px">
      <Pipeline instance-id="instanceId1" :task-nodes="task1Nodes" />
      <Pipeline instance-id="instanceId2" :task-nodes="task2Nodes" />
      <Pipeline instance-id="instanceId3" :task-nodes="task3Nodes" />
      <Pipeline instance-id="instanceId4" :task-nodes="task4Nodes" />
    </div>

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" />
    </el-row>
    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <radar-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import GithubCorner from '@/components/GithubCorner'
import PanelGroup from './dashboard/PanelGroup'
import LineChart from './dashboard/LineChart'
import RadarChart from '@/components/Echarts/RadarChart'
import PieChart from '@/components/Echarts/PieChart'
import BarChart from '@/components/Echarts/BarChart'
import store from '@/store'
import Pipeline from '@/views/components/devops/pipeline/Pipeline.vue'

const lineChartData = {
  newVisitis: {
    expectedData: [100, 120, 161, 134, 105, 160, 165],
    actualData: [120, 82, 91, 154, 162, 140, 145]
  },
  messages: {
    expectedData: [200, 192, 120, 144, 160, 130, 140],
    actualData: [180, 160, 151, 106, 145, 150, 130]
  },
  purchases: {
    expectedData: [80, 100, 121, 104, 105, 90, 100],
    actualData: [120, 90, 100, 138, 142, 130, 130]
  },
  shoppings: {
    expectedData: [130, 140, 141, 142, 145, 150, 160],
    actualData: [120, 82, 91, 154, 162, 140, 130]
  }
}

export default {
  name: 'Dashboard',
  components: {
    Pipeline,
    GithubCorner,
    PanelGroup,
    LineChart,
    RadarChart,
    PieChart,
    BarChart
  },
  data() {
    return {
      swaggerApi: store.getters.swaggerApi,
      lineChartData: lineChartData.newVisitis,
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
  },
  mounted() {
    // setTimeout(() => {
    //   KitMessage.Info('KitMessage Info')
    // }, 1000)
    // setTimeout(() => {
    //   KitMessage.Warning('KitMessage Warning')
    // }, 2000)
    // setTimeout(() => {
    //   KitMessage.Error('KitMessage Error')
    // }, 3000)
    // setTimeout(() => {
    //   KitMessage.Success('KitMessage Success')
    // }, 4000)
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: #F2F6FC;
  position: relative;

  .github-corner {
    position: absolute;
    top: 0;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
