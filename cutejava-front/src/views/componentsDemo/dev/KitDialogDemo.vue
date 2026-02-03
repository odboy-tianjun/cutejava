<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>不离开当前场景完成增删改操作</li>
      <li>需要确认用户的操作，明确操作会产生的结果，提醒告知用户</li>
      <li>作为配置型功能及信息的容器</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）@Odboy（设计）</li>
    </ul>
    <h4>基础用法</h4>
    <el-row>
      <cute-button @click="handleDeleteConfirm">删除确认框</cute-button>
      <cute-button @click="handleDeleteMessageConfirm">删除确认框(自定义提示)</cute-button>
      <cute-button @click="handleShowConfirm">确认内容消息框</cute-button>
      <cute-button @click="handleTextInputConfirm">数据输入对话框</cute-button>
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
import KitDialog from '@/utils/elementui/KitDialog'

export default {
  name: 'KitDialogDemo',
  components: { CuteButton },
  data() {
    return {
      apiData: [
        { name: 'message', remark: '自定义提示信息', type: 'string', defaultValue: '-', required: '否' },
        { name: 'confirmCallBack', remark: '确定回调', type: '() => {}', defaultValue: '-', required: '否' },
        { name: 'cancelCallBack', remark: '取消回调', type: '() => {}', defaultValue: '-', required: '否' }
      ]
    }
  },
  methods: {
    handleDeleteConfirm() {
      KitDialog.DeleteConfirm(() => {
        KitMessage.Success('确定回调')
      }, () => {
        KitMessage.Warning('取消回调')
      })
    },
    handleDeleteMessageConfirm() {
      KitDialog.DeleteMessageConfirm('请确认是否删除当前麦晓雯', () => {
        KitMessage.Success('确定回调')
      }, () => {
        KitMessage.Warning('取消回调')
      })
    },
    handleShowConfirm() {
      KitDialog.ShowConfirm('温馨提醒', '这里是提醒的内容')
    },
    handleTextInputConfirm() {
      KitDialog.TextInputConfirm('温馨提醒', /^[a-z]+$/, '正则校验输入值均为小写字母不通过', (value) => {
        KitMessage.Success('确定回调，输入的值：' + value)
      }, () => {
        KitMessage.Warning('取消回调')
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
