<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>复制配置信息、代码片段或用户数据到剪贴板，提升操作效率</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法：clipboard版</h4>
    <div id="btn" @click="copy">复制</div>
    <h4>基础用法：OneKeyCopy组件版（推荐）</h4>
    <cute-one-key-copy :content.sync="msg" />
    <h4>API</h4>
    <el-table :data="apiData">
      <el-table-column prop="name" label="参数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="defaultValue" label="默认值" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
  </div>
</template>
<script>
import Clipboard from 'clipboard'
import CuteOneKeyCopy from '@/views/components/dev/CuteOneKeyCopy'
import KitMessage from '@/utils/elementui/KitMessage'
export default {
  name: 'CuteOneKeyCopyDemo',
  components: { CuteOneKeyCopy },
  data() {
    return {
      msg: '来复制我吧',
      apiData: [
        { name: 'content', remark: '待复制的内容', type: 'string', defaultValue: '-', required: '是' }
      ]
    }
  },
  methods: {
    // 复制方法
    copy() {
      const that = this
      const clipboard = new Clipboard('#btn', {
        text(trigger) {
          console.log(trigger)
          // 返回字符串
          return that.msg
        }
      })
      // 复制成功
      clipboard.on('success', (e) => {
        KitMessage.Success('复制成功')
        console.log(e, '复制成功')
        clipboard.destroy()
      })
      // //复制失败
      clipboard.on('error', (e) => {
        KitMessage.Error('复制失败')
        console.log(e, '复制失败')
        clipboard.destroy()
      })
    }
  }
}
</script>
<style lang="scss" scoped>
ul {
  padding-left: 20px;
}
.description > li{
  font-size: 12px;
}
</style>
