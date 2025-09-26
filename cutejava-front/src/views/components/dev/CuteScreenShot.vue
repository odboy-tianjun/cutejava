<!--
 * 截屏工具
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-20
 -->
<template>
  <div>
    <div style="text-align: right">
      <el-button
        type="primary"
        icon="el-icon-camera"
        :loading="capturing"
        size="small"
        @click="capture"
      >
        截图并下载
      </el-button>
    </div>
    <div id="elementIdSelector">
      <slot />
    </div>
  </div>
</template>

<script>

import CsMessage from '@/utils/elementui/CsMessage'
import { FormatDateTimeShortStr } from '@/utils/CsUtil'
import html2canvas from 'html2canvas'

export default {
  name: 'CuteScreenShot',
  data() {
    return {
      capturing: false
    }
  },
  methods: {
    async capture() {
      this.capturing = true
      const element = document.querySelector('#elementIdSelector')
      if (!element) {
        CsMessage.Error('未找到匹配的元素，请检查选择器是否正确')
        this.capturing = false
        return
      }
      const canvas = await html2canvas(element)
      // const base64 = canvas.toDataURL('image/png')
      const link = document.createElement('a')
      link.download = `screenshot_${FormatDateTimeShortStr(new Date())}.png`
      link.href = canvas.toDataURL('image/png')
      link.click()
      this.capturing = false
    }
  }
}
</script>

