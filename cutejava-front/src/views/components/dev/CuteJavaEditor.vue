<!--Yaml编辑器 基于-->
<!--<a href="https://github.com/codemirror/CodeMirror" target="_blank">CodeMirror</a>，-->
<!--主题预览地址 <a href="https://codemirror.net/demo/theme.html#idea" target="_blank">Theme</a>-->
<!--</p>-->
<template>
  <textarea ref="textarea" />
</template>

<script>
import CodeMirror from 'codemirror'
import 'codemirror/lib/codemirror.css'
// 替换主题这里需修改名称
// 主题预览地址 https://blog.csdn.net/qq_41694291/article/details/106429772
import 'codemirror/theme/darcula.css'
import 'codemirror/mode/javascript/javascript'

export default {
  name: 'CuteJavaEditor',
  props: {
    content: {
      type: String,
      required: true,
      default: ''
    },
    height: {
      type: String,
      required: false,
      default: '500px'
    },
    readOnly: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data() {
    return {
      editor: false
    }
  },
  watch: {
    content(newVal, oldVal) {
      const editorValue = this.editor.getValue()
      if (newVal !== editorValue) {
        this.editor.setValue(newVal)
      }
    },
    height(newVal, oldVal) {
      this.editor.setSize('auto', newVal)
    }
  },
  mounted() {
    this.editor = CodeMirror.fromTextArea(this.$refs.textarea, {
      mode: 'javascript',
      autoRefresh: true,
      lineNumbers: true,
      lint: true,
      lineWrapping: true,
      tabSize: 2,
      cursorHeight: 1,
      // 替换主题这里需修改名称
      theme: 'darcula'
    })
    this.editor.setSize('auto', this.height)
    if (this.content) {
      this.editor.setValue(this.content)
    }
    this.editor.setOption('readOnly', this.readOnly)
    this.editor.on('change', cm => {
      this.$emit('change', cm.getValue())
    })
  },
  methods: {
    getValue() {
      if (this.editor == null) {
        return ''
      }
      return this.editor.getValue()
    },
    setValue(val) {
      if (this.editor == null) {
        return
      }
      this.editor.setValue(val)
    },
    resetField() {
      this.editor.setValue('')
    }
  }
}
</script>
