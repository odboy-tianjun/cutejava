<!--
 * 段落标题组件：占一行，支持嵌套按钮
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-13
 -->
<template>
  <el-row>
    <el-col :span="12">
      <div class="title-container">
        <div class="main-title-line">
          <i class="el-icon-platform-eleme" style="color: #409EFF;font-size: 18px" />
          <i style="color: #303133;font-style: normal">{{ title }}</i>
        </div>
        <div v-if="subTitle" class="sub-title-with-icon-align">{{ subTitle }}</div>
        <div v-if="thirdTitle" class="third-title-with-text-align">{{ thirdTitle }}</div>
      </div>
    </el-col>
    <el-col :span="12" style="text-align: right;line-height: 64px;height: 64px;padding-right: 30px">
      <slot />
    </el-col>
  </el-row>
</template>

<script>

export default {
  name: 'CuteSectionTitle',
  props: {
    title: {
      type: String,
      required: true,
      default: '默认标题'
    },
    subTitle: {
      type: String,
      required: false,
      default: ''
    },
    thirdTitle: {
      type: String,
      required: false,
      default: ''
    }
  }
}
</script>
<style lang="scss" scoped>
.title-container {
  display: flex;
  flex-direction: column;
}

.main-title-line {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  color: #303133;

  i:first-child {
    margin-right: 8px; // 图标与文字的间距
  }
}

.sub-title-with-icon-align {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}

.third-title-with-text-align {
  font-size: 12px;
  color: #909399;
  margin-left: 26px;
  margin-bottom: 0;
}
</style>
