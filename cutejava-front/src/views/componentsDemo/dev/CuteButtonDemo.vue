<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>触发提交/执行某个操作</li>
      <li>页面内主、次操作之间需要明确的视觉层级（primary/secondary/normal/text）</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）</li>
    </ul>
    <h4>基础用法</h4>
    <el-row>
      <cute-button>默认按钮</cute-button>
      <cute-button type="primary">主要按钮</cute-button>
      <cute-button type="secondary">次要按钮</cute-button>
      <cute-button type="normal">普通按钮</cute-button>
      <cute-button type="text">文字按钮</cute-button>
    </el-row>
    <h4>禁用状态</h4>
    <el-row>
      <cute-button disabled>默认按钮</cute-button>
      <cute-button disabled type="primary">主要按钮</cute-button>
      <cute-button disabled type="secondary">次要按钮</cute-button>
      <cute-button disabled type="normal">普通按钮</cute-button>
      <cute-button disabled type="text">文字按钮</cute-button>
    </el-row>
    <h4>Loading状态</h4>
    <el-row>
      <cute-button loading>默认按钮</cute-button>
      <cute-button loading type="primary">主要按钮</cute-button>
      <cute-button loading type="secondary">次要按钮</cute-button>
      <cute-button loading type="normal">普通按钮</cute-button>
      <cute-button loading type="text">文字按钮</cute-button>
    </el-row>
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

export default {
  name: 'CuteButtonDemo',
  components: { CuteButton },
  data() {
    return {
      apiData: [
        { name: 'type', remark: '按钮类型', type: 'primary| secondary | normal | text', defaultValue: 'normal', required: '否' },
        { name: 'icon', remark: '内置图标占位', type: '参考ElementUI icon', defaultValue: '-', required: '否' },
        { name: 'disabled', remark: '是否禁用', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'loading', remark: '载入状态（禁用点击，展示 loading）', type: 'boolean', defaultValue: 'false', required: '否' }
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
