<template>
  <div>
    <h4>何时使用</h4>
    在系统四个角显示通知提醒信息。经常用于以下情况
    <ul class="description">
      <li>较为复杂的通知内容</li>
      <li>带有交互的通知，给出用户下一步的行动点</li>
      <li>系统主动推送</li>
      <li>当需要向用户展示重要信息但又不希望打断用户当前操作时</li>
      <li>当需要同时展示多个不同类型的通知信息时</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <el-row>
      <cute-button @click="handleInfoClick">Info 通知</cute-button>
      <cute-button @click="handleWarningClick">Warning 通知</cute-button>
      <cute-button @click="handleSuccessClick">Success 通知</cute-button>
      <cute-button @click="handleErrorClick">Error 通知</cute-button>
    </el-row>
    <h4>快捷调用</h4>
    <el-table :data="apiData">
      <el-table-column prop="name" label="参数" />
      <el-table-column prop="remark" label="说明" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="defaultValue" label="默认值" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
  </div>
</template>
<script>
import CuteButton from '@/views/components/dev/CuteButton.vue'
import KitNotification from '@/utils/elementui/KitNotification'

export default {
  name: 'KitNotificationDemo',
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
      KitNotification.Info('这是一段 Info 通知提示信息')
    },
    handleWarningClick() {
      KitNotification.Warning('这是一段 Warning 通知提示信息')
    },
    handleSuccessClick() {
      KitNotification.Success('这是一段 Success 通知提示信息')
    },
    handleErrorClick() {
      KitNotification.Error('这是一段 Error 通知提示信息')
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
