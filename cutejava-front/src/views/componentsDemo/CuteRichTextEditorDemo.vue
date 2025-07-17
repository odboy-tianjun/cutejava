<template>
  <div class="app-container">
    <cute-rich-text-editor
      :read-only="false"
      @change="onChange"
    />
  </div>
</template>

<script>
import CuteRichTextEditor from '@/views/components/dev/CuteRichTextEditor'

export default {
  name: 'CuteRichTextEditorDemo',
  components: { CuteRichTextEditor },
  data() {
    return {
      html: {
        height: '200px'
      }
    }
  },
  methods: {
    onChange(html, text) {
      console.error('onChange:html', html)
      console.error('onChange:text', text)
    }
  }
}
</script>

