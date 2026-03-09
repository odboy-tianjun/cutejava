<template>
  <div :style="taskWindowStyle">
    <div :style="taskHeaderStyle" />
    <div :style="taskBodyStyle">
      <!-- 标题和图标 -->
      <el-row :gutter="2">
        <el-col :span="16">{{ name }}</el-col>
        <el-col :span="8" style="text-align: right">
          <i :class="getStatusIconClassByStatus(status)" :style="getStatusIconColorByStatus(status)" />
        </el-col>
      </el-row>
      <!-- 分割线 -->
      <div style="padding-left: 10px; padding-right: 10px; padding-top: 2px; border-bottom: 1px solid #EBEEF5" />
      <!-- 任务执行信息 -->
      <div
        v-if="click === false"
        style="width: 100%; height: 40px; line-height: 30px; font-size: 12px; color: #606266;
      text-align: center;padding: 9px 0;font-family: Arial, sans-serif"
      >
        {{ desc }}
      </div>
      <el-button
        v-else
        v-prevent-re-click
        type="text"
        style="width: 100%; height: 40px; line-height: 30px; font-size: 12px; color: #606266"
        @click="onTaskDetailClick"
      >
        {{ desc }}
      </el-button>
      <el-row :gutter="2">
        <!-- 持续时间 -->
        <el-col :span="15" style="font-size: 12px;color: #606266;height: 32px;max-height: 32px;line-height: 32px">
          {{ FormatDuration(status !== statusMap.running ? durationMillis : new Date().getTime() - startTimeMillis) }}
        </el-col>
        <!-- 任务失败，且支持重试 -->
        <el-col v-if="status === statusMap.failed && retry === true" :span="9" style="font-size: 12px;color: #606266;height: 32px;max-height: 32px">
          <el-button v-prevent-re-click type="text" @click="onRetryClick">重试</el-button>
        </el-col>
        <!-- 任务运行中，且包含按钮组 -->
        <el-col v-if="buttonList && buttonList.length > 0" :span="9" style="font-size: 12px;color: #606266;height: 32px;max-height: 32px;padding-right: 5px">
          <el-button
            v-for="(item, index) in buttonList"
            :key="index"
            v-prevent-re-click
            type="text"
            :style="`color: ${item.color ? item.color : statusColorMap.running};display: ${item.condition ===
              'all' ?
                'inline-block':
                (item.condition === status ? 'inline-block': 'none')};overflow: hidden;text-overflow: ellipsis;`"
            @click="onButtonNodeClick(item)"
          >{{ item.text }}</el-button>
        </el-col>
      </el-row>
    </div>
    <!-- 任务节点明细 -->
    <cute-preview-drawer ref="taskDetailDrawer" :title="name">
      <el-timeline>
        <el-timeline-item
          v-for="(taskDetail, index) in taskDetails"
          :key="index"
          :color="getColorHexByStatus(taskDetail.status)"
          :timestamp="taskDetail.timestamp"
        >
          {{ taskDetail.content }}
        </el-timeline-item>
      </el-timeline>
    </cute-preview-drawer>
  </div>
</template>

<script>

import KitMessage from '@/utils/elementui/KitMessage'
import CutePreviewDrawer from '@/views/components/dev/CutePreviewDrawer.vue'
import { OpenWindowInCurrent, OpenWindowInNew } from '@/utils/KitUtil'
import request from '@/utils/request'
import { FormatDuration } from '@/utils/filters'

export default {
  name: 'PipeTask',
  components: { CutePreviewDrawer },
  props: {
    /**
     * 任务名称
     */
    name: {
      type: String,
      required: true
    },
    /**
     * 任务编码
     */
    code: {
      type: String,
      required: true
    },
    /**
     * 开始时间戳。单位：毫秒
     */
    startTimeMillis: {
      type: Number,
      required: false,
      default: function() {
        return new Date().getTime()
      }
    },
    /**
     * 持续时间。单位：秒
     */
    durationMillis: {
      type: Number,
      required: false,
      default: 0
    },
    /**
     * 状态
     */
    status: {
      type: String,
      required: true
    },
    /**
     * 状态（翻译）
     */
    statusDesc: {
      type: String,
      required: true
    },
    /**
     * 任务执行信息
     */
    desc: {
      type: String,
      required: true
    },
    /**
     * 是否可以点击
     */
    click: {
      type: Boolean,
      required: false,
      default: false
    },
    /**
     * 是否可以重试
     */
    retry: {
      type: Boolean,
      required: false,
      default: false
    },
    /**
     * 任务明细类型（gitlab、jenkins等）
     */
    detailType: {
      type: String,
      required: false,
      default: null
    },
    /**
     * 按钮组
     * @property type: execute（请求系统api）、link（调用三方链接跳转）
     * @property text: 按钮标题
     * @property color: 按钮颜色
     * @property condition: 符合条件才展示，当值为`all`时，非pending状态都展示
     * @property method: 当type=execute时，配置请求方式：GET/POST
     * @property requestUrl: 当type=execute时，配置请求路径和参数
     * @property linkUrl: 当type=link时，配置跳转路径和参数
     * @property isBlank: 当type=link时，判断是否在新页面中打开
     */
    buttonList: {
      type: Array,
      required: false,
      default: function() {
        return []
      }
    }
  },
  data() {
    return {
      statusMap: {
        pending: 'pending',
        running: 'running',
        success: 'success',
        failed: 'failed'
      },
      statusColorMap: {
        pending: '#EBEEF5',
        success: '#67C23A',
        failed: '#F56C6C',
        running: '#409EFF'
      },
      // 任务明细
      taskDetails: [
        // { timestamp: '2018-04-03 20:46', content: '开始初始化', status: 'success' },
        // { timestamp: '2018-04-03 20:46', content: '开始初始化', status: 'success' },
        // { timestamp: '2018-04-03 20:46', content: '开始初始化', status: 'failed' }
      ]
    }
  },
  computed: {
    taskWindowStyle() {
      const colorHex = this.getColorHexByStatus(this.status)
      return {
        width: '260px',
        maxWidth: '260px',
        height: '100px',
        maxHeight: '100px',
        border: `1px solid ${colorHex}`,
        borderRadius: '6px',
        overflow: 'hidden',
        display: 'inline-block'
      }
    },
    taskHeaderStyle() {
      const colorHex = this.getColorHexByStatus(this.status)
      return {
        width: '100%',
        height: '5px',
        backgroundColor: colorHex
      }
    },
    taskBodyStyle() {
      return {
        padding: '5px',
        fontSize: '14px',
        backgroundColor: '#FFFFFF',
        color: '#606266',
        height: '95px'
      }
    }
  },
  watch: {},
  mounted() {
  },
  methods: {
    FormatDuration,
    /**
     * 根据任务状态获取颜色Hex值
     */
    getColorHexByStatus(status) {
      const colorHex = this.statusColorMap[status]
      if (colorHex) {
        return colorHex
      }
      return this.statusColorMap.pending
    },
    /**
     * 根据任务状态渲染状态图标样式
     * @param status
     */
    getStatusIconClassByStatus(status) {
      if (this.statusMap.success === status) {
        return 'el-icon-circle-check'
      }
      if (this.statusMap.failed === status) {
        return 'el-icon-circle-close'
      }
      if (this.statusMap.running === status) {
        return 'el-icon-loading'
      }
      return 'el-icon-time'
    },
    /**
     * 根据任务状态渲染状态图标颜色
     * @param status
     */
    getStatusIconColorByStatus(status) {
      const styles = {
        width: '14px',
        height: '14px'
      }
      styles.color = this.getColorHexByStatus(status)
      return styles
    },
    onTaskDetailClick() {
      console.error('当任务卡片可点击时，查看明细记录')
      if (this.statusMap.pending === this.status) {
        KitMessage.Error('节点任务未开始')
        return
      }
      if (this.detailType === 'gitlab') {
        // 获取gitlab任务明细
      } else if (this.detailType === 'jenkins') {
        // 获取jenkins任务明细
      } else {
        // 获取业务服务明细
      }
      this.$refs.taskDetailDrawer.show()
    },
    onRetryClick() {
      console.error('当任务卡片可重试时，任务重试', this.code)
      this.$emit('retry', this.code)
    },
    async onButtonNodeClick(buttonProps) {
      console.error('当任务卡片包含按钮组时，某个按钮被单击', buttonProps)
      if (buttonProps.type === 'execute') {
        const { method, requestUrl } = buttonProps
        if (method === 'get' || method === 'post') {
          const response = await request({ url: requestUrl, method: method })
          console.error('请求系统内置接口结果：', response)
        } else {
          KitMessage.Error(`'${buttonProps.text}' 按钮异常，不支持的请求方式`)
        }
      } else if (buttonProps.type === 'link') {
        const { blank, linkUrl } = buttonProps
        if (blank) {
          OpenWindowInNew(linkUrl)
        } else {
          OpenWindowInCurrent(linkUrl)
        }
      } else {
        KitMessage.Error(`'${buttonProps.text}' 按钮异常，不支持的类型`)
      }
    }
  }
}
</script>

