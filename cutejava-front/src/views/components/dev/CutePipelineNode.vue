<template>
  <div
    class="box-card"
    :style="{
      borderTopWidth: '5px',
      borderTopStyle: 'solid',
      borderTopColor: statusColorConst[templateData.status].color
    }"
  >
    <div>
      <div style="float: left;">
        <i
          v-if="templateData.currentNodeStatus === 'pending'"
          class="el-icon-time"
          :style="{
            fontSize: '22px',
            color: statusColorConst.pending.color
          }"
        />
        <i
          v-else-if="templateData.currentNodeStatus === 'running'"
          class="el-icon-loading"
          :style="{
            fontSize: '22px',
            color: statusColorConst.running.color
          }"
        />
        <i
          v-else-if="templateData.currentNodeStatus === 'success'"
          class="el-icon-success"
          :style="{
            fontSize: '22px',
            color: statusColorConst.success.color
          }"
        />
        <i
          v-else-if="templateData.currentNodeStatus === 'fail'"
          class="el-icon-error"
          :style="{
            fontSize: '22px',
            color: statusColorConst.fail.color
          }"
        />
        <i
          v-else
          class="el-icon-error"
          :style="{
            fontSize: '22px',
            color: statusColorConst.fail.color
          }"
        />
      </div>
      <div class="box-name">{{ templateData.name }}</div>
      <div style="clear: both" />
    </div>
    <div class="box-current-node">
      <a @click="onCurrentNodeClick">
        {{ templateData.currentNode }}
      </a>
    </div>
    <el-row>
      <el-col :span="12" style="text-align: left">
        <!-- 流水线状态为fail，且节点支持重试 -->
        <div v-if="templateData.status === 'fail' && templateData.retry === true">
          <div class="box-buttons">
            <el-button
              type="text"
              size="medium"
              :style="{
                padding: 0,
                margin: '0 10px 0 10px',
                color: statusColorConst.success.color
              }"
              @click="onNodeRetryClick"
            >
              重试
            </el-button>
          </div>
        </div>
        <!-- 流水线状态为running，且有操作按钮 -->
        <div v-else-if="templateData.status === 'running' && templateData.buttons && templateData.buttons.length > 0">
          <div class="box-buttons">
            <el-button
              v-for="buttonItem in templateData.buttons"
              :key="buttonItem.service"
              size="medium"
              type="text"
              :style="{
                padding: 0,
                margin: '0 10px 0 10px',
                color: ['apply','agree','ok','success'].includes(buttonItem.code) ? 'green' : 'red'
              }"
              @click="onNodeOperationClick(buttonItem)"
            >
              {{ buttonItem.title }}
            </el-button>
          </div>
        </div>
        <!-- 流水线状态为success，且有需要满足条件的操作按钮 -->
        <div v-else-if="templateData.status === 'success' && templateData.buttons && templateData.buttons.length > 0">
          <div class="box-buttons">
            <el-button
              v-for="buttonItem in templateData.buttons"
              :key="buttonItem.service"
              size="medium"
              type="text"
              :style="{
                padding: 0,
                margin: '0 10px 0 10px',
                color: ['apply','agree','ok','success'].includes(buttonItem.code) ? 'green' : 'red',
                display: ['success', 'fail'].includes(buttonItem.code) && templateData.status === buttonItem.code ? '' : 'none'
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
    <cute-preview-drawer ref="detailDrawer" :title="templateData.name">
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
        pending: { label: '未开始', color: '#C0C4CC' },
        running: { label: '运行中', color: '#67C23A' },
        success: { label: '执行成功', color: '#67C23A' },
        fail: { label: '执行失败', color: '#F56C6C' }
      },
      executeTimeStr: ''
    }
  },
  watch: {
    templateData: {
      handler(newVal, oldVal) {
        this.executeTimeStr = this.renderDateTimeStr(newVal)
      },
      deep: true
    }
  },
  // computed: {
  //   formatExecuteTimeStr() {
  //     return this.renderDateTimeStr(this.templateData)
  //   }
  // },
  mounted() {
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
          executeTimeStr = this.formatTimeDifference(newVal.startTime, newVal.updateTime)
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
    },
    onCurrentNodeClick() {
      const templateData = this.templateData || { click: false }
      if (!templateData.click) {
        console.warn('当前流水线节点不支持查看明细')
        return
      }
      console.error('templateData', this.templateData)
      this.$refs.detailDrawer.show()
    },
    onNodeRetryClick() {
      const templateData = this.templateData || { retry: false }
      if (!templateData.retry) {
        console.warn('当前流水线节点不支持重试')
        return
      }
      console.error('templateData', this.templateData)
    },
    onNodeOperationClick(buttonInfo) {
      console.error('buttonInfo', buttonInfo)
    }
  }
}
</script>
<style scoped>
.box-card {
  width: 240px; /* 卡片宽度 */
  max-width: 240px; /* 卡片宽度 */
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
  font-size: 18px;
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
