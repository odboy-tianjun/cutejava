<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>页面标题、模块标题、区块标题适用</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-section-title title="主标题" />
    <h4>副标题</h4>
    <cute-section-title title="主标题" sub-title="副标题" />
    <h4>三级标题</h4>
    <cute-section-title title="主标题" sub-title="副标题" third-title="三级标题" />
    <h4>主标题按钮</h4>
    <cute-section-title title="主标题">
      <template v-slot:operation>
        <cute-button type="primary">操作按钮1</cute-button>
        <cute-button type="primary">操作按钮2</cute-button>
        <cute-button type="primary">操作按钮3</cute-button>
      </template>
    </cute-section-title>
    <h4>API</h4>
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
import CuteSectionTitle from '@/views/components/dev/CuteSectionTitle.vue'

export default {
  name: 'CuteSectionTitleDemo',
  components: { CuteSectionTitle, CuteButton },
  data() {
    return {
      apiData: [
        { name: 'title', remark: '主标题', type: 'string', defaultValue: '默认标题', required: '是' },
        { name: 'subTitle', remark: '子标题', type: 'string', defaultValue: '-', required: '否' },
        { name: 'thirdTitle', remark: '三级标题', type: 'string', defaultValue: '-', required: '否' }
      ]
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
