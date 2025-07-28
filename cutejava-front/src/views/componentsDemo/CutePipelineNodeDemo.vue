<template>
  <div>
    <el-divider>演示：{{ dynamicTemplate.description ? dynamicTemplate.description: '动态流水线演示' }}
      <el-button
        v-prevent-re-click
        style="margin-left: 10px"
        type="primary"
        :loading="dynamicStartButtonLoading"
        :disabled="dynamicStartButtonLoading"
        @click="doStartPipeline"
      >{{ dynamicStartupStatusMap[dynamicStartupStatus].label }}
      </el-button>
      <el-button
        v-prevent-re-click
        :disabled="!dynamicRunning"
        style="margin-left: 10px"
        type="danger"
        @click="doStopPipeline"
      >停止
      </el-button>
    </el-divider>
    <div class="box-pipeline">
      <div v-if="dynamicInstance.nodes && dynamicInstance.nodes.length > 0" class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in dynamicInstance.nodes"
          :key="template.code"
          :template-data.sync="dynamicInstance.nodes[index]"
          :instance-data.sync="dynamicInstance"
          @node-click="onPipelineNodeClick"
          @node-retry="onPipelineNodeRetryClick"
          @node-opera="onPipelineNodeOperationClick"
        />
      </div>
      <div v-else class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in dynamicTemplateList"
          :key="template.code"
          :template-data.sync="dynamicTemplateList[index]"
          :instance-data.sync="dynamicInstance"
        />
      </div>
    </div>
    <cute-preview-drawer ref="detailDrawer" :title="currentNodeData.name">
      <el-timeline>
        <el-timeline-item timestamp="2018/4/12" placement="top">
          <el-card>
            <h4>更新 Github 模板</h4>
            <p>王小虎 提交于 2018/4/12 20:46</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="2018/4/3" placement="top">
          <el-card>
            <h4>更新 Github 模板</h4>
            <p>王小虎 提交于 2018/4/3 20:46</p>
          </el-card>
        </el-timeline-item>
        <el-timeline-item timestamp="2018/4/2" placement="top">
          <el-card>
            <h4>更新 Github 模板</h4>
            <p>王小虎 提交于 2018/4/2 20:46</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
    </cute-preview-drawer>
  </div>
</template>

<script>

import CutePipelineNode from '@/views/components/dev/CutePipelineNode'
import { getPipelineTemplate } from '@/api/devops/pipelineTemplate'
import {
  queryLastPipelineDetailWs,
  restartPipeline,
  retryPipeline,
  startPipeline,
  stopPipeline
} from '@/api/devops/pipelineInstance'
import { CountArraysObjectByPropKey } from '@/utils/CsUtil'
import CsMessage from '@/utils/elementui/CsMessage'
import CsWsClient from '@/utils/CsWsClient'
import { mapGetters } from 'vuex'
import CutePreviewDrawer from '@/views/components/dev/CutePreviewDrawer.vue'

export default {
  name: 'CutePipelineNodeDemo',
  components: { CutePreviewDrawer, CutePipelineNode },
  data() {
    return {
      testAppInfo: {
        appName: 'cuteops',
        pipelineTemplateEnv: 'daily',
        iterationName: '流水线测试'
      },
      // ============== 动态流水线
      dynamicStartupStatusMap: {
        start: { label: '启动', code: 'start' },
        restart: { label: '重新启动', code: 'restart' }
      },
      // 按钮状态
      dynamicStartupStatus: 'start',
      dynamicStartButtonLoading: false,
      // 当前模板
      dynamicTemplate: {
        id: 21
      },
      // 当前流水线节点模板
      dynamicTemplateList: [],
      // dynamicHook: null,
      // 当前流水线实例
      dynamicInstance: {},
      // 当前ws客户端
      dynamicWsClient: null,
      dynamicRunning: false,
      // ============== 交互变量
      currentNodeData: {}
    }
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  mounted() {
    this.initPipelineTemplate(this.dynamicTemplate.id)
  },
  methods: {
    /**
     * 初始化流水线模板
     * @param templateId
     * @returns {Promise<void>}
     */
    async initPipelineTemplate(templateId) {
      const pipelineTemplate = await getPipelineTemplate(templateId)
      if (pipelineTemplate && pipelineTemplate.template) {
        try {
          this.dynamicTemplate = pipelineTemplate
          this.dynamicTemplateList = JSON.parse(pipelineTemplate.template)
        } catch (e) {
          // ignore
        }
      }
    },
    /**
     * 连接WebSocket服务
     */
    connectWebSocketServer(instanceId) {
      const that = this
      // {username}_{bizCode}_{contextParams}
      const sid = `${that.user.username}_FetchPipelineLastDetail_${instanceId}`
      if (that.dynamicWsClient) {
        try {
          that.dynamicWsClient.close()
        } catch (e) {
          // ignore
        }
      }
      that.dynamicWsClient = new CsWsClient(sid)
      that.dynamicWsClient.connect(that.handleWebSocketError, that.handleWebSocketMessage)
      setTimeout(() => {
        // 请求推送流水线数据
        queryLastPipelineDetailWs(sid)
        that.dynamicStartButtonLoading = false
        that.dynamicStartupStatus = that.dynamicStartupStatusMap.restart.code
      }, 1000)
    },
    handleWebSocketError(e) {
      console.error('连接WebSocket服务失败', e)
    },
    handleWebSocketMessage(e) {
      const that = this
      const data = e.data
      // console.log('来自服务器的数据:data', data)
      const callbackData = JSON.parse(data)
      that.dynamicRunning = true
      // console.log('callbackData', callbackData)
      try {
        that.dynamicInstance = JSON.parse(callbackData.data)
        // 判断流水线是否结束
        const successCount = CountArraysObjectByPropKey(that.dynamicInstance.nodes, 'status', 'success')
        if (that.dynamicTemplateList && that.dynamicTemplateList.length === successCount) {
          // 所有节点执行成功
          if (that.dynamicWsClient) {
            sessionStorage.removeItem('pipelineInstanceId')
            that.dynamicWsClient.close()
            that.dynamicRunning = false
          }
          that.dynamicStartupStatus = that.dynamicStartupStatusMap.start.code
        } else {
          const failCount = CountArraysObjectByPropKey(that.dynamicInstance.nodes, 'status', 'fail')
          // 存在失败的节点
          if (that.dynamicWsClient && failCount > 0) {
            that.dynamicWsClient.close()
            that.dynamicRunning = false
          }
          that.dynamicStartupStatus = that.dynamicStartupStatusMap.restart.code
        }
      } catch (e) {
        // ignore
        that.dynamicRunning = false
      }
    },
    /**
     * 启动流水线
     * @returns {Promise<void>}
     */
    async doStartPipeline() {
      const that = this
      try {
        that.dynamicStartButtonLoading = true
        // 这里的 dynamicStartupStatus状态，需要后期与上下文关联后，初始化的时候带入最近一个流水线实例
        const args = {
          templateId: that.dynamicTemplate.id,
          instanceName: that.testAppInfo.iterationName,
          contextName: that.testAppInfo.appName,
          env: that.testAppInfo.pipelineTemplateEnv
        }
        let result
        const pipelineInstanceId = sessionStorage.getItem('pipelineInstanceId')
        if (pipelineInstanceId) {
          result = await restartPipeline({
            ...args,
            instanceId: pipelineInstanceId
          })
          CsMessage.Success('流水线重启成功')
        } else {
          result = await startPipeline(args)
          CsMessage.Success('流水线启动成功')
        }
        sessionStorage.setItem('pipelineInstanceId', result.instanceId)
        that.connectWebSocketServer(result.instanceId)
      } catch (e) {
        console.error('error', e)
        that.dynamicStartButtonLoading = false
        CsMessage.Error('流水线启动失败')
      }
    },
    /**
     * 停止流水线
     * @returns {Promise<void>}
     */
    async doStopPipeline() {
      const that = this
      const args = {
        templateId: that.dynamicTemplate.id,
        instanceName: that.testAppInfo.iterationName,
        contextName: that.testAppInfo.appName,
        env: that.testAppInfo.pipelineTemplateEnv
      }
      const pipelineInstanceId = sessionStorage.getItem('pipelineInstanceId')
      if (pipelineInstanceId) {
        await stopPipeline({
          ...args,
          instanceId: pipelineInstanceId
        })
        CsMessage.Success('流水线停止成功')
      }
    },
    onPipelineNodeClick(data) {
      console.log('data', data)
      this.currentNodeData = data
      this.$refs.detailDrawer.show()
    },
    async onPipelineNodeRetryClick(data) {
      console.log('data', data)
      const result = await retryPipeline({
        instanceId: data.instanceId,
        code: data.code
      })
      sessionStorage.setItem('pipelineInstanceId', result.instanceId)
      this.connectWebSocketServer(data.instanceId)
    },
    async onPipelineNodeOperationClick(data, buttonInfo) {
      console.log('data', data, buttonInfo)
    }
  }
}
</script>
<style scoped>
.box-pipeline {
  width: 1500px; /* 固定宽度 */
  overflow-x: auto; /* 允许水平滚动 */
  white-space: nowrap; /* 防止内容换行，使所有内容在一行显示 */
  padding: 30px 20px 30px 20px;
}

.box-pipeline::-webkit-scrollbar {
  height: 5px; /* 设置水平滚动条高度 */
}

.box-pipeline::-webkit-scrollbar-track {
  background: #f1f1f1; /* 滚动条底色 */
}

.box-pipeline::-webkit-scrollbar-thumb {
  background: #DCDFE6; /* 滚动条颜色 */
}

.box-pipeline-content {
  display: inline-block; /* 使内容水平排列 */
  white-space: nowrap; /* 防止内容换行 */
}
</style>

