<template>
  <div style="padding: 50px">
    <el-divider>CuteSearchForm</el-divider>
    <cute-search-form :model="searchFormModel" @submit="handleSearchFormSubmit" @reset="handleSearchFormReset">
      <template v-slot:suffix>
        <el-form-item label="模糊查询" prop="blurry">
          <el-input v-model="searchFormModel.blurry" clearable />
        </el-form-item>
      </template>
      <template v-slot:operation>
        <el-button type="primary">查询后置按钮</el-button>
      </template>
    </cute-search-form>
    <el-divider>CuteHorizontalScrollBar</el-divider>
    <cute-horizontal-scroll-bar>
      <div style="width: 3000px;min-width: 3000px">模拟超长内容</div>
    </cute-horizontal-scroll-bar>
    <el-divider>CuteVerticalScrollBar</el-divider>
    <cute-vertical-scroll-bar>
      <div style="height: 3000px;min-height: 3000px">模拟超高内容</div>
    </cute-vertical-scroll-bar>
    <el-divider>CuteLink</el-divider>
    <cute-link title="百度一下" href="https://www.baidu.com" />
    <el-divider>CuteNoData</el-divider>
    <cute-no-data desc="自定义提示语" />
    <el-divider>CutePreviewDrawer <el-button type="primary" @click="handleCutePreviewDrawerShow">显示</el-button></el-divider>
    <cute-preview-drawer ref="cpd" title="预览抽屉">
      <p>预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉预览抽屉</p>
    </cute-preview-drawer>
    <el-divider>截屏：包裹要截屏的组件</el-divider>
    <cute-screen-shot>
      <cute-search-form :model="searchFormModel" @submit="handleSearchFormSubmit" @reset="handleSearchFormReset">
        <template v-slot:suffix>
          <el-form-item label="模糊查询" prop="blurry">
            <el-input v-model="searchFormModel.blurry" clearable />
          </el-form-item>
        </template>
        <template v-slot:operation>
          <el-button type="primary">查询后置按钮</el-button>
        </template>
      </cute-search-form>
    </cute-screen-shot>
    <el-divider>CuteSectionTitle</el-divider>
    <cute-section-title title="段落标题">
      <el-button type="danger">操作按钮</el-button>
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

export default {
  name: 'CuteOtherDemo',
  components: {
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
      searchFormModel: {}
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

