<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>需求输入Markdown格式数据</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）@Odboy（设计）@Odboy（后端）</li>
    </ul>
    <h4>基础用法</h4>
    <cute-markdown
      :content="content"
      @change="onChange"
      @save="onSave"
    />
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
import CuteMarkdown from '@/views/components/dev/CuteMarkdown.vue'

export default {
  name: 'CuteMarkdownDemo',
  components: { CuteMarkdown },
  data() {
    return {
      content: '#### Markdown 是一种轻量级标记语言，可使用易读易写的纯文本格式编写文档。\n' +
        '\n' +
        '\n' +
        '- 文字;\n' +
        '- Emoji;\n' +
        '- Task lists;\n' +
        '- HTML 标签；\n' +
        '- 流程图和序列图 \n',
      apiData: [
        { name: 'content', remark: 'markdown格式内容', type: 'string', defaultValue: '-', required: '是' },
        { name: 'change', remark: '编辑区发生变化的回调事件', type: '(text, markdown) => {}', defaultValue: '-', required: '否' },
        { name: 'save', remark: 'ctrl + s 的回调事件(保存按键,同样触发该回调)', type: '(text, markdown) => {}', defaultValue: '-', required: '否' }
      ]
    }
  },
  methods: {
    onChange(text, markdown) {
      console.log('onChange:text', text)
      console.log('onChange:markdown', markdown)
    },
    onSave(text, markdown) {
      console.log('onSave:text', text)
      console.log('onSave:markdown', markdown)
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
