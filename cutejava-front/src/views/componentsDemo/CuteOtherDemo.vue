<template>
  <div>
    <cute-section-title title="高级搜索组件 CuteSearchForm" />
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
    <cute-section-title title="横向滚动组件 CuteHorizontalScrollBar" />
    <cute-horizontal-scroll-bar>
      <div style="width: 3000px;min-width: 3000px">模拟超长内容</div>
    </cute-horizontal-scroll-bar>
    <cute-section-title title="纵向滚动组件 CuteVerticalScrollBar" />
    <cute-vertical-scroll-bar>
      <div style="height: 3000px;min-height: 3000px">模拟超高内容</div>
    </cute-vertical-scroll-bar>
    <cute-section-title title="数据穿梭组件 CuteTransfer" />
    <cute-transfer :data-source="leftDataSource" primary-key="id">
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="description" label="备注" />
    </cute-transfer>
    <cute-section-title title="链接组件 CuteLink" />
    <cute-link title="百度一下" href="https://www.baidu.com" />
    <cute-section-title title="空状态 CuteNoData" />
    <cute-no-data desc="自定义提示语" />
    <cute-section-title title="预览抽屉 CutePreviewDrawer">
      <template v-slot:operation>
        <cute-button type="primary" @click="handleCutePreviewDrawerShow">显示</cute-button>
      </template>
    </cute-section-title>
    <cute-preview-drawer ref="cpd" title="预览抽屉">
      <p>预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉</p>
    </cute-preview-drawer>
    <cute-section-title title="截屏组件 CuteScreenShot：包裹要截屏的组件" />
    <cute-screen-shot>
      <cute-search-form :model="searchFormModel" @submit="handleSearchFormSubmit" @reset="handleSearchFormReset">
        <template v-slot:suffix>
          <el-form-item label="模糊查询" prop="blurry">
            <el-input v-model="searchFormModel.blurry" clearable />
          </el-form-item>
        </template>
        <template v-slot:operation>
          <cute-button type="primary">查询后置按钮</cute-button>
        </template>
      </cute-search-form>
    </cute-screen-shot>
    <cute-section-title title="段落标题 CuteSectionTitle" />
    <cute-section-title title="主标题" sub-title="副标题" third-title="三级标题">
      <cute-button type="danger" size="mini">操作按钮1</cute-button>
      <cute-button type="danger" size="mini">操作按钮2</cute-button>
      <cute-button type="danger" size="mini">操作按钮3</cute-button>
    </cute-section-title>
  </div>
</template>

<script>

import CuteSearchForm from '@/views/components/dev/CuteSearchForm.vue'
import CuteHorizontalScrollBar from '@/views/components/dev/CuteHorizontalScrollBar.vue'
import CuteVerticalScrollBar from '@/views/components/dev/CuteVerticalScrollBar.vue'
import CuteLink from '@/views/components/dev/CuteLink.vue'
import CuteNoData from '@/views/components/dev/CuteNoData.vue'
import CutePreviewDrawer from '@/views/components/dev/CutePreviewDrawer.vue'
import CuteScreenShot from '@/views/components/dev/CuteScreenShot.vue'
import CuteSectionTitle from '@/views/components/dev/CuteSectionTitle.vue'
import CuteButton from '@/views/components/dev/CuteButton.vue'
import CuteTransfer from '@/views/components/dev/CuteTransfer.vue'

export default {
  name: 'CuteOtherDemo',
  components: {
    CuteTransfer,
    CuteButton,
    CuteSectionTitle,
    CuteScreenShot,
    CutePreviewDrawer,
    CuteNoData,
    CuteLink,
    CuteVerticalScrollBar,
    CuteHorizontalScrollBar,
    CuteSearchForm
  },
  data() {
    return {
      searchFormModel: {},
      leftDataSource: [
        { id: 1, name: '小明', description: '小明牛P' },
        { id: 2, name: '小明', description: '小明牛P' },
        { id: 3, name: '小明', description: '小明牛P' },
        { id: 4, name: '小明', description: '小明牛P' },
        { id: 5, name: '小明', description: '小明牛P' }
      ]
    }
  },
  methods: {
    handleSearchFormSubmit(data) {
      console.log('handleSearchFormSubmit', data)
    },
    handleSearchFormReset() {
      console.log('handleSearchFormReset')
    },
    handleCutePreviewDrawerShow() {
      this.$refs.cpd.show()
    }
  }
}
</script>

