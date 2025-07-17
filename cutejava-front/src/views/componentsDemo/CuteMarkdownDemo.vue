<template>
  <div class="app-container">
    <!-- @change 编辑区发生变化的回调事件 -->
    <!-- @save ctrl + s 的回调事件(保存按键,同样触发该回调) -->
    <cute-markdown
      :content="markdown.content"
      @change="onChange"
      @save="onSave"
    />
  </div>
</template>

<script>
import CuteMarkdown from '@/views/components/dev/CuteMarkdown'

export default {
  name: 'CuteMarkdownDemo',
  components: { CuteMarkdown },
  data() {
    return {
      markdown: {
        height: '200px',
        content: '初始化内容'
      }
    }
  },
  methods: {
    onChange(text, markdown) {
      console.error('onChange:text', text)
      console.error('onChange:markdown', markdown)
    },
    onSave(text, markdown) {
      console.error('onSave:text', text)
      console.error('onSave:markdown', markdown)
    }
  }
}
</script>

