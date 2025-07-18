<template>
  <div>
    <el-divider>演示：运行中</el-divider>
    <div class="box-pipeline">
      <div v-if="dataList1 && dataList1.length > 0" class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in dataList1"
          :key="template.code"
          :template-data="dataList1[index]"
        />
      </div>
      <div v-else class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in templateList"
          :key="template.code"
          :template-data="dataList1[index]"
        />
      </div>
    </div>
    <el-divider>演示：成功</el-divider>
    <div class="box-pipeline">
      <div v-if="dataList2 && dataList2.length > 0" class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in dataList2"
          :key="template.code"
          :template-data="dataList2[index]"
        />
      </div>
      <div v-else class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in templateList"
          :key="template.code"
          :template-data="dataList2[index]"
        />
      </div>
    </div>
    <el-divider>演示：失败或重试</el-divider>
    <div class="box-pipeline">
      <div v-if="dataList3 && dataList3.length > 0" class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in dataList3"
          :key="template.code"
          :template-data="dataList3[index]"
        />
      </div>
      <div v-else class="box-pipeline-content">
        <cute-pipeline-node
          v-for="(template, index) in templateList"
          :key="template.code"
          :template-data="dataList3[index]"
        />
      </div>
    </div>
  </div>
</template>

<script>

import CutePipelineNode from '@/views/components/dev/CutePipelineNode'
import { formatDateTimeStr } from '@/utils'

export default {
  name: 'CutePipelineNodeDemo',
  components: { CutePipelineNode },
  data() {
    return {
      templateList: [
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
      // 运行中
      dataList1: [
        {
          code: 'node_init',
          type: 'service',
          name: '初始化',
          createTime: '2025-07-17 21:12:01',
          updateTime: '2025-07-17 21:13:01',
          startTime: '2025-07-17 21:13:01',
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          updateTime: '2025-07-17 21:28:21',
          startTime: '2025-07-17 21:28:21',
          currentNode: '等待人工审批',
          currentNodeStatus: 'running',
          status: 'running'
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
          currentNode: '待执行',
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
          currentNode: '待执行',
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
          currentNode: '待执行',
          currentNodeStatus: 'pending',
          status: 'pending'
        }
      ],
      // 成功
      dataList2: [
        {
          code: 'node_init',
          type: 'service',
          name: '初始化',
          createTime: '2025-07-17 21:12:01',
          updateTime: '2025-07-17 21:13:01',
          startTime: '2025-07-17 21:13:01',
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行成功',
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
          currentNode: '执行失败',
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
          currentNode: '待执行',
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
          currentNode: '待执行',
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
          currentNode: '待执行',
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
          currentNode: '待执行',
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
          currentNode: '待执行',
          currentNodeStatus: 'pending',
          status: 'pending'
        }
      ]
    }
  },
  mounted() {
    setInterval(this.refreshData, 1000)
  },
  methods: {
    refreshData() {
      // 运行中
      for (const datum of this.dataList1) {
        if (datum.status === 'running') {
          datum.updateTime = formatDateTimeStr(new Date())
        }
      }
      this.dataList1 = [...this.dataList1]
      // 成功
      for (const datum of this.dataList2) {
        if (datum.status === 'running') {
          datum.updateTime = formatDateTimeStr(new Date())
        }
      }
      this.dataList2 = [...this.dataList2]
      // 失败或重试
      for (const datum of this.dataList3) {
        if (datum.status === 'running') {
          datum.updateTime = formatDateTimeStr(new Date())
        }
      }
      this.dataList3 = [...this.dataList3]
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

