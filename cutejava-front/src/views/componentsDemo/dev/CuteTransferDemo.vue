<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>在多个可选项中进行选择，并区分已选和待选状态</li>
      <li>需要明确区分源数据和目标数据的场景</li>
      <li>需要通过搜索功能快速筛选可选项的场景</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-transfer :data-source="leftDataSource" primary-key="id">
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="description" label="备注" />
    </cute-transfer>
    <h4>禁用状态</h4>
    <cute-transfer :data-source="leftDataSource" primary-key="id" disabled>
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="description" label="备注" />
    </cute-transfer>
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
import CuteTransfer from '@/views/components/dev/CuteTransfer.vue'

export default {
  name: 'CuteTransferDemo',
  components: { CuteTransfer },
  data() {
    return {
      leftDataSource: [
        { id: 1, name: '小明', description: '小明牛P' },
        { id: 3, name: '小蓝', description: '小蓝牛P', disabled: true },
        { id: 15, name: '小周', description: '小周牛P' },
        { id: 16, name: '小吴', description: '小吴牛P', disabled: true },
        { id: 18, name: '小王', description: '小王牛P' }
      ],
      apiData: [
        { name: 'value | v-model', remark: '右侧数据对象属性值集合', type: 'array', defaultValue: '[]', required: '否' },
        { name: 'dataSource', remark: '左侧表格数据', type: 'array', defaultValue: '[]', required: '是' },
        { name: 'primary-key', remark: '取对象的哪个值作为value', type: 'string', defaultValue: 'id', required: '否' },
        { name: 'loading', remark: '是否加载中', type: 'boolean', defaultValue: 'false', required: '是' },
        { name: 'change', remark: '右侧数据源发生改变的时候触发的回调', type: '(value) => {}', defaultValue: '-', required: '否' }
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
