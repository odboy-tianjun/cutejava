<template>
  <div
    class="box-card"
    :style="{
      borderTopWidth: '5px',
      borderTopStyle: 'solid',
      borderTopColor: statusColorConst[pipelineData.currentNodeStatus ? pipelineData.currentNodeStatus : statusColorConst.pending.code].color
    }"
  >
    <div>
      <div style="float: left;">
        <i
          v-if="pipelineData.currentNodeStatus && pipelineData.currentNodeStatus === statusColorConst.pending.code"
          class="el-icon-time"
          :style="{
            fontSize: '22px',
            color: statusColorConst.pending.color
          }"
        />
        <i
          v-else-if="pipelineData.currentNodeStatus && pipelineData.currentNodeStatus === statusColorConst.running.code"
          class="el-icon-loading"
          :style="{
            fontSize: '22px',
            color: statusColorConst.running.color
          }"
        />
        <i
          v-else-if="pipelineData.currentNodeStatus && pipelineData.currentNodeStatus === statusColorConst.success.code"
          class="el-icon-success"
          :style="{
            fontSize: '22px',
            color: statusColorConst.success.color
          }"
        />
        <i
          v-else-if="pipelineData.currentNodeStatus && pipelineData.currentNodeStatus === statusColorConst.fail.code"
          class="el-icon-error"
          :style="{
            fontSize: '22px',
            color: statusColorConst.fail.color
          }"
        />
        <i
          v-else
          class="el-icon-time"
          :style="{
            fontSize: '22px',
            color: statusColorConst.pending.color
          }"
        />
      </div>
      <div class="box-name">{{ pipelineData.name }}</div>
      <div style="clear: both" />
    </div>
    <div class="box-current-node">
      <a @click="onCurrentNodeClick">
        {{ pipelineData.currentNodeMsg ? pipelineData.currentNodeMsg : statusColorConst.pending.label }}
      </a>
    </div>
    <el-row>
      <el-col :span="12" style="text-align: left">
        <!-- 流水线状态为fail，且节点支持重试 -->
        <div v-if="pipelineData.currentNodeStatus && pipelineData.currentNodeStatus === 'fail' && pipelineData.retry === true">
          <div class="box-buttons">
            {{ '1' }}
            <el-button
              type="text"
              size="medium"
              :style="{
                padding: 0,
                margin: '0 10px 0 10px',
                color: statusColorConst.running.color
              }"
              @click="onNodeRetryClick"
            >
              重试
            </el-button>
          </div>
        </div>
        <!-- 流水线状态为success，且有需要满足条件的操作按钮 -->
        <div v-else-if="pipelineData.currentNodeStatus && pipelineData.currentNodeStatus === statusColorConst.success.code && pipelineData.buttons && pipelineData.buttons.length > 0">
          <div class="box-buttons">
            {{ '2' }}
            <el-button
              v-for="buttonItem in pipelineData.buttons"
              :key="buttonItem.service"
              size="medium"
              type="text"
              :style="{
                padding: 0,
                margin: '0 10px 0 10px',
                color: ['apply','agree','ok','success'].includes(buttonItem.code) ? statusColorConst.running.color : statusColorConst.fail.color,
                display: ['success', 'fail'].includes(buttonItem.code) && pipelineData.status === buttonItem.code ? '' : 'none'
              }"
              @click="onNodeOperationClick(buttonItem)"
            >
              {{ buttonItem.title }}
            </el-button>
          </div>
        </div>
        <!-- 流水线状态非pending，且有操作按钮 -->
        <div v-else-if="pipelineData.currentNodeStatus && pipelineData.currentNodeStatus !== statusColorConst.pending.code && pipelineData.buttons && pipelineData.buttons.length > 0">
          <div class="box-buttons">
            {{ '3' }}
            <el-button
              v-for="buttonItem in pipelineData.buttons"
              :key="buttonItem.service"
              size="medium"
              type="text"
              :style="{
                padding: 0,
                margin: '0 10px 0 10px',
                color: ['apply','agree','ok','success'].includes(buttonItem.code) ? statusColorConst.running.color : statusColorConst.fail.color
              }"
              @click="onNodeOperationClick(buttonItem)"
            >
              {{ buttonItem.title }}
            </el-button>
          </div>
        </div>
        <!-- 占位用，为了前端样式美观些 -->
        <div v-else>
          <div style="width: 110px;height: 30px" />
        </div>
      </el-col>
      <el-col :span="12">
        <div class="box-execute-time">
          {{ executeTimeStr }}
        </div>
      </el-col>
      <div style="clear: both" />
    </el-row>
    <cute-preview-drawer ref="detailDrawer" :title="pipelineData.name">
      <div style="padding: 20px">
        这里是明细
      </div>
    </cute-preview-drawer>
  </div>
</template>

<script>
import dayjs from 'dayjs'
import CutePreviewDrawer from '@/views/components/dev/CutePreviewDrawer'

export default {
  name: 'CutePipelineNode',
  components: { CutePreviewDrawer },
  props: {
    templateData: {
      type: Object,
      required: true,
      default: function() {
        return {}
      }
    }
  },
  data() {
    return {
      statusColorConst: {
        pending: { code: 'pending', label: '未开始', color: '#C0C4CC' },
        running: { code: 'running', label: '运行中', color: '#409EFF' },
        success: { code: 'success', label: '执行成功', color: '#67C23A' },
        fail: { code: 'fail', label: '执行失败', color: '#F56C6C' }
      },
      executeTimeStr: '',
      pipelineData: {}
    }
  },
  watch: {
    templateData: {
      handler(newVal, oldVal) {
        this.pipelineData = { ...newVal }
        this.executeTimeStr = this.renderDateTimeStr(newVal)
      },
      deep: true
    }
  },
  mounted() {
    this.pipelineData = this.templateData
    this.executeTimeStr = this.renderDateTimeStr(this.templateData)
  },
  methods: {
    renderDateTimeStr(newVal) {
      let executeTimeStr = ''
      switch (newVal.status) {
        case 'pending':
          executeTimeStr = ''
          break
        case 'running':
        case 'success':
        case 'fail':
          if (newVal.currentNodeStatus !== this.statusColorConst.pending.code) {
            if (newVal.startTime && newVal.finishTime) {
              executeTimeStr = this.formatTimeDifference(newVal.startTime, newVal.finishTime)
              return executeTimeStr
            }
            if (newVal.startTime) {
              executeTimeStr = this.formatTimeDifference(newVal.startTime, new Date())
              return executeTimeStr
            }
            if (newVal.createTime) {
              executeTimeStr = this.formatTimeDifference(newVal.createTime, new Date())
              return executeTimeStr
            }
          }
          break
        default:
          executeTimeStr = ''
          break
      }
      return executeTimeStr
    },
    formatTimeDifference(createTimeStr, updateTimeStr) {
      const startTime = dayjs(createTimeStr)
      const endTime = dayjs(updateTimeStr)
      // 计算两个时间点之间的差异（毫秒）
      const diffInMilliseconds = endTime.diff(startTime)
      // 将毫秒转换为秒
      let totalSeconds = Math.floor(diffInMilliseconds / 1000)
      const days = Math.floor(totalSeconds / (3600 * 24))
      totalSeconds %= 3600 * 24
      const hours = Math.floor(totalSeconds / 3600)
      totalSeconds %= 3600
      const minutes = Math.floor(totalSeconds / 60)
      const seconds = totalSeconds % 60
      // 格式化为字符串
      if (days > 0) {
        return `${days} 天 ${hours} 时 ${minutes} 分 ${seconds} 秒`
      }
      if (hours > 0) {
        return `${hours} 时 ${minutes} 分 ${seconds} 秒`
      }
      if (minutes > 0) {
        return `${minutes} 分 ${seconds} 秒`
      }
      if (seconds >= 0) {
        return `${seconds} 秒`
      }
      return `${diffInMilliseconds} 毫秒`
    },
    onCurrentNodeClick() {
      const pipelineData = this.pipelineData || { click: false }
      if (!pipelineData.click) {
        console.warn('当前流水线节点不支持查看明细')
        return
      }
      console.error('templateData', this.templateData)
      this.$refs.detailDrawer.show()
    },
    onNodeRetryClick() {
      const pipelineData = this.pipelineData || { retry: false }
      if (!pipelineData.retry) {
        console.warn('当前流水线节点不支持重试')
        return
      }
      console.error('pipelineData', this.pipelineData)
    },
    onNodeOperationClick(buttonInfo) {
      console.error('buttonInfo', buttonInfo)
    }
  }
}
</script>
<style scoped>
.box-card {
  width: 300px; /* 卡片宽度 */
  max-width: 300px; /* 卡片宽度 */
  padding: 10px;
  background-color: #fff; /* 背景颜色 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  border-radius: 8px; /* 圆角效果 */
  overflow: hidden; /* 防止子元素溢出 */
  transition: transform 0.3s ease, box-shadow 0.3s ease; /* 过渡效果 */
  display: inline-block;
  margin-right: 30px;
}

.box-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
}

.box-name {
  float: left;
  margin-left: 5px;
  font-size: 16px;
  line-height: 22px
}

.box-current-node {
  padding: 20px;
  overflow: hidden;
  text-overflow: ellipsis;
  text-wrap: nowrap;
  font-size: 15px;
  text-align: center;
}

.box-buttons {
  padding: 5px;
  overflow: hidden;
  text-align: center;
  height: 30px;
}

.box-execute-time {
  text-align: right;
  font-size: 12px;
  height: 30px;
  line-height: 30px;
}
</style>
