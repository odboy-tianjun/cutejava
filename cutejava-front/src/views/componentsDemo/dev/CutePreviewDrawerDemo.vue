<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>抽屉是用于在不离开主路径的情况下，提供用户快速执行简单的操作、确认用户信息或反馈提示的辅助窗口</li>
      <li>宽度固定，样式统一</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-button type="primary" @click="handleCutePreviewDrawerShow">显示预览抽屉</cute-button>
    <cute-preview-drawer ref="cpd" title="预览抽屉">
      <p>预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉</p>
    </cute-preview-drawer>
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
import CuteButton from '@/views/components/dev/CuteButton.vue'
import CutePreviewDrawer from '@/views/components/dev/CutePreviewDrawer.vue'

export default {
  name: 'CutePreviewDrawerDemo',
  components: { CutePreviewDrawer, CuteButton },
  data() {
    return {
      apiData: [
        { name: 'title', remark: '标题', type: 'string', defaultValue: '默认标题', required: '否' },
        { name: 'width', remark: '抽屉宽度', type: 'string', defaultValue: '40%', required: '否' },
        { name: 'wrapperClosable', remark: '点击遮罩层是否可以关闭抽屉', type: 'boolean', defaultValue: 'false', required: '否' }
      ]
    }
  },
  methods: {
    handleCutePreviewDrawerShow() {
      this.$refs.cpd.show()
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
