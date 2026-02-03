<template>
  <div>
    <cute-section-title title="高级搜索 CuteSearchForm" />
    <cute-search-form :model="searchFormModel" @submit="handleSearchFormSubmit">
      <template v-slot:suffix>
        <el-form-item label="模糊查询" prop="blurry">
          <el-input v-model="searchFormModel.blurry" clearable />
        </el-form-item>
      </template>
      <template v-slot:operation>
        <el-button type="primary">查询后置按钮</el-button>
      </template>
    </cute-search-form>
    <cute-section-title title="高级搜索Pro版 CuteSearchProForm" />
    <cute-search-pro-form :model="searchFormProValues" :schema="searchFormProSchema" />
  </div>
</template>

<script>

import CuteSearchForm from '@/views/components/advanced/CuteSearchForm.vue'
import CuteSectionTitle from '@/views/components/dev/CuteSectionTitle.vue'
import CuteSearchFormPro from '@/views/components/advanced/CuteSearchFormPro.vue'

export default {
  name: 'CuteOtherDemo',
  components: {
    CuteSearchProForm: CuteSearchFormPro,
    CuteSectionTitle,
    CuteSearchForm
  },
  data() {
    return {
      searchFormModel: {},
      searchFormProSchema: [
        { name: 'name', title: '姓名', type: 'input' },
        { name: 'sex', title: '性别', type: 'select', dataSource: [{ label: '男', value: 'nn' }, { label: '女', value: 'mm' }] },
        { name: 'createDate', title: '创建日期', type: 'date' },
        { name: 'createTime', title: '创建时间', type: 'datetime' }
      ],
      searchFormProValues: { name: '小明', sex: 'mm' }
    }
  },
  methods: {
    handleSearchFormSubmit(data) {
      console.log('handleSearchFormSubmit', data)
    },
    handleSearchFormReset() {
      console.log('handleSearchFormReset')
    }
  }
}
</script>

