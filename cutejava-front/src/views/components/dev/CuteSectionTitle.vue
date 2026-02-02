<!--
 * 段落标题组件：占一行，支持嵌套按钮
 * @author odboy
 * @email tianjun@odboy.cn
 * @created 2025-08-13
 -->
<template>
  <el-row style="margin: 10px;" class="container-height">
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
    <el-col v-if="$slots.operation" :span="12" class="postfix-operation">
      <slot name="operation" />
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
  margin-bottom: 3px;
  color: #303133;

  i:first-child {
    // 图标与文字的间距
    margin-right: 8px;
  }
}

.sub-title-with-icon-align {
  font-size: 13px;
  color: #606266;
  margin-bottom: 3px;
}

.third-title-with-text-align {
  font-size: 12px;
  color: #909399;
  margin-left: 26px;
  margin-bottom: 0;
}

.postfix-operation {
  text-align: right;
  line-height: 27px;
  padding-right: 30px;
}
</style>
