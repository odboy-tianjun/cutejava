<template>
  <div
    class="box-card"
    :style="cardStyle"
  >
    <div>
      <div style="float: left;">
        <i :class="statusIconClass" :style="statusIconStyle" />
      </div>
      <div class="box-name">{{ currentTemplateData.name }}</div>
      <div style="clear: both" />
    </div>
    <div class="box-current-node">
      <a @click="onCurrentNodeClick">
        {{ currentNodeDisplayText }}
      </a>
    </div>
    <el-row>
      <el-col :span="12" style="text-align: left">
        <!-- 流水线节点状态为fail，且节点支持重试 -->
        <div v-if="showRetryButton">
          <div class="box-buttons">
            <el-button
              v-prevent-re-click="5000"
              type="text"
              size="medium"
              :style="getButtonStyle()"
              @click="onNodeRetryClick"
            >
              重试
            </el-button>
          </div>
        </div>
        <!-- 流水线节点状态为success，且有操作按钮 -->
        <div v-else-if="showOperationButtons">
          <div class="box-buttons">
            <el-button
              v-for="buttonItem in currentTemplateData.buttons"
              :key="buttonItem.service"
              size="medium"
              type="text"
              :style="getButtonStyle(buttonItem)"
              @click="onNodeOperationClick(buttonItem)"
            >
              {{ buttonItem.title }}
            </el-button>
          </div>
        </div>
        <!-- 流水线节点状态为running，且有操作按钮 -->
        <div v-else-if="showOperationButtons">
          <div class="box-buttons">
            <el-button
              v-for="buttonItem in currentTemplateData.buttons"
              :key="buttonItem.service"
              size="medium"
              type="text"
              :style="getButtonStyle(buttonItem)"
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
    <cute-preview-drawer ref="detailDrawer" :title="currentTemplateData.name">
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
import dayjs from 'dayjs'
import CutePreviewDrawer from '@/views/components/dev/CutePreviewDrawer'
import { mapGetters } from 'vuex'
import CsMessage from '@/utils/elementui/CsMessage'
import { retryPipeline } from '@/api/devops/pipelineInstance'
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
    },
    instanceData: {
      type: Object,
      required: false,
      default: function() {
        return {}
      }
    }
  },
  data() {
    return {
      currentTemplateData: {},
      currentInstanceData: {}
    }
  },
  // 动态计算
  computed: {
    ...mapGetters([
      'statusConst'
    ]),
    cardStyle() {
      const statusKey = this.currentTemplateData.currentNodeStatus || this.statusConst.pending.code
      const statusConfig = this.statusConst[statusKey] || this.statusConst.pending
      return {
        borderTop: `5px solid ${statusConfig.color}`
      }
    },
    statusIconClass() {
      const status = this.currentTemplateData.currentNodeStatus || this.statusConst.pending.code
      return this.statusConst[status].icon || 'el-icon-time'
    },
    statusIconStyle() {
      return {
        fontSize: '22px',
        color: this.getStatusColor()
      }
    },
    currentNodeDisplayText() {
      return this.currentTemplateData.currentNodeMsg || this.statusConst.pending.label
    },
    showRetryButton() {
      return this.currentTemplateData.currentNodeStatus === this.statusConst.fail.code &&
        this.currentTemplateData.retry === true
    },
    showOperationButtons() {
      const status = this.currentTemplateData.currentNodeStatus
      const hasButtons = this.currentTemplateData.buttons && this.currentTemplateData.buttons.length > 0
      return hasButtons && (
        status === this.statusConst.success.code ||
        status === this.statusConst.running.code
      )
    },
    executeTimeStr() {
      return this.renderDateTimeStr(this.currentTemplateData)
    }
  },
  watch: {
    templateData: {
      handler(newVal, oldVal) {
        this.currentTemplateData = { ...newVal }
      },
      deep: true
    },
    instanceData: {
      handler(newVal, oldVal) {
        this.currentInstanceData = { ...newVal }
      },
      deep: true
    }
  },
  mounted() {
    // 初始化时，设置流水线模板
    this.currentTemplateData = this.templateData
    this.currentInstanceData = this.instanceData
  },
  methods: {
    getStatusColor() {
      const status = this.currentTemplateData.currentNodeStatus || this.statusConst.pending.code
      const statusConfig = this.statusConst[status] || this.statusConst.pending
      return statusConfig.color
    },
    getButtonStyle(buttonItem) {
      if (buttonItem) {
        const isPositiveAction = ['apply', 'agree', 'ok', 'success'].includes(buttonItem.code)
        const status = this.currentTemplateData.currentNodeStatus
        const color = isPositiveAction
          ? this.statusConst.running.color
          : this.statusConst.fail.color
        let display = ''
        if (status === this.statusConst.running.code) {
          display = this.getRunningNodeButtonVisibleStatus(buttonItem)
        } else if (status === this.statusConst.success.code) {
          display = this.getSuccessNodeButtonVisibleStatus(buttonItem)
        }
        return {
          padding: 0,
          margin: '0 10px 0 10px',
          color,
          display
        }
      }
      return {
        padding: 0,
        margin: '0 10px 0 10px',
        color: this.statusConst.running.color
      }
    },
    /**
     * 运行中节点的按钮显示
     * @param buttonItem
     * @returns {string}
     */
    getRunningNodeButtonVisibleStatus(buttonItem) {
      if (buttonItem.type === 'qrCodeDialog') {
        return 'none'
      }
      return ''
    },
    /**
     * 成功节点的按钮显示
     * @param buttonItem
     * @returns {string}
     */
    getSuccessNodeButtonVisibleStatus(buttonItem) {
      const that = this
      // 根据按钮编码
      if (buttonItem.code === that.currentTemplateData.currentNodeStatus) {
        return ''
      }
      return 'none'
    },
    renderDateTimeStr(newVal) {
      let executeTimeStr = ''
      switch (newVal.status) {
        case 'pending':
          executeTimeStr = ''
          break
        case 'running':
        case 'success':
        case 'fail':
          if (newVal.currentNodeStatus !== this.statusConst.pending.code) {
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
      const currentTemplateData = this.currentTemplateData || { click: false }
      if (!currentTemplateData.click) {
        CsMessage.Warning('当前流水线节点不支持查看明细')
        return
      }
      console.error('templateData', this.templateData)
      this.$refs.detailDrawer.show()
    },
    onNodeRetryClick() {
      const currentTemplateData = this.currentTemplateData || { retry: false }
      if (!currentTemplateData.retry) {
        CsMessage.Warning('当前流水线节点不支持重试')
        return
      }
      const data = {
        ...this.currentInstanceData,
        code: this.currentTemplateData.code
      }
      // console.error('data', data)
      retryPipeline(data)
      this.$emit('retry', data)
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
