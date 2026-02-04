<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>列表上方的标准筛选栏</li>
      <li>在点击搜索和重置按钮时会触发 onSearch，参数为当前筛选值</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-search-form :model="model" @search="handleSearch">
      <template v-slot:suffix>
        <el-form-item label="模糊查询" prop="blurry">
          <el-input v-model="model.blurry" clearable />
        </el-form-item>
      </template>
      <template v-slot:operation>
        <el-button type="primary">查询后置按钮</el-button>
      </template>
    </cute-search-form>
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
import CuteSearchForm from '@/views/components/advanced/CuteSearchForm.vue'

export default {
  name: 'CuteSearchFormDemo',
  components: { CuteSearchForm },
  data() {
    return {
      model: {
        blurry: ''
      },
      apiData: [
        { name: 'model', remark: '绑定的表单对象', type: 'object', defaultValue: '-', required: '是' },
        { name: 'v-slot:suffix', remark: '搜索条件区域', type: 'template', defaultValue: '-', required: '是' },
        { name: 'v-slot:operation', remark: '后置按钮区域', type: 'template', defaultValue: '-', required: '否' },
        { name: 'search', remark: '搜索或重置按钮被点击回调事件', type: '(values) => {}', defaultValue: '-', required: '否' }
      ]
    }
  },
  methods: {
    handleSearch(values) {
      console.log('handleSearch', values)
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
