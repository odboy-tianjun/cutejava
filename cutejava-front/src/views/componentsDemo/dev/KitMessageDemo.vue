<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>当需要向用户展示操作结果时，如成功、失败、警告等状态反馈</li>
      <li>系统级公告，全局类警示提示，模块类状态反馈（成功、警告、错误等）</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <el-row>
      <cute-button @click="handleInfoClick">Info 提示</cute-button>
      <cute-button @click="handleWarningClick">Warning 提示</cute-button>
      <cute-button @click="handleSuccessClick">Success 提示</cute-button>
      <cute-button @click="handleErrorClick">Error 提示</cute-button>
    </el-row>
    <h4>快捷调用</h4>
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
import CuteButton from '@/views/components/dev/CuteButton.vue'
import KitMessage from '@/utils/elementui/KitMessage'

export default {
  name: 'KitMessageDemo',
  components: { CuteButton },
  data() {
    return {
      apiData: [
        { name: 'content', remark: '内容', type: 'string | VNode', defaultValue: '-', required: '否' },
        { name: 'duration', remark: '显示时间, 毫秒。设为 0 则不会自动关闭', type: 'number', defaultValue: '3000', required: '否' }
      ]
    }
  },
  methods: {
    handleInfoClick() {
      KitMessage.Info('这是一段 Info 通知提示信息')
    },
    handleWarningClick() {
      KitMessage.Warning('这是一段 Warning 通知提示信息')
    },
    handleSuccessClick() {
      KitMessage.Success('这是一段 Success 通知提示信息')
    },
    handleErrorClick() {
      KitMessage.Error('这是一段 Error 通知提示信息')
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
