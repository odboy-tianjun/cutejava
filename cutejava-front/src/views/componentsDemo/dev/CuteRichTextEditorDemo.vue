<template>
  <div>
    <h4>何时使用</h4>
    <ul class="description">
      <li>需求输入内容丰富的文本数据</li>
      <li>在使用过程中有任何问题，咨询 @Odboy（前端）@Odboy（设计）@Odboy（后端）</li>
    </ul>
    <h4>基础使用</h4>
    <cute-rich-text-editor @change="onChange" />
    <h4>API</h4>
    <el-table :data="apiData">
      <el-table-column prop="name" label="参数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="type" label="类型" />
      <el-table-column prop="defaultValue" label="默认值" />
      <el-table-column prop="required" label="是否必填" />
    </el-table>
    <h4>方法</h4>
    <el-table :data="methodData">
      <el-table-column prop="name" label="函数" width="220" />
      <el-table-column prop="remark" label="说明" width="450" />
      <el-table-column prop="inputArgs" label="入参" />
      <el-table-column prop="outArgs" label="出参" />
    </el-table>
  </div>
</template>
<script>
import CuteRichTextEditor from '@/views/components/dev/CuteRichTextEditor.vue'

export default {
  name: 'CuteRichTextEditorDemo',
  components: { CuteRichTextEditor },
  data() {
    return {
      content: '',
      apiData: [
        { name: 'content', remark: '内容', type: 'string', defaultValue: '-', required: '是' },
        { name: 'readonly', remark: '是否只读', type: 'boolean', defaultValue: 'false', required: '否' },
        { name: 'change', remark: '内容改变回调事件', type: '(html, text) => {}', defaultValue: '-', required: '否' },
        { name: 'focus', remark: '编辑器获得焦点回调事件', type: '(editor) => {}', defaultValue: '-', required: '否' },
        { name: 'blur', remark: '编辑器失去焦点回调事件', type: '(editor) => {}', defaultValue: '-', required: '否' }
      ],
      methodData: [
        { name: 'setHtml', remark: '设置html内容。如果是 editor.getHtml() 获取的 HTML 格式，可以完美解析', inputArgs: 'html', outArgs: '-' },
        { name: 'getHtml', remark: '获取html内容', inputArgs: '-', outArgs: 'html' },
        { name: 'clear', remark: '清空', inputArgs: '-', outArgs: '-' },
        { name: 'undo', remark: '撤销', inputArgs: '-', outArgs: '-' },
        { name: 'redo', remark: '重做', inputArgs: '-', outArgs: '-' },
        { name: 'getSelectionText', remark: '获取选中的文本', inputArgs: '-', outArgs: 'text' },
        { name: 'deleteFragment', remark: '删除选中的内容', inputArgs: '-', outArgs: '-' },
        { name: 'getFragment', remark: '获取选中的内容。JSON格式', inputArgs: '-', outArgs: 'json' },
        { name: 'getText', remark: '获取文本内容', inputArgs: '-', outArgs: 'text' },
        { name: 'insertText', remark: '在选区插入文本', inputArgs: 'text', outArgs: '-' }
      ]
    }
  },
  methods: {
    onChange(html, text) {
      console.log('onChange:html', html)
      console.log('onChange:text', text)
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
