<template>
  <div>
    <el-divider>演示：动态流水线
      <el-button
        v-prevent-re-click
        type="primary"
        :loading="dynamicStartButtonLoading"
        :disabled="dynamicStartButtonLoading"
        @click="startPipelineTest"
      >{{ dynamicStartupStatusMap[dynamicStartupStatus].label }}
      </el-button>
    </el-divider>
    <div class="box-pipeline">
      <div v-if="dynamicInstance.nodes && dynamicInstance.nodes.length > 0" class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in dynamicInstance.nodes"
          :key="template.code"
          :template-data.sync="dynamicInstance.nodes[index]"
        />
      </div>
      <div v-else class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in dynamicTemplate"
          :key="template.code"
          :template-data.sync="dynamicTemplate[index]"
        />
      </div>
    </div>
    <el-divider>演示：成功</el-divider>
    <div class="box-pipeline">
      <div v-if="dataList2 && dataList2.length > 0" class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in dataList2"
          :key="template.code"
          :template-data.sync="dataList2[index]"
        />
      </div>
      <div v-else class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in defaultTemplate"
          :key="template.code"
          :template-data.sync="defaultTemplate[index]"
        />
      </div>
    </div>
    <el-divider>演示：失败或重试</el-divider>
    <div class="box-pipeline">
      <div v-if="dataList3 && dataList3.length > 0" class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in dataList3"
          :key="template.code"
          :template-data.sync="dataList3[index]"
        />
      </div>
      <div v-else class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in defaultTemplate"
          :key="template.code"
          :template-data.sync="defaultTemplate[index]"
        />
      </div>
    </div>
  </div>
</template>

<script>

import CutePipelineNode from '@/views/components/dev/CutePipelineNode'
import { getPipelineTemplate } from '@/api/devops/pipelineTemplate'
import { queryLastPipelineDetail, queryLastPipelineDetailWs, startPipeline } from '@/api/devops/pipelineInstance'
import { CountArraysObjectByPropKey, FormatDateTimeStr } from '@/utils/CsUtil'
import CsMessage from '@/utils/elementui/CsMessage'
import CsWsClient from '@/utils/CsWsClient'
import { mapGetters } from 'vuex'

export default {
  name: 'CutePipelineNodeDemo',
  components: { CutePipelineNode },
  data() {
    return {
      // 静态模板
      defaultTemplate: [
        {
          code: 'node_init',
          type: 'service',
          name: '初始化'
        },
        {
          code: 'node_merge_branch',
          type: 'service',
          name: '合并代码',
          click: true,
          retry: true
        },
        {
          code: 'node_build_java',
          type: 'service',
          name: '构建',
          click: true,
          retry: true,
          detailType: 'gitlab',
          parameters: {
            pipeline: 'pipeline-backend'
          }
        },
        {
          code: 'node_image_scan',
          type: 'service',
          name: '镜像扫描',
          click: true,
          retry: true,
          detailType: 'gitlab',
          parameters: {
            pipeline: 'pipeline-image-scan'
          }
        },
        {
          code: 'node_approve_deploy',
          type: 'service',
          name: '部署审批',
          click: false,
          retry: true,
          buttons: [{
            type: 'execute',
            title: '同意',
            code: 'agree',
            parameters: {}
          },
          {
            type: 'execute',
            title: '拒绝',
            code: 'refuse',
            parameters: {}
          }
          ]
        },
        {
          code: 'node_deploy_java',
          type: 'service',
          name: '部署',
          click: true,
          retry: false,
          buttons: [{
            type: 'link',
            title: '查看部署详情',
            code: 'success',
            parameters: {
              'isBlank': true
            }
          }]
        },
        {
          code: 'node_merge_confirm',
          type: 'service',
          name: '合并确认',
          click: false,
          retry: false,
          buttons: [{
            type: 'execute',
            title: '通过',
            code: 'agree',
            parameters: {}
          }]
        },
        {
          code: 'node_merge_master',
          type: 'service',
          name: '合并回Master',
          click: false,
          retry: true
        }],
      // 成功
      dataList2: [
        {
          code: 'node_init',
          type: 'service',
          name: '初始化',
          createTime: '2025-07-17 21:12:01',
          updateTime: '2025-07-17 21:13:01',
          startTime: '2025-07-17 21:13:01',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        },
        {
          code: 'node_merge_branch',
          type: 'service',
          name: '合并代码',
          click: true,
          retry: true,
          createTime: '2025-07-17 21:13:01',
          updateTime: '2025-07-17 21:18:01',
          startTime: '2025-07-17 21:13:01',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        },
        {
          code: 'node_build_java',
          type: 'service',
          name: '构建',
          click: true,
          retry: true,
          detailType: 'gitlab',
          parameters: {
            pipeline: 'pipeline-backend'
          },
          createTime: '2025-07-17 21:18:01',
          updateTime: '2025-07-17 21:28:01',
          startTime: '2025-07-17 21:18:01',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        },
        {
          code: 'node_image_scan',
          type: 'service',
          name: '镜像扫描',
          click: true,
          retry: true,
          detailType: 'gitlab',
          parameters: {
            pipeline: 'pipeline-image-scan'
          },
          createTime: '2025-07-17 21:28:01',
          updateTime: '2025-07-17 21:28:21',
          startTime: '2025-07-17 21:28:01',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        },
        {
          code: 'node_approve_deploy',
          type: 'service',
          name: '部署审批',
          click: false,
          retry: true,
          buttons: [
            {
              type: 'execute',
              title: '同意',
              code: 'agree',
              parameters: {}
            },
            {
              type: 'execute',
              title: '拒绝',
              code: 'refuse',
              parameters: {}
            }
          ],
          createTime: '2025-07-17 21:28:21',
          updateTime: '2025-07-17 21:30:21',
          startTime: '2025-07-17 21:28:21',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        },
        {
          code: 'node_deploy_java',
          type: 'service',
          name: '部署',
          click: true,
          retry: false,
          buttons: [{
            type: 'link',
            title: '查看部署详情',
            code: 'success',
            parameters: {
              'isBlank': true
            }
          }],
          createTime: '2025-07-17 21:28:21',
          updateTime: '2025-07-17 21:58:21',
          startTime: '2025-07-17 21:30:21',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        },
        {
          code: 'node_merge_confirm',
          type: 'service',
          name: '合并确认',
          click: false,
          retry: false,
          buttons: [{
            type: 'execute',
            title: '通过',
            code: 'agree',
            parameters: {}
          }],
          createTime: '2025-07-17 21:28:21',
          updateTime: '2025-07-17 22:10:21',
          startTime: '2025-07-17 21:58:21',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        },
        {
          code: 'node_merge_master',
          type: 'service',
          name: '合并回Master',
          click: false,
          retry: true,
          createTime: '2025-07-17 21:28:21',
          updateTime: '2025-07-17 22:10:58',
          startTime: '2025-07-17 22:10:21',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        }
      ],
      // 失败或重试
      dataList3: [
        {
          code: 'node_init',
          type: 'service',
          name: '初始化',
          createTime: '2025-07-17 21:12:01',
          updateTime: '2025-07-17 21:13:01',
          startTime: '2025-07-17 21:13:01',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        },
        {
          code: 'node_merge_branch',
          type: 'service',
          name: '合并代码',
          click: true,
          retry: true,
          createTime: '2025-07-17 21:13:01',
          updateTime: '2025-07-17 21:18:01',
          startTime: '2025-07-17 21:13:01',
          currentNodeMsg: '执行成功',
          currentNodeStatus: 'success',
          status: 'success'
        },
        {
          code: 'node_build_java',
          type: 'service',
          name: '构建',
          click: true,
          retry: true,
          detailType: 'gitlab',
          parameters: {
            pipeline: 'pipeline-backend'
          },
          createTime: '2025-07-17 21:18:01',
          updateTime: '2025-07-17 21:28:01',
          startTime: '2025-07-17 21:18:01',
          currentNodeMsg: '执行失败',
          currentNodeStatus: 'fail',
          status: 'fail'
        },
        {
          code: 'node_image_scan',
          type: 'service',
          name: '镜像扫描',
          click: true,
          retry: true,
          detailType: 'gitlab',
          parameters: {
            pipeline: 'pipeline-image-scan'
          },
          createTime: '2025-07-17 21:28:01',
          updateTime: '2025-07-17 21:28:21',
          startTime: null,
          currentNodeMsg: '待执行',
          currentNodeStatus: 'pending',
          status: 'pending'
        },
        {
          code: 'node_approve_deploy',
          type: 'service',
          name: '部署审批',
          click: false,
          retry: true,
          buttons: [
            {
              type: 'execute',
              title: '同意',
              code: 'agree',
              parameters: {}
            },
            {
              type: 'execute',
              title: '拒绝',
              code: 'refuse',
              parameters: {}
            }
          ],
          createTime: '2025-07-17 21:28:21',
          updateTime: '2025-07-17 21:28:21',
          startTime: null,
          currentNodeMsg: '待执行',
          currentNodeStatus: 'pending',
          status: 'pending'
        },
        {
          code: 'node_deploy_java',
          type: 'service',
          name: '部署',
          click: true,
          retry: false,
          buttons: [{
            type: 'link',
            title: '查看部署详情',
            code: 'success',
            parameters: {
              'isBlank': true
            }
          }],
          createTime: '2025-07-17 21:28:21',
          updateTime: '2025-07-17 21:28:21',
          startTime: null,
          currentNodeMsg: '待执行',
          currentNodeStatus: 'pending',
          status: 'pending'
        },
        {
          code: 'node_merge_confirm',
          type: 'service',
          name: '合并确认',
          click: false,
          retry: false,
          buttons: [{
            type: 'execute',
            title: '通过',
            code: 'agree',
            parameters: {}
          }],
          createTime: '2025-07-17 21:28:21',
          updateTime: '2025-07-17 21:28:21',
          startTime: null,
          currentNodeMsg: '待执行',
          currentNodeStatus: 'pending',
          status: 'pending'
        },
        {
          code: 'node_merge_master',
          type: 'service',
          name: '合并回Master',
          click: false,
          retry: true,
          createTime: '2025-07-17 21:28:21',
          updateTime: '2025-07-17 21:28:21',
          startTime: null,
          currentNodeMsg: '待执行',
          currentNodeStatus: 'pending',
          status: 'pending'
        }
      ],
      // 动态流水线
      dynamicStartupStatus: 'start',
      dynamicStartupStatusMap: {
        start: { label: '启动', code: 'start' },
        restart: { label: '重新启动', code: 'restart' }
      },
      dynamicStartButtonLoading: false,
      dynamicTemplate: [],
      dynamicHook: null,
      dynamicInstance: {},
      dynamicWsClient: null
    }
  },
  mounted() {
    this.refreshData()
    this.initPipelineTemplate(4)
    // const pipelineInstanceId = sessionStorage.getItem('pipelineInstanceId')
    // if (pipelineInstanceId) {
    //   this.fetchLastDetail(pipelineInstanceId)
    // }
    // this.connectWebSocketServer(pipelineInstanceId)
  },
  computed: {
    ...mapGetters([
      'user'
    ])
  },
  methods: {
    refreshData() {
      // 成功
      for (const datum of this.dataList2) {
        if (datum.status === 'running') {
          datum.updateTime = FormatDateTimeStr(new Date())
        }
      }
      this.dataList2 = [...this.dataList2]
      // 失败或重试
      for (const datum of this.dataList3) {
        if (datum.status === 'running') {
          datum.updateTime = FormatDateTimeStr(new Date())
        }
      }
      this.dataList3 = [...this.dataList3]
    },
    /**
     * 初始化流水线模板
     * @param templateId
     * @returns {Promise<void>}
     */
    async initPipelineTemplate(templateId) {
      const pipelineTemplate = await getPipelineTemplate(templateId)
      if (pipelineTemplate && pipelineTemplate.template) {
        try {
          this.dynamicTemplate = JSON.parse(pipelineTemplate.template)
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
      // console.error('来自服务器的数据:data', data)
      const callbackData = JSON.parse(data)
      // console.error('callbackData', callbackData)
      try {
        that.dynamicInstance = JSON.parse(callbackData.data)
        // 判断流水线是否结束
        const successCount = CountArraysObjectByPropKey(that.dynamicInstance.nodes, 'status', 'success')
        if (that.dynamicTemplate && that.dynamicTemplate.length === successCount) {
          // 所有节点执行成功
          if (that.dynamicWsClient) {
            that.dynamicWsClient.close()
          }
          that.dynamicStartupStatus = that.dynamicStartupStatusMap.start.code
        } else {
          const failCount = CountArraysObjectByPropKey(that.dynamicInstance.nodes, 'status', 'fail')
          // 存在失败的节点
          if (that.dynamicWsClient && failCount > 0) {
            that.dynamicWsClient.close()
          }
          that.dynamicStartupStatus = that.dynamicStartupStatusMap.restart.code
        }
      } catch (e) {
        // ignore
      }
    },
    /**
     * 通过Http的方式拉取流水线最新的状态
     * @param instanceId
     * @returns {Promise<void>}
     */
    async fetchLastDetail(instanceId) {
      const that = this
      const intervalTime = 2000
      setTimeout(() => {
        that.dynamicHook = setInterval(() => {
          queryLastPipelineDetail(instanceId).then((data) => {
            that.dynamicInstance = data
            // 判断流水线是否结束
            const successCount = CountArraysObjectByPropKey(data.nodes, 'status', 'success')
            if (that.dynamicTemplate && that.dynamicTemplate.length === successCount) {
              clearInterval(that.dynamicHook)
              that.dynamicHook = null
              that.dynamicStartupStatus = that.dynamicStartupStatusMap.start.code
            } else {
              that.dynamicStartupStatus = that.dynamicStartupStatusMap.restart.code
            }
          })
        }, intervalTime)
        that.dynamicStartButtonLoading = false
      }, 2000)
    },
    /**
     * 启动流水线
     * @returns {Promise<void>}
     */
    async startPipelineTest() {
      const that = this
      try {
        if (that.dynamicHook) {
          clearInterval(that.dynamicHook)
        }
        that.dynamicStartButtonLoading = true
        // 这里的 dynamicStartupStatus状态，需要后期与上下文关联后，初始化的时候带入最近一个流水线实例
        // let result
        // if (that.dynamicStartupStatus === that.dynamicStartupStatusMap.start.code) {
        //   result = await restartPipeline()
        //   CsMessage.Success('流水线重新启动成功')
        // } else {
        //   result = await startPipeline()
        //   CsMessage.Success('流水线启动成功')
        // }
        const result = await startPipeline()
        CsMessage.Success('流水线启动成功')
        // await that.fetchLastDetail(result.instanceId)
        sessionStorage.setItem('pipelineInstanceId', result.instanceId)
        that.connectWebSocketServer(result.instanceId)
      } catch (e) {
        console.error('error', e)
        that.dynamicStartButtonLoading = false
      }
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

